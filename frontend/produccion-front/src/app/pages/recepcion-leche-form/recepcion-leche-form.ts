import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormArray,
  FormGroup
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import {
  RecepcionLecheService,
  Proveedor,
  RecepcionLecheRequest,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';
import { AuthService } from '../../core/services/auth';
import { NotificationService } from '../../core/services/notification';

@Component({
  selector: 'app-recepcion-leche-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './recepcion-leche-form.html',
})
export class RecepcionLecheForm implements OnInit {
  cargando = false;
  cargandoDatos = false;
  error = '';

  proveedores: Proveedor[] = [];
  tanques: SaldoTanqueLeche[] = [];

  readonly tipoMateriaPrima = 'LECHE CRUDA';

  /*
   * Conversión usada según la lógica observada en las planillas:
   * litros = kg netos / 1.03
   */
  readonly factorKgPorLitro = 1.03;

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private service: RecepcionLecheService,
    private authService: AuthService,
    private router: Router,
    private notification: NotificationService
  ) {
    this.form = this.fb.group({
      fechaRecepcion: [new Date().toISOString().slice(0, 10), Validators.required],
      tipoMateriaPrima: [this.tipoMateriaPrima, Validators.required],
      proveedor: ['', Validators.required],
      idTanque: [null, Validators.required],
      cantidadRecibidaLitros: [0, [Validators.required, Validators.min(0.001)]],
      recibidoPor: [''],
      numeroRemision: [''],
      cantidadRemisionLitros: [0],
      observaciones: [''],
      pesajes: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.cargarDatosIniciales();
    this.agregarPesaje();
  }

  get pesajes(): FormArray {
    return this.form.get('pesajes') as FormArray;
  }

  cargarDatosIniciales(): void {
    this.cargandoDatos = true;
    this.error = '';

    this.service.listarProveedores().subscribe({
      next: (proveedores) => {
        this.proveedores = proveedores.filter(proveedor => proveedor.activo);

        this.service.listarSaldosTanques().subscribe({
          next: (tanques) => {
            this.tanques = tanques.filter(tanque => tanque.activo);
            this.cargandoDatos = false;
          },
          error: (err) => {
            console.error(err);
            const mensaje = err.error?.message || 'No se pudieron cargar los tanques de leche.';
            this.error = mensaje;
            this.notification.error(mensaje);
            this.cargandoDatos = false;
          }
        });
      },
      error: (err) => {
        console.error(err);
        const mensaje = err.error?.message || 'No se pudieron cargar los proveedores.';
        this.error = mensaje;
        this.notification.error(mensaje);
        this.cargandoDatos = false;
      }
    });
  }

  crearPesajeForm(numeroPesaje: number): FormGroup {
    return this.fb.group({
      numeroPesaje: [numeroPesaje, Validators.required],
      pesoBrutoKg: [0, [Validators.required, Validators.min(0.001)]],
      taraKg: [0, [Validators.required, Validators.min(0)]],
      observaciones: ['']
    });
  }

  agregarPesaje(): void {
    this.pesajes.push(this.crearPesajeForm(this.pesajes.length + 1));
    this.actualizarLitrosCalculados();
  }

  eliminarPesaje(index: number): void {
    if (this.pesajes.length === 1) {
      this.notification.warning('Debe existir al menos un pesaje.');
      return;
    }

    this.pesajes.removeAt(index);
    this.renumerarPesajes();
    this.actualizarLitrosCalculados();
  }

  renumerarPesajes(): void {
    this.pesajes.controls.forEach((control, index) => {
      control.get('numeroPesaje')?.setValue(index + 1);
    });
  }

  pesoNetoPesaje(index: number): number {
    const control = this.pesajes.at(index);
    const bruto = Number(control.get('pesoBrutoKg')?.value || 0);
    const tara = Number(control.get('taraKg')?.value || 0);
    const neto = bruto - tara;

    return neto > 0 ? neto : 0;
  }

  totalPesoBrutoKg(): number {
    return this.pesajes.controls.reduce((acc, control) => {
      return acc + Number(control.get('pesoBrutoKg')?.value || 0);
    }, 0);
  }

  totalTaraKg(): number {
    return this.pesajes.controls.reduce((acc, control) => {
      return acc + Number(control.get('taraKg')?.value || 0);
    }, 0);
  }

  totalPesoNetoKg(): number {
    return this.pesajes.controls.reduce((acc, _control, index) => {
      return acc + this.pesoNetoPesaje(index);
    }, 0);
  }

  litrosCalculadosPlanta(): number {
    const totalNeto = this.totalPesoNetoKg();

    if (totalNeto <= 0) {
      return 0;
    }

    return Number((totalNeto / this.factorKgPorLitro).toFixed(3));
  }

  actualizarLitrosCalculados(): void {
    const litros = this.litrosCalculadosPlanta();

    this.form.get('cantidadRecibidaLitros')?.setValue(litros, {
      emitEvent: false
    });
  }

  guardar(): void {
    this.actualizarLitrosCalculados();

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.notification.warning('Revise los campos obligatorios antes de guardar.');
      return;
    }

    if (this.totalPesoNetoKg() <= 0) {
      this.notification.warning('Debe registrar al menos un pesaje válido.');
      return;
    }

    if (this.porcentajeDiferenciaAbs() >= 5) {
      this.notification.warning(
        'La diferencia entre litros de remisión y litros reales es alta. Verifique antes de finalizar.'
      );
      return;
    }

    this.cargando = true;
    this.error = '';

    const value = this.form.getRawValue();

    const request: RecepcionLecheRequest = {
      fechaRecepcion: value.fechaRecepcion!,
      tipoMateriaPrima: this.tipoMateriaPrima,
      proveedor: value.proveedor!,
      cantidadRecibidaLitros: Number(value.cantidadRecibidaLitros || 0),
      recibidoPor: value.recibidoPor || undefined,
      idUsuario: this.authService.getIdUsuario(),
      idTanque: Number(value.idTanque),
      numeroRemision: value.numeroRemision || undefined,
      cantidadRemisionLitros: Number(value.cantidadRemisionLitros) || undefined,
      observaciones: value.observaciones || undefined,
      pesajes: this.pesajes.controls.map((control, index) => ({
        numeroPesaje: index + 1,
        pesoBrutoKg: Number(control.get('pesoBrutoKg')?.value || 0),
        taraKg: Number(control.get('taraKg')?.value || 0),
        observaciones: control.get('observaciones')?.value || undefined
      }))
    };

    this.service.registrarRecepcion(request).subscribe({
      next: () => {
        this.cargando = false;
        this.notification.success('Recepción de leche registrada correctamente.');
        this.router.navigate(['/recepcion-leche']);
      },
      error: (err) => {
        console.error(err);
        this.cargando = false;
        const mensaje = err.error?.message || 'No se pudo registrar la recepción de leche.';
        this.error = mensaje;
        this.notification.error(mensaje);
      }
    });
  }

  diferenciaLitros(): number {
    const real = Number(this.form.get('cantidadRecibidaLitros')?.value || 0);
    const remision = Number(this.form.get('cantidadRemisionLitros')?.value || 0);
    return real - remision;
  }

  porcentajeDiferencia(): number {
    const remision = Number(this.form.get('cantidadRemisionLitros')?.value || 0);

    if (remision <= 0) {
      return 0;
    }

    return (this.diferenciaLitros() / remision) * 100;
  }

  porcentajeDiferenciaAbs(): number {
    return Math.abs(this.porcentajeDiferencia());
  }

  claseDiferencia(): string {
    const porcentaje = this.porcentajeDiferenciaAbs();

    if (porcentaje >= 5) {
      return 'bg-red-50 text-red-700 border-red-100';
    }

    if (porcentaje >= 2) {
      return 'bg-amber-50 text-amber-700 border-amber-100';
    }

    return 'bg-emerald-50 text-emerald-700 border-emerald-100';
  }
}