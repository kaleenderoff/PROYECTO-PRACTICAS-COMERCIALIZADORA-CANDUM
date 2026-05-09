import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  Insumo,
  OrdenProduccionService,
  SkuCatalogo
} from '../../core/services/orden-produccion';

@Component({
  selector: 'app-orden-produccion-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './orden-produccion-form.html',
  styleUrl: './orden-produccion-form.scss',
})
export class OrdenProduccionForm implements OnInit {
  cargandoDatos = false;
  error = '';

  insumos: Insumo[] = [];
  skus: SkuCatalogo[] = [];

  productos = [
    'DULCE DE LECHE',
    'LECHE CONDENSADA'
  ];

  form;

  constructor(
    private fb: FormBuilder,
    private ordenService: OrdenProduccionService
  ) {
    this.form = this.fb.group({
      fechaProduccion: ['', Validators.required],
      fechaVencimiento: [''],
      producto: ['LECHE CONDENSADA', Validators.required],
      responsable: [''],
      turno: ['MAÑANA'],
      tamanoBatchKg: [0, [Validators.required, Validators.min(0.001)]],
      numeroBatchManual: [0],
      porcentajeReduccion: [53],
      rendimientoEsperado: [47],
      observaciones: [''],
      presentaciones: this.fb.array([]),
      ingredientes: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.cargarDatosIniciales();
  }

  get ingredientes(): FormArray {
    return this.form.get('ingredientes') as FormArray;
  }

  get presentaciones(): FormArray {
    return this.form.get('presentaciones') as FormArray;
  }

  cargarDatosIniciales(): void {
    this.cargandoDatos = true;
    this.error = '';

    this.ordenService.listarInsumos().subscribe({
      next: (data) => {
        this.insumos = data.filter(insumo => insumo.activo);
        this.cargarIngredientesLecheCondensada();
        this.cargandoDatos = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los insumos.';
        this.cargandoDatos = false;
      }
    });

    this.ordenService.listarSkus().subscribe({
      next: (data) => {
        this.skus = data.filter(sku =>
          sku.activo &&
          sku.nombreProducto.toUpperCase().includes('LECHE CONDENSADA')
        );

        this.agregarPresentacion();
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los SKUs.';
      }
    });
  }

  cargarIngredientesLecheCondensada(): void {
    this.ingredientes.clear();

    const nombres = [
      'LECHE LIQUIDA',
      'AZUCAR',
      'ALMIDON ARGO',
      'BICARBONATO DE SODIO',
      'LACTASA',
      'CARRAGENINA',
      'ANTIESPUMANTE',
      'NATAMICINA',
      'SORBATO',
      'BENZOATO',
      'CITRATO DE SODIO'
    ];

    nombres.forEach(nombre => {
      const insumo = this.insumos.find(i =>
        i.nombre.toUpperCase() === nombre
      );

      if (insumo) {
        this.agregarIngrediente(insumo);
      }
    });
  }

  agregarIngrediente(insumo: Insumo): void {
    this.ingredientes.push(
      this.fb.group({
        idInsumo: [insumo.id],
        nombreInsumo: [insumo.nombre],
        porcentajeFormula: [0],
        cantidadSugeridaKg: [0],
        cantidadAjustadaKg: [0],
        cantidadGramos: [0]
      })
    );
  }

  agregarPresentacion(): void {
    this.presentaciones.push(
      this.fb.group({
        idSku: [null],
        pesoGramos: [0],
        unidadesPlaneadas: [0],
        kilosProductoTerminado: [0],
        kilosBatch: [0],
        numeroBatchesCalculado: [0]
      })
    );
  }

  seleccionarSku(index: number): void {
    const control = this.presentaciones.at(index);
    const idSku = Number(control.get('idSku')?.value);

    const sku = this.skus.find(item => item.id === idSku);

    if (!sku) {
      control.get('pesoGramos')?.setValue(0);
      return;
    }

    control.get('pesoGramos')?.setValue(sku.pesoNetoGr);
    this.calcular();
  }

  calcular(): void {
    const tamanoBatchKg =
      Number(this.form.get('tamanoBatchKg')?.value) || 0;

    const rendimiento =
      Number(this.form.get('rendimientoEsperado')?.value) || 1;

    this.presentaciones.controls.forEach((control) => {
      const pesoGramos =
        Number(control.get('pesoGramos')?.value) || 0;

      const unidades =
        Number(control.get('unidadesPlaneadas')?.value) || 0;

      const kilosProductoTerminado =
        (pesoGramos * unidades) / 1000;

      const kilosBatch =
        kilosProductoTerminado / (rendimiento / 100);

      const numeroBatches =
        tamanoBatchKg > 0 ? kilosBatch / tamanoBatchKg : 0;

      control.get('kilosProductoTerminado')
        ?.setValue(Number(kilosProductoTerminado.toFixed(3)));

      control.get('kilosBatch')
        ?.setValue(Number(kilosBatch.toFixed(3)));

      control.get('numeroBatchesCalculado')
        ?.setValue(Number(numeroBatches.toFixed(3)));
    });

    this.ingredientes.controls.forEach((control) => {
      const porcentaje =
        Number(control.get('porcentajeFormula')?.value) || 0;

      const cantidadKg =
        tamanoBatchKg * (porcentaje / 100);

      control.get('cantidadSugeridaKg')
        ?.setValue(Number(cantidadKg.toFixed(3)));

      control.get('cantidadGramos')
        ?.setValue(Number((cantidadKg * 1000).toFixed(3)));

      if (!control.get('cantidadAjustadaKg')?.value) {
        control.get('cantidadAjustadaKg')
          ?.setValue(Number(cantidadKg.toFixed(3)));
      }
    });
  }
}