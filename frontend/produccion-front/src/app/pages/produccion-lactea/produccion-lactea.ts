import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  ProduccionLactea as ProduccionLacteaModel,
  ProduccionLacteaService
} from '../../core/services/produccion-lactea';

@Component({
  selector: 'app-produccion-lactea',
  imports: [CommonModule, RouterLink],
  templateUrl: './produccion-lactea.html',
  styleUrl: './produccion-lactea.scss',
})
export class ProduccionLactea implements OnInit {
  producciones: ProduccionLacteaModel[] = [];

  cargando = false;
  error = '';

  constructor(private service: ProduccionLacteaService) { }

  ngOnInit(): void {
    this.cargarProducciones();
  }

  cargarProducciones(): void {
    this.cargando = true;
    this.error = '';

    this.service.listar().subscribe({
      next: (data) => {
        this.producciones = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar las producciones lácteas.';
        this.cargando = false;
      }
    });
  }
}