import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  RecepcionLeche as RecepcionLecheModel,
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';

import { FormsModule } from '@angular/forms';

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

  // Métricas para el Panel Superior
  totalLitrosMes = 0;
  proveedorTop = '-';
  promedioLitros = 0;
  maxLitros = 1; // Para la escala visual

  constructor(private service: RecepcionLecheService) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.error = '';

    this.service.listarRecepciones().subscribe({
      next: (data) => {
        this.recepciones = data;
        this.paginaActual = 1;
        this.calcularMetricas(data);
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las recepciones de leche.';
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
    if (!this.filtro.trim()) return this.recepciones;
    const f = this.filtro.toLowerCase();
    return this.recepciones.filter((r: RecepcionLecheModel) => 
      (r.proveedor && r.proveedor.toLowerCase().includes(f)) ||
      (r.idTanque && r.idTanque.toString().includes(f)) ||
      (r.numeroRemision && r.numeroRemision.toLowerCase().includes(f))
    );
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
