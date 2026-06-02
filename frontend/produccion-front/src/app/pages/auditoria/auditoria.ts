import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AuditoriaResponse, AuditoriaService } from '../../core/services/auditoria';

type FiltroAccionAuditoria = 'TODAS' | 'CREAR' | 'ACTUALIZAR' | 'ELIMINAR' | 'CAMBIAR_ESTADO';
type FiltroModuloAuditoria =
  | 'TODOS'
  | 'CALIDAD'
  | 'ORDENES'
  | 'RECEPCION'
  | 'DESCREMADO'
  | 'USUARIOS'
  | 'PRODUCTO_TERMINADO'
  | 'OTROS';

@Component({
  selector: 'app-auditoria',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auditoria.html',
})
export class Auditoria implements OnInit {

  registros: AuditoriaResponse[] = [];
  registroDetalle: AuditoriaResponse | null = null;

  cargando = false;
  error = '';

  limite = 300;

  filtroTexto = '';
  filtroAccion: FiltroAccionAuditoria = 'TODAS';
  filtroModulo: FiltroModuloAuditoria = 'TODOS';
  filtroUsuario = '';
  filtroFechaDesde = '';
  filtroFechaHasta = '';

  constructor(private auditoriaService: AuditoriaService) { }

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';

    this.auditoriaService.listarUltimos(this.limite).subscribe({
      next: (data) => {
        this.registros = data || [];
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cargar el historial de auditoría.';
        this.cargando = false;
      }
    });
  }

  limpiarFiltros(): void {
    this.filtroTexto = '';
    this.filtroAccion = 'TODAS';
    this.filtroModulo = 'TODOS';
    this.filtroUsuario = '';
    this.filtroFechaDesde = '';
    this.filtroFechaHasta = '';
  }

  abrirDetalle(item: AuditoriaResponse): void {
    this.registroDetalle = item;
  }

  cerrarDetalle(): void {
    this.registroDetalle = null;
  }

  get registrosFiltrados(): AuditoriaResponse[] {
    const texto = this.normalizarTexto(this.filtroTexto);
    const usuario = this.normalizarTexto(this.filtroUsuario);

    return this.registros.filter(item => {
      const fecha = this.fechaSolo(item.fechaHora);

      const coincideTexto = !texto || this.normalizarTexto([
        item.accion,
        item.entidadAfectada,
        item.idRegistroAfectado,
        item.detalle,
        item.nombreUsuario,
        this.moduloAmigable(item),
        this.resumenAmigable(item),
        this.referenciaRegistro(item)
      ].join(' ')).includes(texto);

      const coincideAccion = this.filtroAccion === 'TODAS' || item.accion === this.filtroAccion;
      const coincideModulo = this.filtroModulo === 'TODOS' || this.moduloTecnico(item) === this.filtroModulo;
      const coincideUsuario = !usuario || this.normalizarTexto(item.nombreUsuario || `Usuario ${item.idUsuario}`).includes(usuario);

      const coincideFechaDesde = !this.filtroFechaDesde || fecha >= this.filtroFechaDesde;
      const coincideFechaHasta = !this.filtroFechaHasta || fecha <= this.filtroFechaHasta;

      return coincideTexto &&
        coincideAccion &&
        coincideModulo &&
        coincideUsuario &&
        coincideFechaDesde &&
        coincideFechaHasta;
    });
  }

  get totalEventos(): number {
    return this.registros.length;
  }

  get totalFiltrados(): number {
    return this.registrosFiltrados.length;
  }

  get totalCreaciones(): number {
    return this.registros.filter(item => item.accion === 'CREAR').length;
  }

  get totalActualizaciones(): number {
    return this.registros.filter(item => item.accion === 'ACTUALIZAR' || item.accion === 'CAMBIAR_ESTADO').length;
  }

  get totalEliminaciones(): number {
    return this.registros.filter(item => item.accion === 'ELIMINAR').length;
  }

  get totalCalidad(): number {
    return this.registros.filter(item => this.moduloTecnico(item) === 'CALIDAD').length;
  }

  claseAccion(accion: string): string {
    switch (accion) {
      case 'CREAR':
        return 'bg-emerald-50 text-emerald-700 border-emerald-100';
      case 'ACTUALIZAR':
        return 'bg-blue-50 text-blue-700 border-blue-100';
      case 'CAMBIAR_ESTADO':
        return 'bg-amber-50 text-amber-700 border-amber-100';
      case 'ELIMINAR':
        return 'bg-red-50 text-red-700 border-red-100';
      default:
        return 'bg-slate-50 text-slate-600 border-slate-100';
    }
  }

  claseModulo(item: AuditoriaResponse): string {
    const modulo = this.moduloTecnico(item);

    switch (modulo) {
      case 'CALIDAD':
        return 'bg-violet-50 text-violet-700 border-violet-100';
      case 'ORDENES':
        return 'bg-blue-50 text-blue-700 border-blue-100';
      case 'RECEPCION':
        return 'bg-cyan-50 text-cyan-700 border-cyan-100';
      case 'DESCREMADO':
        return 'bg-indigo-50 text-indigo-700 border-indigo-100';
      case 'USUARIOS':
        return 'bg-slate-100 text-slate-700 border-slate-200';
      case 'PRODUCTO_TERMINADO':
        return 'bg-emerald-50 text-emerald-700 border-emerald-100';
      default:
        return 'bg-slate-50 text-slate-600 border-slate-100';
    }
  }

  iconoAccion(accion: string): string {
    switch (accion) {
      case 'CREAR':
        return 'bi-plus-circle';
      case 'ACTUALIZAR':
        return 'bi-pencil-square';
      case 'CAMBIAR_ESTADO':
        return 'bi-arrow-repeat';
      case 'ELIMINAR':
        return 'bi-trash';
      default:
        return 'bi-clock-history';
    }
  }

  accionAmigable(accion: string): string {
    switch (accion) {
      case 'CREAR':
        return 'Creación';
      case 'ACTUALIZAR':
        return 'Actualización';
      case 'CAMBIAR_ESTADO':
        return 'Cambio de estado';
      case 'ELIMINAR':
        return 'Eliminación';
      default:
        return accion || 'Movimiento';
    }
  }

  moduloTecnico(item: AuditoriaResponse): FiltroModuloAuditoria {
    const entidad = this.normalizarTexto(item.entidadAfectada);
    const detalle = this.normalizarTexto(item.detalle || '');

    if (
      entidad.includes('CALIDAD') ||
      entidad.includes('MEDICION_CALIDAD') ||
      entidad.includes('CONTROL_CALIDAD') ||
      detalle.includes('CONTROLES-CALIDAD') ||
      detalle.includes('MEDICIONES-CALIDAD')
    ) {
      return 'CALIDAD';
    }

    if (
      entidad.includes('ORDEN') ||
      detalle.includes('ORDENES-PRODUCCION')
    ) {
      return 'ORDENES';
    }

    if (
      entidad.includes('RECEPCION') ||
      detalle.includes('RECEPCION')
    ) {
      return 'RECEPCION';
    }

    if (
      entidad.includes('DESCREMADO') ||
      detalle.includes('DESCREMADO')
    ) {
      return 'DESCREMADO';
    }

    if (
      entidad.includes('USUARIO') ||
      detalle.includes('USUARIOS')
    ) {
      return 'USUARIOS';
    }

    if (
      entidad.includes('PRODUCTO_TERMINADO') ||
      detalle.includes('PRODUCTO-TERMINADO') ||
      detalle.includes('PESO')
    ) {
      return 'PRODUCTO_TERMINADO';
    }

    return 'OTROS';
  }

  moduloAmigable(item: AuditoriaResponse): string {
    const modulo = this.moduloTecnico(item);

    switch (modulo) {
      case 'CALIDAD':
        return 'Calidad láctea';
      case 'ORDENES':
        return 'Órdenes de producción';
      case 'RECEPCION':
        return 'Recepción de leche';
      case 'DESCREMADO':
        return 'Descremado';
      case 'USUARIOS':
        return 'Usuarios';
      case 'PRODUCTO_TERMINADO':
        return 'Producto terminado';
      default:
        return this.entidadAmigable(item.entidadAfectada);
    }
  }

  entidadAmigable(entidad: string): string {
    const normalizada = this.normalizarTexto(entidad);

    if (normalizada.includes('CONTROL_CALIDAD_LACTEA')) return 'Control de calidad láctea';
    if (normalizada.includes('MEDICION_CALIDAD_LACTEA')) return 'Medición de calidad láctea';
    if (normalizada.includes('ORDEN_PRODUCCION')) return 'Orden de producción';
    if (normalizada.includes('RECEPCION')) return 'Recepción de leche';
    if (normalizada.includes('DESCREMADO')) return 'Descremado';
    if (normalizada.includes('USUARIO')) return 'Usuario';
    if (normalizada.includes('PRODUCTO_TERMINADO')) return 'Producto terminado';

    return String(entidad || 'Registro del sistema')
      .replaceAll('_', ' ')
      .toLowerCase()
      .replace(/\b\w/g, letra => letra.toUpperCase());
  }

  referenciaRegistro(item: AuditoriaResponse): string {
    const entidad = this.entidadAmigable(item.entidadAfectada);
    const id = item.idRegistroAfectado ? ` #${item.idRegistroAfectado}` : '';
    return `${entidad}${id}`;
  }

  resumenAmigable(item: AuditoriaResponse): string {
    const modulo = this.moduloAmigable(item);
    const entidad = this.entidadAmigable(item.entidadAfectada);
    const referencia = item.idRegistroAfectado ? ` #${item.idRegistroAfectado}` : '';
    const detalle = this.normalizarTexto(item.detalle || '');

    if (detalle.includes('CERRAR-TANDAS')) {
      return `Se cerró el registro de tandas de una orden de producción.`;
    }

    if (detalle.includes('REABRIR-TANDAS')) {
      return `Se reabrió el registro de tandas de una orden de producción.`;
    }

    if (detalle.includes('CONTROLES-CALIDAD-LACTEA/PROCESO')) {
      return item.accion === 'CREAR'
        ? 'Se registró un control de proceso de calidad.'
        : 'Se modificó un control de proceso de calidad.';
    }

    if (detalle.includes('CONTROLES-CALIDAD-LACTEA/PESO')) {
      return item.accion === 'CREAR'
        ? 'Se registró un control de peso de producto terminado.'
        : 'Se modificó un control de peso de producto terminado.';
    }

    if (detalle.includes('MEDICIONES-CALIDAD-LACTEA')) {
      return item.accion === 'CREAR'
        ? 'Se registró una medición rápida de Brix / pH.'
        : 'Se modificó una medición rápida de Brix / pH.';
    }

    switch (item.accion) {
      case 'CREAR':
        return `Se creó un registro en ${modulo}: ${entidad}${referencia}.`;
      case 'ACTUALIZAR':
        return `Se actualizó un registro en ${modulo}: ${entidad}${referencia}.`;
      case 'CAMBIAR_ESTADO':
        return `Se cambió el estado de ${entidad}${referencia}.`;
      case 'ELIMINAR':
        return `Se eliminó un registro en ${modulo}: ${entidad}${referencia}.`;
      default:
        return `Movimiento registrado en ${modulo}.`;
    }
  }

  detalleTecnico(item: AuditoriaResponse): string {
    return item.detalle || 'Sin detalle técnico registrado.';
  }

  metodoHttp(item: AuditoriaResponse): string {
    const detalle = item.detalle || '';
    const match = detalle.match(/method=([^;]+)/i);
    return match?.[1]?.trim() || '-';
  }

  endpoint(item: AuditoriaResponse): string {
    const detalle = item.detalle || '';
    const match = detalle.match(/path=([^;]+)/i);
    return match?.[1]?.trim() || '-';
  }

  estadoHttp(item: AuditoriaResponse): string {
    const detalle = item.detalle || '';
    const match = detalle.match(/status=([^;]+)/i);
    return match?.[1]?.trim() || '-';
  }

  usuarioNombre(item: AuditoriaResponse): string {
    return item.nombreUsuario || `Usuario #${item.idUsuario}`;
  }

  fechaSolo(fechaHora: string): string {
    if (!fechaHora) return '';
    return fechaHora.slice(0, 10);
  }

  private normalizarTexto(valor: unknown): string {
    return String(valor || '')
      .trim()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, ' ')
      .toUpperCase();
  }
}