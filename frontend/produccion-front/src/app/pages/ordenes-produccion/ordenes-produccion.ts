import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

import {
  OrdenProduccionResponse,
  OrdenProduccionService
} from '../../core/services/orden-produccion';

import { AuthService } from '../../core/services/auth';
import { NotificationService } from '../../core/services/notification';

@Component({
  selector: 'app-ordenes-produccion',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './ordenes-produccion.html',
})
export class OrdenesProduccion implements OnInit {

  ordenes: OrdenProduccionResponse[] = [];

  cargando = false;
  error = '';

  constructor(
    private ordenService: OrdenProduccionService,
    public authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.cargando = true;
    this.error = '';

    this.ordenService.listar().subscribe({
      next: (data) => {
        this.ordenes = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar las órdenes de producción.';
        this.notification.error('No se pudieron cargar las órdenes de producción.');
        this.cargando = false;
      }
    });
  }

  async finalizarOrden(orden: OrdenProduccionResponse): Promise<void> {
    if (!this.authService.canWriteOperaciones()) {
      return;
    }

    const confirmado = await this.notification.confirm({
      title: 'Finalizar orden',
      text: `¿Desea finalizar la orden ${orden.numeroOrden}?`,
      confirmText: 'Sí, finalizar',
      cancelText: 'Cancelar',
      icon: 'question',
      confirmButtonColor: '#2563eb'
    });

    if (!confirmado) {
      return;
    }

    this.cargando = true;
    this.error = '';

    this.ordenService.finalizar(orden.id).subscribe({
      next: () => {
        this.notification.toast('Orden finalizada correctamente.');
        this.cargarOrdenes();
      },
      error: (err) => {
        console.error(err);
        const mensaje = err.error?.message || 'No se pudo finalizar la orden de producción.';
        this.error = mensaje;
        this.notification.error(mensaje);
        this.cargando = false;
      }
    });
  }

  obtenerClaseEstado(estado: string): string {
    switch (estado) {
      case 'PROGRAMADA':
        return 'bg-secondary';

      case 'EN_EJECUCION':
        return 'bg-primary';

      case 'FINALIZADA':
        return 'bg-success';

      case 'CERRADA':
        return 'bg-dark';

      case 'CANCELADA':
        return 'bg-danger';

      default:
        return 'bg-dark';
    }
  }

  // Paginación y Filtros
  filtro = '';
  paginaActual = 1;
  itemsPorPagina = 10;

  get ordenesFiltradas(): OrdenProduccionResponse[] {
    const termino = this.filtro.toLowerCase().trim();

    if (!termino) {
      return this.ordenes;
    }

    return this.ordenes.filter(o =>
      o.numeroOrden.toLowerCase().includes(termino) ||
      (o.nombreProducto && o.nombreProducto.toLowerCase().includes(termino)) ||
      o.estado.toLowerCase().includes(termino)
    );
  }

  get ordenesPaginadas(): OrdenProduccionResponse[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.ordenesFiltradas.slice(inicio, inicio + this.itemsPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.ordenesFiltradas.length / this.itemsPorPagina);
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  cambiarPagina(p: number): void {
    if (p >= 1 && p <= this.totalPaginas) {
      this.paginaActual = p;
    }
  }
}