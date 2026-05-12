import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

import {
  FormArray,
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { ProduccionLacteaService } from '../../core/services/produccion-lactea';

import {
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';

import {
  OrdenProduccionResponse,
  OrdenProduccionService
} from '../../core/services/orden-produccion';

@Component({
  selector: 'app-ejecucion-produccion-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './ejecucion-produccion-form.html',
  styleUrl: './ejecucion-produccion-form.scss',
})
export class EjecucionProduccionForm implements OnInit {

  idOrdenProduccion: number | null = null;

  orden: OrdenProduccionResponse | null = null;

  cargando = false;
  cargandoDatos = false;
  error = '';

  tanques: SaldoTanqueLeche[] = [];

  productosLacteos = [
    'LECHE CONDENSADA',
    'DULCE DE LECHE',
    'DULCE DE LECHE PANADERIA'
  ];

  marmitas = [
    { id: 1, nombre: 'Marmita 1' },
    { id: 2, nombre: 'Marmita 2' },
    { id: 3, nombre: 'Marmita 3' },
    { id: 4, nombre: 'Marmita 4' },
    { id: 5, nombre: 'Marmita 5' },
    { id: 6, nombre: 'Marmita 6' }
  ];

  form;

  constructor(
    private fb: FormBuilder,
    private produccionService: ProduccionLacteaService,
    private recepcionLecheService: RecepcionLecheService,
    private ordenService: OrdenProduccionService,
    private route: ActivatedRoute,
    private router: Router
  ) {

    this.form = this.fb.group({

      fechaProduccion: ['', Validators.required],

      producto: ['', Validators.required],

      idTanque: [null, Validators.required],

      observaciones: [''],

      batches: this.fb.array([])

    });
  }

  ngOnInit(): void {

    this.idOrdenProduccion =
      Number(this.route.snapshot.paramMap.get('id')) || null;

    this.cargarTanques();

    if (this.idOrdenProduccion) {
      this.cargarOrden(this.idOrdenProduccion);
    }

    this.agregarBatch();
  }

  get batches(): FormArray {
    return this.form.get('batches') as FormArray;
  }

  cargarOrden(id: number): void {

    this.cargandoDatos = true;

    this.ordenService.obtenerPorId(id).subscribe({

      next: (orden) => {

        this.orden = orden;

        this.form.patchValue({
          fechaProduccion: orden.fechaProduccion,
          producto: this.obtenerNombreProductoPorId(orden.idProducto)
        });

        this.form.get('fechaProduccion')?.disable();
        this.form.get('producto')?.disable();

        this.cargandoDatos = false;
      },


      error: (err) => {

        console.error(err);

        this.error =
          'No se pudo cargar la orden de producción.';

        this.cargandoDatos = false;
      }
    });
  }

  obtenerNombreProductoPorId(idProducto: number): string {
    switch (Number(idProducto)) {
      case 1:
        return 'LECHE CONDENSADA';

      case 2:
        return 'DULCE DE LECHE';

      case 3:
        return 'DULCE DE LECHE PANADERIA';

      default:
        return '';
    }
  }

  crearBatch() {

    return this.fb.group({

      numeroBatch: [
        this.batches.length + 1,
        Validators.required
      ],

      idMarmita: [
        null,
        Validators.required
      ],

      litrosConsumidos: [
        0,
        [
          Validators.required,
          Validators.min(0.001)
        ]
      ],

      kilosProducidos: [
        0,
        [
          Validators.required,
          Validators.min(0.001)
        ]
      ],

      observaciones: ['']

    });
  }

  agregarBatch(): void {
    this.batches.push(this.crearBatch());
  }

  eliminarBatch(index: number): void {

    this.batches.removeAt(index);

    this.renumerarBatches();
  }

  renumerarBatches(): void {

    this.batches.controls.forEach((control, index) => {

      control.get('numeroBatch')
        ?.setValue(index + 1);

    });
  }

  cargarTanques(): void {

    this.cargandoDatos = true;

    this.error = '';

    this.recepcionLecheService
      .listarSaldosTanques()
      .subscribe({

        next: (data) => {

          this.tanques =
            data.filter(tanque => tanque.activo);

          this.cargandoDatos = false;
        },

        error: (err) => {

          console.error(err);

          this.error =
            'No se pudieron cargar los tanques de leche.';

          this.cargandoDatos = false;
        }
      });
  }

  guardar(): void {

    if (this.form.invalid || this.batches.length === 0) {

      this.form.markAllAsTouched();

      return;
    }

    this.cargando = true;

    this.error = '';

    const value = this.form.getRawValue();

    const request = {

      idOrdenProduccion: this.idOrdenProduccion || undefined,

      fechaProduccion: value.fechaProduccion!,

      producto: value.producto!,

      idTanque: Number(value.idTanque),

      observaciones:
        value.observaciones || undefined,

      batches: value.batches.map((batch: any) => ({

        numeroBatch:
          Number(batch.numeroBatch),

        idMarmita:
          Number(batch.idMarmita),

        litrosConsumidos:
          Number(batch.litrosConsumidos),

        kilosProducidos:
          Number(batch.kilosProducidos),

        observaciones:
          batch.observaciones || undefined

      }))
    };

    this.produccionService
      .registrar(request)
      .subscribe({

        next: () => {

          this.cargando = false;

          this.router.navigate([
            '/ordenes-produccion'
          ]);
        },

        error: (err) => {

          console.error(err);

          this.cargando = false;

          this.error =
            'No se pudo registrar la ejecución de producción.';
        }
      });
  }
}