import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { forkJoin, map, switchMap } from 'rxjs';

import {
  OrdenProduccionResponse,
  OrdenProduccionService
} from '../../core/services/orden-produccion';

import { 
  EjecucionBatch, 
  EjecucionBatchService 
} from '../../core/services/ejecucion-batch';

interface EjecucionExtendida extends OrdenProduccionResponse {
  batches: EjecucionBatch[];
}

@Component({
  selector: 'app-ejecucion-produccion',
  imports: [CommonModule],
  templateUrl: './ejecucion-produccion.html',
})
export class EjecucionProduccion implements OnInit {

  producciones: EjecucionExtendida[] = [];
  seleccionada: EjecucionExtendida | null = null;
  cargando = false;
  error = '';

  private ordenService = inject(OrdenProduccionService);
  private batchService = inject(EjecucionBatchService);

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.cargando = true;
    this.error = '';

    this.ordenService.listar().pipe(
      map(ordenes => ordenes.filter(o => 
        o.estado === 'FINALIZADA' || o.estado === 'CERRADA' || o.estado === 'EN_EJECUCION'
      )),
      switchMap(ordenesFiltradas => {
        if (ordenesFiltradas.length === 0) return [[]];
        
        const requests = ordenesFiltradas.map(orden => 
          this.batchService.listarPorOrden(orden.id).pipe(
            map(batches => ({ ...orden, batches } as EjecucionExtendida))
          )
        );
        return forkJoin(requests);
      })
    ).subscribe({
      next: (data) => {
        this.producciones = data.sort((a, b) => 
          new Date(b.fechaProduccion).getTime() - new Date(a.fechaProduccion).getTime()
        );
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando historial de ejecución:', err);
        this.error = 'No se pudo cargar el historial de producción real.';
        this.cargando = false;
      }
    });
  }

  verDetalle(item: EjecucionExtendida): void {
    this.seleccionada = item;
  }

  cerrarDetalle(): void {
    this.seleccionada = null;
  }

  // Paginación
  paginaActual = 1;
  itemsPorPagina = 10;

  get produccionesPaginadas(): EjecucionExtendida[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.producciones.slice(inicio, inicio + this.itemsPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.producciones.length / this.itemsPorPagina);
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
