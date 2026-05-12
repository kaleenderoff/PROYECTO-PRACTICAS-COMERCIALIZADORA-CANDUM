import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  OrdenProduccionResponse,
  OrdenProduccionService
} from '../../core/services/orden-produccion';

@Component({
  selector: 'app-ordenes-produccion',
  imports: [CommonModule, RouterLink],
  templateUrl: './ordenes-produccion.html',
  styleUrl: './ordenes-produccion.scss',
})
export class OrdenesProduccion implements OnInit {

  ordenes: OrdenProduccionResponse[] = [];

  cargando = false;
  error = '';

  constructor(
    private ordenService: OrdenProduccionService
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
        this.cargando = false;
      }
    });
  }

  finalizarOrden(orden: OrdenProduccionResponse): void {
    if (!confirm(`¿Desea finalizar la orden ${orden.numeroOrden}?`)) {
      return;
    }

    this.cargando = true;
    this.error = '';

    this.ordenService.finalizar(orden.id).subscribe({
      next: () => {
        this.cargarOrdenes();
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo finalizar la orden de producción.';
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
}