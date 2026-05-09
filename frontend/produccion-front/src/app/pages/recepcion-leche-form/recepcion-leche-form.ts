import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import {
  RecepcionLecheService,
  Proveedor
} from '../../core/services/recepcion-leche';

@Component({
  selector: 'app-recepcion-leche-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './recepcion-leche-form.html',
  styleUrl: './recepcion-leche-form.scss',
})
export class RecepcionLecheForm implements OnInit {
  cargando = false;
  cargandoDatos = false;
  error = '';

  proveedores: Proveedor[] = [];

  tiposMateriaPrima = [
    'LECHE CRUDA'
  ];

  form;

  constructor(
    private fb: FormBuilder,
    private service: RecepcionLecheService,
    private router: Router
  ) {
    this.form = this.fb.group({
      fechaRecepcion: ['', Validators.required],
      tipoMateriaPrima: ['LECHE CRUDA', Validators.required],
      proveedor: ['', Validators.required],
      cantidadRecibidaLitros: [0, [Validators.required, Validators.min(0.001)]],
      recibidoPor: [''],
      numeroRemision: [''],
      cantidadRemisionLitros: [0],
      observaciones: ['']
    });
  }

  ngOnInit(): void {
    this.cargarDatosIniciales();
  }

  cargarDatosIniciales(): void {
    this.cargandoDatos = true;
    this.error = '';

    this.service.listarProveedores().subscribe({
      next: (data) => {
        this.proveedores = data.filter(proveedor => proveedor.activo);
        this.cargandoDatos = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los proveedores.';
        this.cargandoDatos = false;
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.error = '';

    const value = this.form.getRawValue();

    const request = {
      fechaRecepcion: value.fechaRecepcion!,
      tipoMateriaPrima: value.tipoMateriaPrima!,
      proveedor: value.proveedor!,
      cantidadRecibidaLitros: Number(value.cantidadRecibidaLitros),
      recibidoPor: value.recibidoPor || undefined,
      idUsuario: 1,
      numeroRemision: value.numeroRemision || undefined,
      cantidadRemisionLitros: Number(value.cantidadRemisionLitros) || undefined,
      observaciones: value.observaciones || undefined,
      pesajes: []
    };

    this.service.registrarRecepcion(request).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/recepcion-leche']);
      },
      error: (err) => {
        console.error(err);
        this.cargando = false;
        this.error = 'No se pudo registrar la recepción de leche.';
      }
    });
  }
}