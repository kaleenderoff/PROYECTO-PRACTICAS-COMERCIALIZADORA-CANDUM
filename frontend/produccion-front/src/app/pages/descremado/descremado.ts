import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
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
  imports: [CommonModule, RouterLink],
  templateUrl: './descremado.html',
  
})
export class Descremado implements OnInit {

  descremados: DescremadoRecepcion[] = [];
  recepciones: RecepcionLeche[] = [];
  tanques: SaldoTanqueLeche[] = [];

  cargando = false;
  error = '';

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

  obtenerTanque(idTanque?: number): string {
    if (!idTanque) {
      return '-';
    }

    const tanque = this.tanques.find(t => t.idTanque === idTanque);

    return tanque?.nombre || `Tanque ${idTanque}`;
  }

  // Paginación
  paginaActual = 1;
  itemsPorPagina = 10;

  get descremadosPaginados(): DescremadoRecepcion[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.descremados.slice(inicio, inicio + this.itemsPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.descremados.length / this.itemsPorPagina);
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
