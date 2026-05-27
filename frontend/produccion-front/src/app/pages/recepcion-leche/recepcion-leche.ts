import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  RecepcionLeche as RecepcionLecheModel,
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';
import {
  CalidadRecepcionLecheResponse,
  ControlCalidadLacteaService,
  EstadoCalidadRecepcion
} from '../../core/services/control-calidad-lactea';

import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth';
import { NotificationService } from '../../core/services/notification';

@Component({
  selector: 'app-recepcion-leche',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './recepcion-leche.html',
  
})
export class RecepcionLeche implements OnInit {

  recepciones: RecepcionLecheModel[] = [];
  saldos: SaldoTanqueLeche[] = [];

  cargando = false;
  error = '';
  filtro = '';
  filtroProveedor = '';
  filtroFecha = '';
  filtroEstadoCalidad = '';
  recepcionSeleccionada: RecepcionLecheModel | null = null;
  controlesRecepcion: CalidadRecepcionLecheResponse[] = [];
  cargandoCalidad = false;
  guardandoCalidad = false;
  idControlCalidadEditando: number | null = null;

  calidadForm = {
    pruebaAlcoholOk: true,
    lactoscanOk: true,
    acidez: null as number | null,
    densidad: null as number | null,
    grasa: null as number | null,
    aguaPct: null as number | null,
    temperatura: null as number | null,
    ph: null as number | null,
    aprobado: true,
    retenido: false,
    observaciones: ''
  };

  // Métricas para el Panel Superior
  totalLitrosMes = 0;
  proveedorTop = '-';
  promedioLitros = 0;
  maxLitros = 1; // Para la escala visual

  constructor(
    private service: RecepcionLecheService,
    private controlCalidadService: ControlCalidadLacteaService,
    public authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    if (this.authService.isAuxiliarCalidad()) {
      this.filtroEstadoCalidad = 'SIN_CALIDAD';
    }
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.error = '';

    this.service.listarRecepciones().subscribe({
      next: (data) => {
        const sorted = data
          .slice()
          .sort((a, b) => this.fechaOrdenable(b).localeCompare(this.fechaOrdenable(a)));

        // Fusionar estados de calidad
        this.controlCalidadService.listarEstadosRecepcion().subscribe({
          next: (estados: EstadoCalidadRecepcion[]) => {
            const mapaEstados = new Map<number, string>(
              estados.map(e => [e.idRecepcionLeche, e.estadoCalidad])
            );
            this.recepciones = sorted.map(r => ({
              ...r,
              estadoCalidad: (mapaEstados.get(r.id) ?? 'SIN_CALIDAD') as any
            }));
            this.paginaActual = 1;
            this.calcularMetricas(this.recepciones);
          },
          error: () => {
            this.recepciones = sorted.map(r => ({ ...r, estadoCalidad: 'SIN_CALIDAD' as any }));
            this.paginaActual = 1;
            this.calcularMetricas(this.recepciones);
          }
        });

        this.cargando = false;
      },
      error: (err) => {
        const mensaje = err.error?.message || 'No se pudieron cargar las recepciones de leche.';
        this.error = mensaje;
        this.notification.error(mensaje);
        this.cargando = false;
      }
    });

    this.service.listarSaldosTanques().subscribe({
      next: (data) => {
        this.saldos = data;
      }
    });
  }

  // Paginación
  paginaActual = 1;
  itemsPorPagina = 10;

  get recepcionesFiltradas(): RecepcionLecheModel[] {
    const f = this.filtro.toLowerCase().trim();
    const proveedor = this.filtroProveedor.toLowerCase().trim();
    const fecha = this.filtroFecha;
    const estadoCalidad = this.filtroEstadoCalidad;

    return this.recepciones.filter((r: RecepcionLecheModel) => {
      const coincideTexto = !f
        || (r.proveedor && r.proveedor.toLowerCase().includes(f))
        || (r.numeroRemision && r.numeroRemision.toLowerCase().includes(f))
        || (r.observaciones && r.observaciones.toLowerCase().includes(f));

      const coincideProveedor = !proveedor
        || (r.proveedor && r.proveedor.toLowerCase() === proveedor);

      const coincideFecha = !fecha || r.fechaRecepcion === fecha;
      const coincideEstado = !estadoCalidad || (r.estadoCalidad || 'SIN_CALIDAD') === estadoCalidad;

      return coincideTexto && coincideProveedor && coincideFecha && coincideEstado;
    });
  }

  get recepcionesPaginadas(): RecepcionLecheModel[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.recepcionesFiltradas.slice(inicio, inicio + this.itemsPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.recepcionesFiltradas.length / this.itemsPorPagina);
  }

  get paginasVisibles(): (number | string)[] {
    const total = this.totalPaginas;
    const actual = this.paginaActual;
    const paginas: (number | string)[] = [];

    if (total <= 7) {
      return Array.from({ length: total }, (_, i) => i + 1);
    }

    paginas.push(1);

    if (actual > 3) {
      paginas.push('...');
    }

    const inicio = Math.max(2, actual - 1);
    const fin = Math.min(total - 1, actual + 1);

    for (let i = inicio; i <= fin; i++) {
      paginas.push(i);
    }

    if (actual < total - 2) {
      paginas.push('...');
    }

    paginas.push(total);

    return paginas;
  }

  cambiarPagina(p: number | string): void {
    if (typeof p === 'number' && p >= 1 && p <= this.totalPaginas) {
      this.paginaActual = p;
    }
  }

  limpiarFiltros(): void {
    this.filtro = '';
    this.filtroProveedor = '';
    this.filtroFecha = '';
    this.filtroEstadoCalidad = this.authService.isAuxiliarCalidad() ? 'SIN_CALIDAD' : '';
    this.paginaActual = 1;
  }

  abrirDetalle(recepcion: RecepcionLecheModel): void {
    this.recepcionSeleccionada = recepcion;
    this.cargarCalidadRecepcion(recepcion.id);
  }

  cerrarDetalle(): void {
    this.recepcionSeleccionada = null;
    this.controlesRecepcion = [];
    this.idControlCalidadEditando = null;
    this.resetCalidadForm();
  }

  cargarCalidadRecepcion(idRecepcionLeche: number): void {
    this.cargandoCalidad = true;

    this.controlCalidadService.listarRecepcion(idRecepcionLeche).subscribe({
      next: (data) => {
        this.controlesRecepcion = data;
        this.actualizarEstadoSeleccionado();
        this.cargandoCalidad = false;
      },
      error: (err) => {
        console.error(err);
        this.controlesRecepcion = [];
        this.cargandoCalidad = false;
      }
    });
  }

  registrarCalidadRecepcion(): void {
    if (!this.recepcionSeleccionada) {
      return;
    }

    this.guardandoCalidad = true;

    const request = {
      idRecepcionLeche: this.recepcionSeleccionada.id,
      pruebaAlcoholOk: this.calidadForm.pruebaAlcoholOk,
      lactoscanOk: this.calidadForm.lactoscanOk,
      acidez: this.calidadForm.acidez,
      densidad: this.calidadForm.densidad,
      grasa: this.calidadForm.grasa,
      aguaPct: this.calidadForm.aguaPct,
      temperatura: this.calidadForm.temperatura,
      ph: this.calidadForm.ph,
      aprobado: this.calidadForm.aprobado,
      retenido: this.calidadForm.retenido,
      idRealizadoPor: this.authService.getIdUsuario(),
      observaciones: this.calidadForm.observaciones || null
    };

    const operacion = this.idControlCalidadEditando
      ? this.controlCalidadService.actualizarRecepcion(this.idControlCalidadEditando, request)
      : this.controlCalidadService.registrarRecepcion(request);

    operacion.subscribe({
      next: (control) => {
        const estabaEditando = !!this.idControlCalidadEditando;
        this.controlesRecepcion = estabaEditando
          ? this.controlesRecepcion.map(item => item.id === control.id ? control : item)
          : [control, ...this.controlesRecepcion];
        this.actualizarEstadoSeleccionado();
        this.guardandoCalidad = false;
        this.resetCalidadForm();
        this.notification.success(estabaEditando
          ? 'Control de calidad actualizado correctamente.'
          : 'Control de calidad de recepcion registrado correctamente.');
      },
      error: (err) => {
        const mensaje = err.error?.message || 'No se pudo registrar el control de calidad.';
        this.notification.error(mensaje);
        this.guardandoCalidad = false;
      }
    });
  }

  resetCalidadForm(): void {
    this.idControlCalidadEditando = null;
    this.calidadForm = {
      pruebaAlcoholOk: true,
      lactoscanOk: true,
      acidez: null,
      densidad: null,
      grasa: null,
      aguaPct: null,
      temperatura: null,
      ph: null,
      aprobado: true,
      retenido: false,
      observaciones: ''
    };
  }

  get tieneControlCalidad(): boolean {
    return this.controlesRecepcion.length > 0;
  }

  get ultimoControlCalidad(): CalidadRecepcionLecheResponse | null {
    return this.controlesRecepcion[0] || null;
  }

  get puedeRegistrarControlCalidad(): boolean {
    return this.authService.canWriteCalidad() && !this.guardandoCalidad;
  }

  editarControlRecepcion(control: CalidadRecepcionLecheResponse): void {
    if (!this.authService.canWriteCalidad()) return;

    this.idControlCalidadEditando = control.id;
    this.calidadForm = {
      pruebaAlcoholOk: !!control.pruebaAlcoholOk,
      lactoscanOk: !!control.lactoscanOk,
      acidez: control.acidez ?? null,
      densidad: control.densidad ?? null,
      grasa: control.grasa ?? null,
      aguaPct: control.aguaPct ?? null,
      temperatura: control.temperatura ?? null,
      ph: control.ph ?? null,
      aprobado: !!control.aprobado,
      retenido: !!control.retenido,
      observaciones: control.observaciones || ''
    };
  }

  eliminarControlRecepcion(control: CalidadRecepcionLecheResponse): void {
    if (!this.authService.canWriteCalidad()) return;
    if (!confirm('¿Eliminar este control de calidad?')) return;

    this.controlCalidadService.eliminarRecepcion(control.id).subscribe({
      next: () => {
        this.controlesRecepcion = this.controlesRecepcion.filter(item => item.id !== control.id);
        this.actualizarEstadoSeleccionado();
        if (this.idControlCalidadEditando === control.id) {
          this.resetCalidadForm();
        }
        this.notification.success('Control de calidad eliminado correctamente.');
      },
      error: err => this.notification.error(err.error?.message || 'No se pudo eliminar el control de calidad.')
    });
  }

  estadoTextoControl(control: CalidadRecepcionLecheResponse | null): string {
    if (!control) return 'Sin calidad';
    if (control.retenido) return 'Retenida';
    return control.aprobado ? 'Aprobada' : 'No aprobada';
  }

  claseEstadoControl(control: CalidadRecepcionLecheResponse | null): string {
    if (!control) return 'text-slate-500 bg-slate-100';
    if (control.retenido) return 'text-red-700 bg-red-50';
    return control.aprobado ? 'text-emerald-700 bg-emerald-50' : 'text-orange-700 bg-orange-50';
  }

  get proveedoresDisponibles(): string[] {
    return Array.from(new Set(this.recepciones.map(r => r.proveedor).filter(Boolean))).sort();
  }

  diferenciaLitros(recepcion: RecepcionLecheModel): number {
    return Number(recepcion.cantidadRecibidaLitros || 0) - Number(recepcion.cantidadRemisionLitros || 0);
  }

  porcentajeDiferencia(recepcion: RecepcionLecheModel): number {
    const remision = Number(recepcion.cantidadRemisionLitros || 0);
    if (remision <= 0) return 0;
    return (this.diferenciaLitros(recepcion) / remision) * 100;
  }

  claseDiferencia(recepcion: RecepcionLecheModel): string {
    const porcentaje = Math.abs(this.porcentajeDiferencia(recepcion));
    if (porcentaje >= 5) return 'text-red-700 bg-red-50 border-red-100';
    if (porcentaje >= 2) return 'text-amber-700 bg-amber-50 border-amber-100';
    return 'text-emerald-700 bg-emerald-50 border-emerald-100';
  }

  colorProveedor(proveedor: string): string {
    const colores = [
      'bg-blue-100 text-blue-700',
      'bg-emerald-100 text-emerald-700',
      'bg-amber-100 text-amber-700',
      'bg-violet-100 text-violet-700',
      'bg-rose-100 text-rose-700',
      'bg-cyan-100 text-cyan-700'
    ];
    const total = (proveedor || '').split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    return colores[total % colores.length];
  }

  private fechaOrdenable(recepcion: RecepcionLecheModel): string {
    return `${recepcion.fechaRecepcion || ''}-${String(recepcion.id || 0).padStart(8, '0')}`;
  }

  private actualizarEstadoSeleccionado(): void {
    if (!this.recepcionSeleccionada) {
      return;
    }

    const ultimo = this.ultimoControlCalidad;
    const estado = ultimo
      ? (ultimo.retenido ? 'RETENIDA' : (ultimo.aprobado ? 'APROBADA' : 'NO_APROBADA'))
      : 'SIN_CALIDAD';

    this.recepcionSeleccionada = {
      ...this.recepcionSeleccionada,
      estadoCalidad: estado as any
    };

    this.recepciones = this.recepciones.map(recepcion =>
      recepcion.id === this.recepcionSeleccionada?.id
        ? { ...recepcion, estadoCalidad: estado as any }
        : recepcion
    );
  }

  private calcularMetricas(data: RecepcionLecheModel[]): void {
    if (!data || data.length === 0) return;

    this.totalLitrosMes = data.reduce((acc, r) => acc + (r.cantidadRecibidaLitros || 0), 0);
    this.promedioLitros = this.totalLitrosMes / data.length;
    this.maxLitros = Math.max(...data.map(r => r.cantidadRecibidaLitros || 0));

    const conteoProveedores = data.reduce((acc, r) => {
      acc[r.proveedor] = (acc[r.proveedor] || 0) + (r.cantidadRecibidaLitros || 0);
      return acc;
    }, {} as Record<string, number>);

    this.proveedorTop = Object.keys(conteoProveedores).reduce((a, b) =>
      conteoProveedores[a] > conteoProveedores[b] ? a : b, '-'
    );
  }
}
