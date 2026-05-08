import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RecepcionLecheService } from '../../core/services/recepcion-leche';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-recepcion-leche-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './recepcion-leche-form.html',
  styleUrl: './recepcion-leche-form.scss',
})
export class RecepcionLecheForm {

  cargando = false;
  error = '';

  form;

  constructor(
    private fb: FormBuilder,
    private service: RecepcionLecheService,
    private router: Router
  ) {
    this.form = this.fb.group({
      fechaRecepcion: ['', Validators.required],
      tipoMateriaPrima: ['LECHE CRUDA'],
      proveedor: ['', Validators.required],
      cantidadRecibidaLitros: [0, [Validators.required, Validators.min(0.001)]],
      recibidoPor: [''],
      idTanque: [1, Validators.required],
      idUsuario: [1, Validators.required],
      numeroRemision: [''],
      cantidadRemisionLitros: [0, [Validators.required, Validators.min(0.001)]],
      observaciones: ['']
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
      recibidoPor: value.recibidoPor || '',
      idTanque: Number(value.idTanque),
      idUsuario: Number(value.idUsuario),
      numeroRemision: value.numeroRemision || '',
      cantidadRemisionLitros: Number(value.cantidadRemisionLitros),
      observaciones: value.observaciones || '',
      pesajes: []
    };

    this.service.registrarRecepcion(request).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/recepcion-leche']);
      },
      error: () => {
        this.cargando = false;
        this.error = 'No se pudo registrar la recepción de leche.';
      }
    });
  }
}