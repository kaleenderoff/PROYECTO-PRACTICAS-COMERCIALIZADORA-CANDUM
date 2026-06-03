import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import {
  DescremadoService,
  SkuCatalogo
} from '../../core/services/descremado';

import {
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';

import { AuthService } from '../../core/services/auth';
import { NotificationService } from '../../core/services/notification';

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

  tanques: SaldoTanqueLeche[] = [];
  skusCrema: SkuCatalogo[] = [];

  private lotesCremaRegistrados = new Set<string>();

  form: any;

  constructor(
    private fb: FormBuilder,
    private descremadoService: DescremadoService,
    private recepcionLecheService: RecepcionLecheService,
    private authService: AuthService,
    private router: Router,
    private notification: NotificationService
  ) {
    this.form = this.fb.group({
      fechaDescremado: [this.fechaHoyLocal(), Validators.required],
      idTanqueOrigen: [null, Validators.required],
      idTanqueDestino: [null, Validators.required],
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

    this.form.get('idTanqueOrigen')?.valueChanges.subscribe(() => {
      const saldo = this.saldoTanqueOrigen();
      this.form.get('litrosDescremados')?.setValue(Number(saldo.toFixed(3)));
    });

    this.form.get('registraCremaEmpacada')?.valueChanges.subscribe((registra: boolean) => {
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

    this.recepcionLecheService.listarSaldosTanques().subscribe({
      next: (data) => {
        this.tanques = (data || [])
          .filter(tanque => tanque.activo)
          .sort((a, b) => String(a.nombre).localeCompare(String(b.nombre)));

        const tanqueRefrigeracion = this.tanques.find(t =>
          this.normalizarTexto(t.nombre).includes('REFRIGERACION')
        );

        if (tanqueRefrigeracion) {
          this.form.patchValue({
            idTanqueOrigen: Number(tanqueRefrigeracion.idTanque)
          });
        }

        this.cargandoDatos = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudieron cargar los saldos de tanques.';
        this.notification.error(this.error);
        this.cargandoDatos = false;
      }
    });

    this.descremadoService.listar().subscribe({
      next: (descremados) => {
        this.lotesCremaRegistrados = new Set(
          (descremados || [])
            .map(descremado => descremado.loteCrema?.trim().toUpperCase())
            .filter((lote): lote is string => Boolean(lote))
        );
      },
      error: (err) => {
        console.error(err);
        this.notification.warning('No se pudieron cargar los lotes de crema registrados.');
      }
    });

    this.descremadoService.listarSkus().subscribe({
      next: (data) => {
        this.skusCrema = (data || []).filter(sku =>
          sku.activo &&
          (
            this.normalizarTexto(sku.descripcion).includes('CREMA')
            || this.normalizarTexto(sku.nombreProducto).includes('CREMA')
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

    const idUsuario = this.authService.getIdUsuario();

    if (!idUsuario) {
      this.notification.error('No se pudo identificar el usuario autenticado.');
      return;
    }

    const value = this.form.getRawValue();
    const registraCrema = Boolean(value.registraCremaEmpacada);
    const loteCrema = typeof value.loteCrema === 'string' ? value.loteCrema.trim() : '';
    const litrosDescremados = Number(value.litrosDescremados || 0);
    const saldoDisponible = this.saldoTanqueOrigen();

    if (!value.idTanqueOrigen) {
      this.notification.warning('Debe seleccionar el tanque origen.');
      return;
    }

    if (!value.idTanqueDestino) {
      this.notification.warning('Debe seleccionar el tanque destino.');
      return;
    }

    if (registraCrema && this.lotesCremaRegistrados.has(loteCrema.toUpperCase())) {
      this.notification.warning('El lote de crema ya existe. Use un lote diferente.');
      return;
    }

    if (litrosDescremados <= 0) {
      this.notification.warning('Los litros a descremar deben ser mayores que cero.');
      return;
    }

    if (litrosDescremados > saldoDisponible) {
      this.notification.warning(`Solo hay ${saldoDisponible.toFixed(3)} L disponibles en el tanque origen.`);
      return;
    }

    if (value.idTanqueDestino && Number(value.idTanqueDestino) === Number(value.idTanqueOrigen)) {
      this.notification.warning('El tanque destino no puede ser igual al tanque origen.');
      return;
    }

    this.cargando = true;
    this.error = '';

    const request = {
      fechaDescremado: String(value.fechaDescremado),
      idTanqueOrigen: Number(value.idTanqueOrigen),
      idTanqueDestino: Number(value.idTanqueDestino),
      idUsuario: Number(idUsuario),
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

  tanqueOrigenSeleccionado(): SaldoTanqueLeche | undefined {
    const id = Number(this.form.get('idTanqueOrigen')?.value || 0);
    return this.tanques.find(tanque => Number(tanque.idTanque) === id);
  }

  tanqueDestinoSeleccionado(): SaldoTanqueLeche | undefined {
    const id = Number(this.form.get('idTanqueDestino')?.value || 0);
    return this.tanques.find(tanque => Number(tanque.idTanque) === id);
  }

  saldoTanqueOrigen(): number {
    return Number(this.tanqueOrigenSeleccionado()?.saldoLitros || 0);
  }

  litrosDespuesDescremado(): number {
    const disponibles = this.saldoTanqueOrigen();
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

  private fechaHoyLocal(): string {
    const hoy = new Date();
    const year = hoy.getFullYear();
    const month = String(hoy.getMonth() + 1).padStart(2, '0');
    const day = String(hoy.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  private normalizarTexto(valor: string): string {
    return String(valor || '')
      .trim()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, ' ')
      .toUpperCase();
  }
}
