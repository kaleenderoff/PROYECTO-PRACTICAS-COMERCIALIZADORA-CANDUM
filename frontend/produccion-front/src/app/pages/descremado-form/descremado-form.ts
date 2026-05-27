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
import { NotificationService } from '../../core/services/notification';

@Component({
  selector: 'app-descremado-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './descremado-form.html',
})
export class DescremadoForm implements OnInit {

  private readonly kgCremaEsperadoPorLitro = 131 / 3000;

  cargando = false;
  cargandoDatos = false;
  error = '';

  recepciones: RecepcionLeche[] = [];
  skusCrema: SkuCatalogo[] = [];

  tanqueRefrigeracion: SaldoTanqueLeche | null = null;
  private lotesCremaRegistrados = new Set<string>();
  private descremadosRegistrados: Array<{ idRecepcionLeche: number; litrosDescremados: number }> = [];

  form;

  constructor(
    private fb: FormBuilder,
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
    private router: Router,
    private notification: NotificationService
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

    this.form.get('litrosDescremados')?.valueChanges.subscribe(() => {
      this.aplicarCremaEstimada(false);
    });

    this.form.get('idRecepcionLeche')?.valueChanges.subscribe(() => {
      const disponible = this.litrosDisponiblesRecepcion();
      this.form.get('litrosDescremados')?.setValue(Number(disponible.toFixed(3)));
    });

    this.form.get('registraCremaEmpacada')?.valueChanges.subscribe((registra) => {
      const idSkuCrema = this.form.get('idSkuCrema');
      const unidadesCrema = this.form.get('unidadesCrema');
      const kgPorUnidadCrema = this.form.get('kgPorUnidadCrema');
      const loteCrema = this.form.get('loteCrema');

      if (registra) {
        idSkuCrema?.setValidators([Validators.required]);
        unidadesCrema?.setValidators([Validators.required, Validators.min(1)]);
        kgPorUnidadCrema?.setValidators([Validators.required, Validators.min(0.001)]);
        loteCrema?.setValidators([Validators.required]);
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
            this.descremadosRegistrados = descremados.map(descremado => ({
              idRecepcionLeche: descremado.idRecepcionLeche,
              litrosDescremados: Number(descremado.litrosDescremados || 0)
            }));

            this.lotesCremaRegistrados = new Set(
              descremados
                .map(descremado => descremado.loteCrema?.trim().toUpperCase())
                .filter((lote): lote is string => Boolean(lote))
            );

            this.recepciones = recepciones.filter(recepcion =>
              this.litrosDisponiblesPara(recepcion) > 0
            ).sort((a, b) => this.fechaOrdenable(b).localeCompare(this.fechaOrdenable(a)));

            this.cargandoDatos = false;
          },
          error: (err) => {
            console.error(err);
            this.error = 'No se pudieron cargar los descremados existentes.';
            this.notification.error(this.error);
            this.cargandoDatos = false;
          }
        });
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar las recepciones.';
        this.notification.error(this.error);
        this.cargandoDatos = false;
      }
    });

    this.recepcionLecheService.listarSaldosTanques().subscribe({
      next: (data) => {
        const tanque = data.find(t =>
          t.activo &&
          (
            t.nombre.toUpperCase().includes('REFRIGERACION')
            || t.nombre.toUpperCase().includes('REFRIGERACIÓN')
          )
        );

        if (!tanque) {
          this.error = 'No se encontro el tanque de refrigeracion.';
          this.notification.error(this.error);
          return;
        }

        this.tanqueRefrigeracion = tanque;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cargar el tanque de refrigeracion.';
        this.notification.error(this.error);
      }
    });

    this.descremadoService.listarSkus().subscribe({
      next: (data) => {
        this.skusCrema = data.filter(sku =>
          sku.activo &&
          (
            sku.descripcion.toUpperCase().includes('CREMA')
            || sku.nombreProducto.toUpperCase().includes('CREMA')
          )
        );
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los SKUs de crema.';
        this.notification.error(this.error);
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.notification.warning('Revise los campos obligatorios antes de guardar.');
      return;
    }

    if (!this.tanqueRefrigeracion) {
      this.error = 'No existe tanque de refrigeracion configurado.';
      this.notification.error(this.error);
      return;
    }

    this.cargando = true;
    this.error = '';

    const value = this.form.getRawValue();
    const registraCrema = Boolean(value.registraCremaEmpacada);
    const loteCrema = typeof value.loteCrema === 'string' ? value.loteCrema.trim() : '';

    if (registraCrema && this.lotesCremaRegistrados.has(loteCrema.toUpperCase())) {
      this.notification.warning('El lote de crema ya existe. Use un lote diferente.');
      this.cargando = false;
      return;
    }

    const litrosDescremados = Number(value.litrosDescremados);
    const litrosDisponibles = this.litrosDisponiblesRecepcion();

    if (litrosDescremados > litrosDisponibles) {
      this.notification.warning(`Solo hay ${litrosDisponibles.toFixed(2)} L disponibles para esta recepcion.`);
      this.cargando = false;
      return;
    }

    const request = {
      idRecepcionLeche: Number(value.idRecepcionLeche),
      idTanqueDestino: this.tanqueRefrigeracion.idTanque,
      litrosDescremados,
      cremaObtenidaKg: Number(value.cremaObtenidaKg) || undefined,
      idSkuCrema: registraCrema ? Number(value.idSkuCrema) : undefined,
      unidadesCrema: registraCrema ? Number(value.unidadesCrema) : undefined,
      kgPorUnidadCrema: registraCrema ? Number(value.kgPorUnidadCrema) : undefined,
      loteCrema: registraCrema ? (loteCrema || undefined) : undefined,
      observaciones: value.observaciones || undefined
    };

    this.descremadoService.registrar(request).subscribe({
      next: () => {
        this.cargando = false;
        this.notification.success('Descremado registrado correctamente.');
        this.router.navigate(['/descremado']);
      },
      error: (err) => {
        console.error(err);
        this.cargando = false;
        this.error = err.error?.message || 'No se pudo registrar el descremado.';
        this.notification.error(this.error);
      }
    });
  }

  recepcionSeleccionada(): RecepcionLeche | undefined {
    const id = Number(this.form.get('idRecepcionLeche')?.value || 0);
    return this.recepciones.find(recepcion => recepcion.id === id);
  }

  litrosDisponiblesRecepcion(): number {
    const recepcion = this.recepcionSeleccionada();
    return recepcion ? this.litrosDisponiblesPara(recepcion) : 0;
  }

  litrosDisponiblesParaVista(recepcion: RecepcionLeche): number {
    return this.litrosDisponiblesPara(recepcion);
  }

  cremaEstimadaKg(): number {
    const litros = Number(this.form.get('litrosDescremados')?.value || 0);
    return litros * this.kgCremaEsperadoPorLitro;
  }

  aplicarCremaEstimada(emitEvent = true): void {
    this.form.get('cremaObtenidaKg')?.setValue(Number(this.cremaEstimadaKg().toFixed(2)), { emitEvent });
  }

  cremaEmpacadaKg(): number {
    const unidades = Number(this.form.get('unidadesCrema')?.value || 0);
    const kgUnidad = Number(this.form.get('kgPorUnidadCrema')?.value || 0);
    return unidades * kgUnidad;
  }

  private fechaOrdenable(recepcion: RecepcionLeche): string {
    return `${recepcion.fechaRecepcion || ''}-${String(recepcion.id).padStart(8, '0')}`;
  }

  private litrosDisponiblesPara(recepcion: RecepcionLeche): number {
    const recibido = Number(recepcion.cantidadRecibidaLitros || 0);
    const descremado = this.descremadosRegistrados
      .filter(item => item.idRecepcionLeche === recepcion.id)
      .reduce((total, item) => total + item.litrosDescremados, 0);

    return Math.max(recibido - descremado, 0);
  }
}
