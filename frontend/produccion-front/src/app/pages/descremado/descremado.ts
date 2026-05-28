import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import {
  DescremadoRecepcion,
  DescremadoService
} from '../../core/services/descremado';

import {
  RecepcionLeche,
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';

import {
  ControlCalidadLacteaService,
  EstadoCalidadRecepcion
} from '../../core/services/control-calidad-lactea';

import { AuthService } from '../../core/services/auth';
import { NotificationService } from '../../core/services/notification';

@Component({
  selector: 'app-descremado',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './descremado.html',
  styleUrl: './descremado.scss'
})
export class Descremado implements OnInit {

  cargando = false;
  error = '';

  descremados: DescremadoRecepcion[] = [];
  recepciones: RecepcionLeche[] = [];
  tanques: SaldoTanqueLeche[] = [];
  estadosCalidad: EstadoCalidadRecepcion[] = [];

  filtroFecha = '';
  filtroProveedor = '';
  filtroTanque = '';
  filtroLote = '';

  paginaActual = 1;
  tamanioPagina = 10;

  constructor(
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
    private controlCalidadService: ControlCalidadLacteaService,
    public authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.error = '';

    this.descremadoService.listar().subscribe({
      next: (descremados: DescremadoRecepcion[]) => {
        this.descremados = descremados || [];

        this.recepcionLecheService.listarRecepciones().subscribe({
          next: (recepciones: RecepcionLeche[]) => {
            this.recepciones = recepciones || [];

            this.recepcionLecheService.listarSaldosTanques().subscribe({
              next: (tanques: SaldoTanqueLeche[]) => {
                this.tanques = tanques || [];

                this.controlCalidadService.listarEstadosRecepcion().subscribe({
                  next: (estados: EstadoCalidadRecepcion[]) => {
                    this.estadosCalidad = estados || [];
                    this.cargando = false;
                  },
                  error: (err) => {
                    console.error(err);
                    this.error = 'No se pudieron cargar los estados de calidad.';
                    this.notification.error(this.error);
                    this.cargando = false;
                  }
                });
              },
              error: (err) => {
                console.error(err);
                this.error = 'No se pudieron cargar los tanques.';
                this.notification.error(this.error);
                this.cargando = false;
              }
            });
          },
          error: (err) => {
            console.error(err);
            this.error = 'No se pudieron cargar las recepciones de leche.';
            this.notification.error(this.error);
            this.cargando = false;
          }
        });
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los registros de descremado.';
        this.notification.error(this.error);
        this.cargando = false;
      }
    });
  }

  lecheRecibidaHoy(): number {
    const hoy = this.fechaHoyLocal();

    return this.recepciones
      .filter(recepcion => this.esMismaFecha(recepcion.fechaRecepcion, hoy))
      .reduce((total, recepcion) => total + Number(recepcion.cantidadRecibidaLitros || 0), 0);
  }

  lecheDisponibleParaDescremar(): number {
    return this.tanques.reduce((total, tanque: any) => {
      const saldo = Number(
        tanque.saldoActualLitros ??
        tanque.saldoLitros ??
        tanque.cantidadActualLitros ??
        tanque.litrosDisponibles ??
        tanque.disponibleLitros ??
        0
      );

      return total + saldo;
    }, 0);
  }

  lecheDescremadaHoy(): number {
    const hoy = this.fechaHoyLocal();

    return this.descremados
      .filter(descremado => this.esMismaFecha(descremado.createdAt, hoy))
      .reduce((total, descremado) => total + Number(descremado.litrosDescremados || 0), 0);
  }

  cremaObtenidaHoy(): number {
    const hoy = this.fechaHoyLocal();

    return this.descremados
      .filter(descremado => this.esMismaFecha(descremado.createdAt, hoy))
      .reduce((total, descremado) => total + Number(descremado.cremaObtenidaKg || 0), 0);
  }

  proveedoresDisponibles(): string[] {
    const proveedores = this.recepciones
      .map(recepcion => recepcion.proveedor)
      .filter((proveedor): proveedor is string => Boolean(proveedor));

    return Array.from(new Set(proveedores)).sort();
  }

  get descremadosFiltrados(): DescremadoRecepcion[] {
    return this.descremados
      .filter(item => {
        const recepcion = this.buscarRecepcion(item.idRecepcionLeche);

        const coincideFecha = !this.filtroFecha
          || this.esMismaFecha(item.createdAt, this.filtroFecha)
          || this.esMismaFecha(recepcion?.fechaRecepcion, this.filtroFecha);

        const coincideProveedor = !this.filtroProveedor
          || recepcion?.proveedor === this.filtroProveedor;

        const coincideTanque = !this.filtroTanque
          || String(item.idTanqueDestino || '') === String(this.filtroTanque);

        const coincideLote = !this.filtroLote
          || (item.loteCrema || '').toLowerCase().includes(this.filtroLote.toLowerCase());

        return coincideFecha && coincideProveedor && coincideTanque && coincideLote;
      })
      .sort((a, b) => this.compararDescremadosRecientes(a, b));
  }

  get descremadosPaginados(): DescremadoRecepcion[] {
    const inicio = (this.paginaActual - 1) * this.tamanioPagina;
    const fin = inicio + this.tamanioPagina;

    return this.descremadosFiltrados.slice(inicio, fin);
  }

  get totalPaginas(): number {
    return Math.max(Math.ceil(this.descremadosFiltrados.length / this.tamanioPagina), 1);
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, index) => index + 1);
  }

  cambiarPagina(pagina: number): void {
    if (pagina < 1 || pagina > this.totalPaginas) {
      return;
    }

    this.paginaActual = pagina;
  }

  limpiarFiltros(): void {
    this.filtroFecha = '';
    this.filtroProveedor = '';
    this.filtroTanque = '';
    this.filtroLote = '';
    this.paginaActual = 1;
  }

  obtenerRecepcion(idRecepcionLeche: number): string {
    const recepcion = this.buscarRecepcion(idRecepcionLeche);

    if (!recepcion) {
      return `Recepción #${idRecepcionLeche}`;
    }

    const fecha = this.normalizarFecha(recepcion.fechaRecepcion);
    const proveedor = recepcion.proveedor || 'Sin proveedor';
    const remision = recepcion.numeroRemision ? ` - Rem. ${recepcion.numeroRemision}` : '';

    return `${fecha} - ${proveedor}${remision}`;
  }

  litrosRecibidosRecepcion(idRecepcionLeche: number): number {
    const recepcion = this.buscarRecepcion(idRecepcionLeche);
    return Number(recepcion?.cantidadRecibidaLitros || 0);
  }

  litrosRestantesRecepcion(idRecepcionLeche: number): number {
    const recibido = this.litrosRecibidosRecepcion(idRecepcionLeche);

    const descremado = this.descremados
      .filter(item => Number(item.idRecepcionLeche) === Number(idRecepcionLeche))
      .reduce((total, item) => total + Number(item.litrosDescremados || 0), 0);

    return Math.max(recibido - descremado, 0);
  }

  obtenerTanque(idTanqueDestino?: number): string {
    if (!idTanqueDestino) {
      return 'Sin tanque';
    }

    const tanque = this.tanques.find(item => Number(item.idTanque) === Number(idTanqueDestino));

    return tanque?.nombre || `Tanque #${idTanqueDestino}`;
  }

  estadoRecepcion(idRecepcionLeche: number): string {
    const recibido = this.litrosRecibidosRecepcion(idRecepcionLeche);
    const restante = this.litrosRestantesRecepcion(idRecepcionLeche);

    if (recibido <= 0) {
      return 'Sin dato';
    }

    if (restante <= 0) {
      return 'Completa';
    }

    if (restante < recibido) {
      return 'Parcial';
    }

    return 'Pendiente';
  }

  claseEstado(idRecepcionLeche: number): string {
    const estado = this.estadoRecepcion(idRecepcionLeche);

    if (estado === 'Completa') {
      return 'bg-emerald-50 text-emerald-700 border-emerald-100';
    }

    if (estado === 'Parcial') {
      return 'bg-amber-50 text-amber-700 border-amber-100';
    }

    return 'bg-slate-50 text-slate-600 border-slate-100';
  }

  estadoCalidadRecepcion(idRecepcionLeche: number): string {
    return this.estadosCalidad.find(item => Number(item.idRecepcionLeche) === Number(idRecepcionLeche))?.estadoCalidad || 'SIN_CALIDAD';
  }

  private compararDescremadosRecientes(a: DescremadoRecepcion, b: DescremadoRecepcion): number {
    const fechaA = this.obtenerTiempoDescremado(a);
    const fechaB = this.obtenerTiempoDescremado(b);

    if (fechaA !== fechaB) {
      return fechaB - fechaA;
    }

    return Number(b.id || 0) - Number(a.id || 0);
  }

  private obtenerTiempoDescremado(descremado: DescremadoRecepcion): number {
    const tiempo = new Date(descremado.createdAt || '').getTime();

    if (!Number.isNaN(tiempo)) {
      return tiempo;
    }

    return 0;
  }

  private buscarRecepcion(idRecepcionLeche: number): RecepcionLeche | undefined {
    return this.recepciones.find(recepcion => Number(recepcion.id) === Number(idRecepcionLeche));
  }

  private esMismaFecha(fecha: unknown, fechaComparar: string): boolean {
    return this.normalizarFecha(fecha) === fechaComparar;
  }

  private fechaHoyLocal(): string {
    const hoy = new Date();
    const year = hoy.getFullYear();
    const month = String(hoy.getMonth() + 1).padStart(2, '0');
    const day = String(hoy.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  private normalizarFecha(fecha: unknown): string {
    if (!fecha) {
      return '';
    }

    if (typeof fecha === 'string') {
      if (fecha.includes('T')) {
        return fecha.split('T')[0];
      }

      return fecha.substring(0, 10);
    }

    if (fecha instanceof Date) {
      const year = fecha.getFullYear();
      const month = String(fecha.getMonth() + 1).padStart(2, '0');
      const day = String(fecha.getDate()).padStart(2, '0');

      return `${year}-${month}-${day}`;
    }

    if (Array.isArray(fecha) && fecha.length >= 3) {
      const year = fecha[0];
      const month = String(fecha[1]).padStart(2, '0');
      const day = String(fecha[2]).padStart(2, '0');

      return `${year}-${month}-${day}`;
    }

    return String(fecha).substring(0, 10);
  }
}