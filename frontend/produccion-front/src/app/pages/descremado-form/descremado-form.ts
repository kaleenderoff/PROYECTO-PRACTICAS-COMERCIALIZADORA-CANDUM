import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import {
  DescremadoService,
  SkuCatalogo
} from '../../core/services/descremado';

import {
  RecepcionLeche,
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';

@Component({
  selector: 'app-descremado-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './descremado-form.html',
  
})
export class DescremadoForm implements OnInit {

  cargando = false;
  cargandoDatos = false;
  error = '';

  recepciones: RecepcionLeche[] = [];
  skusCrema: SkuCatalogo[] = [];

  tanqueRefrigeracion: SaldoTanqueLeche | null = null;

  form;

  constructor(
    private fb: FormBuilder,
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
    private router: Router
  ) {

    this.form = this.fb.group({
      idRecepcionLeche: [null, Validators.required],
      litrosDescremados: [0, [Validators.required, Validators.min(0.001)]],
      cremaObtenidaKg: [0],

      registraCremaEmpacada: [false],

      idSkuCrema: [null],
      unidadesCrema: [0],
      kgPorUnidadCrema: [18],
      loteCrema: [''],

      observaciones: ['']
    });
  }

  ngOnInit(): void {

    this.cargarDatosIniciales();

    this.form.get('registraCremaEmpacada')?.valueChanges.subscribe((registra) => {

      const idSkuCrema = this.form.get('idSkuCrema');
      const unidadesCrema = this.form.get('unidadesCrema');
      const kgPorUnidadCrema = this.form.get('kgPorUnidadCrema');
      const loteCrema = this.form.get('loteCrema');

      if (registra) {

        idSkuCrema?.setValidators([Validators.required]);

        unidadesCrema?.setValidators([
          Validators.required,
          Validators.min(1)
        ]);

        kgPorUnidadCrema?.setValidators([
          Validators.required,
          Validators.min(0.001)
        ]);

        loteCrema?.setValidators([
          Validators.required
        ]);

      } else {

        idSkuCrema?.clearValidators();
        unidadesCrema?.clearValidators();
        kgPorUnidadCrema?.clearValidators();
        loteCrema?.clearValidators();

        idSkuCrema?.setValue(null);
        unidadesCrema?.setValue(0);
        kgPorUnidadCrema?.setValue(18);
        loteCrema?.setValue('');
      }

      idSkuCrema?.updateValueAndValidity();
      unidadesCrema?.updateValueAndValidity();
      kgPorUnidadCrema?.updateValueAndValidity();
      loteCrema?.updateValueAndValidity();

    });
  }

  cargarDatosIniciales(): void {

    this.cargandoDatos = true;
    this.error = '';

    this.recepcionLecheService.listarRecepciones().subscribe({

      next: (recepciones) => {

        this.descremadoService.listar().subscribe({

          next: (descremados) => {

            this.recepciones = recepciones.filter(recepcion => {

              const totalDescremado = descremados
                .filter(d => d.idRecepcionLeche === recepcion.id)
                .reduce((total, d) =>
                  total + Number(d.litrosDescremados || 0), 0);

              const disponible =
                Number(recepcion.cantidadRecibidaLitros || 0)
                - totalDescremado;

              return disponible > 0;
            });

            this.cargandoDatos = false;
          },

          error: (err) => {
            console.error(err);
            this.error = 'No se pudieron cargar los descremados existentes.';
            this.cargandoDatos = false;
          }

        });

      },

      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar las recepciones.';
        this.cargandoDatos = false;
      }

    });

    this.recepcionLecheService.listarSaldosTanques().subscribe({

      next: (data) => {

        const tanque = data.find(t =>
          t.activo &&
          (
            t.nombre.toUpperCase().includes('REFRIGERACION')
            ||
            t.nombre.toUpperCase().includes('REFRIGERACIÓN')
          )
        );

        if (!tanque) {
          this.error = 'No se encontró el tanque de refrigeración.';
          return;
        }

        this.tanqueRefrigeracion = tanque;
      },

      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cargar el tanque de refrigeración.';
      }

    });

    this.descremadoService.listarSkus().subscribe({

      next: (data) => {

        this.skusCrema = data.filter(sku =>
          sku.activo &&
          (
            sku.descripcion.toUpperCase().includes('CREMA')
            ||
            sku.nombreProducto.toUpperCase().includes('CREMA')
          )
        );

      },

      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los SKUs de crema.';
      }

    });
  }

  guardar(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (!this.tanqueRefrigeracion) {
      this.error = 'No existe tanque de refrigeración configurado.';
      return;
    }

    this.cargando = true;
    this.error = '';

    const value = this.form.getRawValue();

    const registraCrema = Boolean(value.registraCremaEmpacada);

    const request = {

      idRecepcionLeche: Number(value.idRecepcionLeche),

      idTanqueDestino: this.tanqueRefrigeracion.idTanque,

      litrosDescremados: Number(value.litrosDescremados),

      cremaObtenidaKg:
        Number(value.cremaObtenidaKg) || undefined,

      idSkuCrema:
        registraCrema
          ? Number(value.idSkuCrema)
          : undefined,

      unidadesCrema:
        registraCrema
          ? Number(value.unidadesCrema)
          : undefined,

      kgPorUnidadCrema:
        registraCrema
          ? Number(value.kgPorUnidadCrema)
          : undefined,

      loteCrema:
        registraCrema
          ? (value.loteCrema || undefined)
          : undefined,

      observaciones:
        value.observaciones || undefined
    };

    this.descremadoService.registrar(request).subscribe({

      next: () => {
        this.cargando = false;
        this.router.navigate(['/descremado']);
      },

      error: (err) => {
        console.error(err);
        this.cargando = false;
        this.error =
          err.error?.message
          || 'No se pudo registrar el descremado.';
      }

    });
  }
}
