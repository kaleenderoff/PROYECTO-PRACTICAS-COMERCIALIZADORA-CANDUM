import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  RecepcionLeche as RecepcionLecheModel,
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';

@Component({
  selector: 'app-recepcion-leche',
  imports: [CommonModule, RouterLink],
  templateUrl: './recepcion-leche.html',
  styleUrl: './recepcion-leche.scss',
})
export class RecepcionLeche implements OnInit {

  recepciones: RecepcionLecheModel[] = [];
  saldos: SaldoTanqueLeche[] = [];

  cargando = false;
  error = '';

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
}