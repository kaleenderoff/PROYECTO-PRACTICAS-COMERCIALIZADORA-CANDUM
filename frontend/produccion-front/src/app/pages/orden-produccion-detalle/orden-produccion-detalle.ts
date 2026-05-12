import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute } from '@angular/router';

import {
  OrdenProduccionResponse,
  OrdenProduccionService
} from '../../core/services/orden-produccion';

@Component({
  selector: 'app-orden-produccion-detalle',
  imports: [CommonModule, RouterLink],
  templateUrl: './orden-produccion-detalle.html',
  styleUrl: './orden-produccion-detalle.scss',
})
export class OrdenProduccionDetalle implements OnInit {

  orden: OrdenProduccionResponse | null = null;

  cargando = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private ordenService: OrdenProduccionService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (id) {
      this.cargarOrden(id);
    }
  }

  cargarOrden(id: number): void {
    this.cargando = true;
    this.error = '';

    this.ordenService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.orden = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cargar la orden de producción.';
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

  obtenerNombreProducto(idProducto: number): string {
    switch (Number(idProducto)) {
      case 1:
        return 'Leche condensada';
      case 2:
        return 'Dulce de leche';
      case 3:
        return 'Dulce de leche panadería';
      default:
        return 'Producto no identificado';
    }
  }

  obtenerNombreLinea(idLinea: number): string {
    switch (Number(idLinea)) {
      case 1:
        return 'Línea 1';
      default:
        return 'Línea no identificada';
    }
  }

  obtenerNombreTurno(idTurno: number): string {
    switch (Number(idTurno)) {
      case 1:
        return 'Turno 1';
      case 2:
        return 'Turno 2';
      case 3:
        return 'Turno 3';
      default:
        return 'Turno no identificado';
    }
  }
}