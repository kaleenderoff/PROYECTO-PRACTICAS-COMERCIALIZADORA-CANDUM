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

@Component({
  selector: 'app-descremado',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './descremado.html',
  
})
export class Descremado implements OnInit {

  descremados: DescremadoRecepcion[] = [];
  recepciones: RecepcionLeche[] = [];
  tanques: SaldoTanqueLeche[] = [];

  cargando = false;
  error = '';
  filtroFecha = '';
  filtroProveedor = '';
  filtroTanque = '';
  filtroLote = '';

  constructor(
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.error = '';

    this.recepcionLecheService.listarRecepciones().subscribe({
      next: (recepciones) => {
        this.recepciones = recepciones;

        this.recepcionLecheService.listarSaldosTanques().subscribe({
          next: (tanques) => {
            this.tanques = tanques;

            this.descremadoService.listar().subscribe({
              next: (data) => {
                this.descremados = data.slice().sort((a, b) =>
                  this.fechaOrdenable(b).localeCompare(this.fechaOrdenable(a))
                );
                this.cargando = false;
              },
              error: (err) => {
                console.error(err);
                this.error = 'No se pudieron cargar los descremados.';
                this.cargando = false;
              }
            });
          }
        });
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los datos.';
        this.cargando = false;
      }
    });
  }

  obtenerRecepcion(idRecepcion: number): string {
    const recepcion = this.recepciones.find(r => r.id === idRecepcion);

    if (!recepcion) {
      return 'Recepción no encontrada';
    }

    return `${recepcion.fechaRecepcion} - ${recepcion.proveedor}`;
  }

  obtenerRecepcionEntidad(idRecepcion: number): RecepcionLeche | undefined {
    return this.recepciones.find(r => r.id === idRecepcion);
  }

  obtenerTanque(idTanque?: number): string {
    if (!idTanque) {
      return '-';
    }

    const tanque = this.tanques.find(t => t.idTanque === idTanque);

    return tanque?.nombre || `Tanque ${idTanque}`;
  }

  get descremadosFiltrados(): DescremadoRecepcion[] {
    const proveedor = this.filtroProveedor.trim().toLowerCase();
    const lote = this.filtroLote.trim().toLowerCase();
    const idTanque = Number(this.filtroTanque || 0);

    return this.descremados.filter(item => {
      const recepcion = this.obtenerRecepcionEntidad(item.idRecepcionLeche);
      const cumpleFecha = !this.filtroFecha || recepcion?.fechaRecepcion === this.filtroFecha;
      const cumpleProveedor = !proveedor || (recepcion?.proveedor || '').toLowerCase().includes(proveedor);
      const cumpleTanque = !idTanque || item.idTanqueDestino === idTanque;
      const cumpleLote = !lote || (item.loteCrema || '').toLowerCase().includes(lote);
      return cumpleFecha && cumpleProveedor && cumpleTanque && cumpleLote;
    });
  }

  limpiarFiltros(): void {
    this.filtroFecha = '';
    this.filtroProveedor = '';
    this.filtroTanque = '';
    this.filtroLote = '';
    this.paginaActual = 1;
  }

  fechaHoy(): string {
    return new Date().toISOString().slice(0, 10);
  }

  lecheRecibidaHoy(): number {
    const hoy = this.fechaHoy();
    return this.recepciones
      .filter(recepcion => recepcion.fechaRecepcion === hoy)
      .reduce((total, recepcion) => total + Number(recepcion.cantidadRecibidaLitros || 0), 0);
  }

  lecheDisponibleParaDescremar(): number {
    return this.recepciones.reduce((total, recepcion) => total + this.litrosRestantesRecepcion(recepcion.id), 0);
  }

  lecheDescremadaHoy(): number {
    const hoy = this.fechaHoy();
    return this.descremados
      .filter(item => this.obtenerRecepcionEntidad(item.idRecepcionLeche)?.fechaRecepcion === hoy)
      .reduce((total, item) => total + Number(item.litrosDescremados || 0), 0);
  }

  cremaObtenidaHoy(): number {
    const hoy = this.fechaHoy();
    return this.descremados
      .filter(item => this.obtenerRecepcionEntidad(item.idRecepcionLeche)?.fechaRecepcion === hoy)
      .reduce((total, item) => total + Number(item.cremaObtenidaKg || 0), 0);
  }

  proveedoresDisponibles(): string[] {
    return Array.from(new Set(this.recepciones.map(r => r.proveedor).filter(Boolean))).sort();
  }

  litrosRecibidosRecepcion(idRecepcion: number): number {
    return Number(this.obtenerRecepcionEntidad(idRecepcion)?.cantidadRecibidaLitros || 0);
  }

  litrosDescremadosRecepcion(idRecepcion: number): number {
    return this.descremados
      .filter(item => item.idRecepcionLeche === idRecepcion)
      .reduce((total, item) => total + Number(item.litrosDescremados || 0), 0);
  }

  litrosRestantesRecepcion(idRecepcion: number): number {
    return Math.max(this.litrosRecibidosRecepcion(idRecepcion) - this.litrosDescremadosRecepcion(idRecepcion), 0);
  }

  estadoRecepcion(idRecepcion: number): 'Pendiente' | 'Parcial' | 'Completa' {
    const recibido = this.litrosRecibidosRecepcion(idRecepcion);
    const descremado = this.litrosDescremadosRecepcion(idRecepcion);

    if (recibido <= 0 || descremado <= 0) {
      return 'Pendiente';
    }

    return descremado >= recibido ? 'Completa' : 'Parcial';
  }

  claseEstado(idRecepcion: number): string {
    const estado = this.estadoRecepcion(idRecepcion);
    if (estado === 'Completa') return 'bg-emerald-50 text-emerald-700 border-emerald-100';
    if (estado === 'Parcial') return 'bg-amber-50 text-amber-700 border-amber-100';
    return 'bg-slate-50 text-slate-600 border-slate-100';
  }

  // Paginación
  paginaActual = 1;
  itemsPorPagina = 10;

  get descremadosPaginados(): DescremadoRecepcion[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.descremadosFiltrados.slice(inicio, inicio + this.itemsPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.descremadosFiltrados.length / this.itemsPorPagina);
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  cambiarPagina(p: number): void {
    if (p >= 1 && p <= this.totalPaginas) {
      this.paginaActual = p;
    }
  }

  private fechaOrdenable(descremado: DescremadoRecepcion): string {
    return `${descremado.createdAt || ''}-${String(descremado.id).padStart(8, '0')}`;
  }
}
