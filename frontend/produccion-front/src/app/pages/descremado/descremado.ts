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

  filtroFecha = '';
  filtroProveedor = '';
  filtroTanque = '';
  filtroLote = '';

  paginaActual = 1;
  tamanioPagina = 10;

  constructor(
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
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
      next: (descremados) => {
        this.descremados = descremados || [];

        this.recepcionLecheService.listarRecepciones().subscribe({
          next: (recepciones) => {
            this.recepciones = recepciones || [];

            this.recepcionLecheService.listarSaldosTanques().subscribe({
              next: (tanques) => {
                this.tanques = tanques || [];
                this.cargando = false;
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
    return this.tanques
      .filter(tanque => tanque.activo)
      .reduce((total, tanque) => total + Number(tanque.saldoLitros || 0), 0);
  }

  lecheDescremadaHoy(): number {
    const hoy = this.fechaHoyLocal();

    return this.descremados
      .filter(descremado => this.esMismaFechaDescremado(descremado, hoy))
      .reduce((total, descremado) => total + Number(descremado.litrosDescremados || 0), 0);
  }

  cremaObtenidaHoy(): number {
    const hoy = this.fechaHoyLocal();

    return this.descremados
      .filter(descremado => this.esMismaFechaDescremado(descremado, hoy))
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
        const recepcion = this.buscarRecepcion(item.idRecepcionLeche ?? null);

        const coincideFecha = !this.filtroFecha
          || this.esMismaFechaDescremado(item, this.filtroFecha)
          || this.esMismaFecha(recepcion?.fechaRecepcion, this.filtroFecha);

        const coincideProveedor = !this.filtroProveedor
          || recepcion?.proveedor === this.filtroProveedor;

        const coincideTanque = !this.filtroTanque
          || String(item.idTanqueOrigen || '') === String(this.filtroTanque)
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

  obtenerNumeroFila(index: number): number {
    return ((this.paginaActual - 1) * this.tamanioPagina) + index + 1;
  }

  obtenerProcedencia(item: DescremadoRecepcion): string {
    const fecha = this.normalizarFecha(item.fechaDescremado || item.createdAt);
    const recepcion = this.buscarRecepcion(item.idRecepcionLeche ?? null);

    if (recepcion) {
      const proveedor = recepcion.proveedor || 'Sin proveedor';
      const remision = recepcion.numeroRemision ? ` - Rem. ${recepcion.numeroRemision}` : '';
      return `${fecha} - ${proveedor}${remision}`;
    }

    return `${fecha || 'Sin fecha'} - Descremado por tanque`;
  }

  obtenerTanque(idTanque?: number | null): string {
    if (!idTanque) {
      return 'Sin tanque';
    }

    const tanque = this.tanques.find(item => Number(item.idTanque) === Number(idTanque));

    return tanque?.nombre || `Tanque #${idTanque}`;
  }

  obtenerSaldoTanque(idTanque?: number | null): number {
    if (!idTanque) {
      return 0;
    }

    const tanque = this.tanques.find(item => Number(item.idTanque) === Number(idTanque));
    return Number(tanque?.saldoLitros || 0);
  }

  estadoRegistro(item: DescremadoRecepcion): string {
    if (item.idRecepcionLeche) {
      return 'Histórico';
    }

    return 'Por tanque';
  }

  claseEstado(item: DescremadoRecepcion): string {
    if (item.idRecepcionLeche) {
      return 'bg-slate-50 text-slate-600 border-slate-100';
    }

    return 'bg-emerald-50 text-emerald-700 border-emerald-100';
  }

  private esMismaFechaDescremado(descremado: DescremadoRecepcion, fechaComparar: string): boolean {
    if (descremado.fechaDescremado) {
      return this.normalizarFecha(descremado.fechaDescremado) === fechaComparar;
    }

    const fechaCreacion = this.normalizarFecha(descremado.createdAt);

    if (fechaCreacion) {
      return fechaCreacion === fechaComparar;
    }

    const recepcion = this.buscarRecepcion(descremado.idRecepcionLeche ?? null);

    return this.normalizarFecha(recepcion?.fechaRecepcion) === fechaComparar;
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
    const tiempoFechaDescremado = new Date(descremado.fechaDescremado || '').getTime();

    if (!Number.isNaN(tiempoFechaDescremado)) {
      return tiempoFechaDescremado;
    }

    const tiempoCreacion = new Date(descremado.createdAt || '').getTime();

    if (!Number.isNaN(tiempoCreacion)) {
      return tiempoCreacion;
    }

    const recepcion = this.buscarRecepcion(descremado.idRecepcionLeche ?? null);
    const tiempoRecepcion = new Date(recepcion?.fechaRecepcion || '').getTime();

    if (!Number.isNaN(tiempoRecepcion)) {
      return tiempoRecepcion;
    }

    return 0;
  }

  private buscarRecepcion(idRecepcionLeche?: number | null): RecepcionLeche | undefined {
    if (!idRecepcionLeche) {
      return undefined;
    }

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