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
import {
  ControlCalidadLacteaService,
  EstadoCalidadRecepcion
} from '../../core/services/control-calidad-lactea';

@Component({
  selector: 'app-descremado-form',
  standalone: true,
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
  estadosCalidad: EstadoCalidadRecepcion[] = [];

  tanqueRefrigeracion: SaldoTanqueLeche | null = null;

  private lotesCremaRegistrados = new Set<string>();
  private descremadosRegistrados: Array<{
    idRecepcionLeche: number;
    litrosDescremados: number;
  }> = [];

  form;

  constructor(
    private fb: FormBuilder,
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
    private controlCalidadService: ControlCalidadLacteaService,
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
        this.controlCalidadService.listarEstadosRecepcion().subscribe({
          next: (estados) => {
            this.estadosCalidad = estados || [];

            this.descremadoService.listar().subscribe({
              next: (descremados) => {
                this.descremadosRegistrados = (descremados || []).map(descremado => ({
                  idRecepcionLeche: Number(descremado.idRecepcionLeche),
                  litrosDescremados: Number(descremado.litrosDescremados || 0)
                }));

                this.lotesCremaRegistrados = new Set(
                  (descremados || [])
                    .map(descremado => descremado.loteCrema?.trim().toUpperCase())
                    .filter((lote): lote is string => Boolean(lote))
                );

                this.recepciones = (recepciones || [])
                  .map(recepcion => ({
                    ...recepcion,
                    estadoCalidad: this.estadoCalidadRecepcion(recepcion.id)
                  }))
                  .sort((a, b) => this.compararRecepcionesRecientes(a, b));

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
            this.error = 'No se pudieron cargar los estados de calidad.';
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
        const tanque = (data || []).find(t =>
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
        this.skusCrema = (data || []).filter(sku =>
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

  get recepcionesPendientesDescremar(): RecepcionLeche[] {
    return this.recepciones
      .filter(recepcion => this.litrosDisponiblesParaVista(recepcion) > 0)
      .sort((a, b) => this.compararRecepcionesRecientes(a, b));
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
    const estadoCalidad = this.estadoCalidadSeleccionada();

    if (estadoCalidad === 'RETENIDA' || estadoCalidad === 'NO_APROBADA') {
      this.notification.warning('No se puede descremar una recepcion retenida o no aprobada por calidad.');
      this.cargando = false;
      return;
    }

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
    return this.recepciones.find(recepcion => Number(recepcion.id) === id);
  }

  litrosDisponiblesRecepcion(): number {
    const recepcion = this.recepcionSeleccionada();
    return recepcion ? this.litrosDisponiblesPara(recepcion) : 0;
  }

  litrosDisponiblesParaVista(recepcion: RecepcionLeche): number {
    return this.litrosDisponiblesPara(recepcion);
  }

  litrosRecibidosSeleccionada(): number {
    return Number(this.recepcionSeleccionada()?.cantidadRecibidaLitros || 0);
  }

  litrosYaDescremadosSeleccionada(): number {
    const recepcion = this.recepcionSeleccionada();

    if (!recepcion) {
      return 0;
    }

    return this.descremadosRegistrados
      .filter(item => Number(item.idRecepcionLeche) === Number(recepcion.id))
      .reduce((total, item) => total + Number(item.litrosDescremados || 0), 0);
  }

  litrosDespuesDescremado(): number {
    const disponibles = this.litrosDisponiblesRecepcion();
    const litros = Number(this.form.get('litrosDescremados')?.value || 0);

    return Math.max(disponibles - litros, 0);
  }

  rendimientoCremaPor100Litros(): number {
    const litros = Number(this.form.get('litrosDescremados')?.value || 0);
    const crema = Number(this.form.get('cremaObtenidaKg')?.value || 0);

    if (litros <= 0 || crema <= 0) {
      return 0;
    }

    return (crema / litros) * 100;
  }

  estadoCalidadSeleccionada(): RecepcionLeche['estadoCalidad'] {
    const recepcion = this.recepcionSeleccionada();
    return recepcion?.estadoCalidad || 'SIN_CALIDAD';
  }

  textoEstadoCalidad(): string {
    const estado = this.estadoCalidadSeleccionada();

    if (estado === 'APROBADA') {
      return 'Aprobada';
    }

    if (estado === 'RETENIDA') {
      return 'Retenida';
    }

    if (estado === 'NO_APROBADA') {
      return 'No aprobada';
    }

    return 'Sin control';
  }

  claseEstadoCalidad(): string {
    const estado = this.estadoCalidadSeleccionada();

    if (estado === 'APROBADA') {
      return 'bg-emerald-50 text-emerald-700 border-emerald-100';
    }

    if (estado === 'RETENIDA' || estado === 'NO_APROBADA') {
      return 'bg-red-50 text-red-700 border-red-100';
    }

    return 'bg-amber-50 text-amber-700 border-amber-100';
  }

  puedeDescremarSeleccionada(): boolean {
    const estado = this.estadoCalidadSeleccionada();
    return estado !== 'RETENIDA' && estado !== 'NO_APROBADA';
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

  private compararRecepcionesRecientes(a: RecepcionLeche, b: RecepcionLeche): number {
    const fechaA = this.tiempoRecepcion(a);
    const fechaB = this.tiempoRecepcion(b);

    if (fechaA !== fechaB) {
      return fechaB - fechaA;
    }

    return Number(b.id || 0) - Number(a.id || 0);
  }

  private tiempoRecepcion(recepcion: RecepcionLeche): number {
    const tiempo = new Date(recepcion.fechaRecepcion || '').getTime();

    if (!Number.isNaN(tiempo)) {
      return tiempo;
    }

    return 0;
  }

  private litrosDisponiblesPara(recepcion: RecepcionLeche): number {
    const recibido = Number(recepcion.cantidadRecibidaLitros || 0);

    const descremado = this.descremadosRegistrados
      .filter(item => Number(item.idRecepcionLeche) === Number(recepcion.id))
      .reduce((total, item) => total + Number(item.litrosDescremados || 0), 0);

    return Math.max(recibido - descremado, 0);
  }

  private estadoCalidadRecepcion(idRecepcion: number): RecepcionLeche['estadoCalidad'] {
    return this.estadosCalidad
      .find(estado => Number(estado.idRecepcionLeche) === Number(idRecepcion))
      ?.estadoCalidad || 'SIN_CALIDAD';
  }
}