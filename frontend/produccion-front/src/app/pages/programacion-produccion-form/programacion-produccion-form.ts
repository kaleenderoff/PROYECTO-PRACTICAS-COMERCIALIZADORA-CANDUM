import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ProgramacionProduccionService } from '../../core/services/programacion-produccion';
import {
  RecepcionLeche,
  RecepcionLecheService,
  SaldoTanqueLeche
} from '../../core/services/recepcion-leche';
import { NotificationService } from '../../core/services/notification';

import {
  SimularSkuRequest
} from '../../core/models/programacion/simular-programacion.request';

import {
  Usuario,
  UsuarioService
} from '../../core/services/usuario';

import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-programacion-produccion-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './programacion-produccion-form.html',
})
export class ProgramacionProduccionForm implements OnInit {

  private programacionService = inject(ProgramacionProduccionService);
  private recepcionLecheService = inject(RecepcionLecheService);
  private usuarioService = inject(UsuarioService);
  private notification = inject(NotificationService);

  private readonly rendimientoExcelDecimalFallback = 0.445;
  private readonly kgBatchExcelFallback = 72.82;

  productos: any[] = [];
  turnos: any[] = [];
  skusDisponibles: any[] = [];
  formulaVigente: any | null = null;
  jefesLinea: Usuario[] = [];

  recepcionesFecha: RecepcionLeche[] = [];
  saldosTanques: SaldoTanqueLeche[] = [];
  cargandoDisponibilidadLeche = false;
  lecheReservadaEnProgramaciones = 0;

  idProducto: number | null = null;
  idJefeLineaEjecutor: number | null = null;
  idTurno: number | null = null;

  fechaProduccion = this.fechaHoyLocal();
  observaciones = '';

  numBachesPlanManual: number | null = null;
  bachesPlanEditadoManual = false;

  skus: SimularSkuRequest[] = [
    {
      idSku: 0,
      unidades: 0
    }
  ];

  cargandoFormula = false;
  guardando = false;

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarTurnos();
    this.cargarJefesLinea();
    this.cargarDisponibilidadLeche();
  }

  cargarProductos(): void {
    this.programacionService.listarProductos().subscribe({
      next: productos => this.productos = productos || [],
      error: error => console.error('Error cargando productos', error)
    });
  }

  cargarTurnos(): void {
    this.programacionService.listarTurnos().subscribe({
      next: turnos => this.turnos = turnos || [],
      error: error => console.error('Error cargando turnos', error)
    });
  }

  cargarJefesLinea(): void {
    this.usuarioService.listarPorRol('JEFE_LINEA').subscribe({
      next: usuarios => this.jefesLinea = usuarios || [],
      error: error => console.error('Error cargando jefes de línea', error)
    });
  }

  cargarDisponibilidadLeche(): void {
    if (!this.fechaProduccion) {
      this.recepcionesFecha = [];
      this.saldosTanques = [];
      this.lecheReservadaEnProgramaciones = 0;
      return;
    }

    this.cargandoDisponibilidadLeche = true;

    let recepcionesCargadas = false;
    let saldosCargados = false;
    let reservadaCargada = false;

    const finalizarCarga = () => {
      if (recepcionesCargadas && saldosCargados && reservadaCargada) {
        this.cargandoDisponibilidadLeche = false;
      }
    };

    this.recepcionLecheService.listarRecepciones().subscribe({
      next: recepciones => {
        this.recepcionesFecha = (recepciones || []).filter(
          recepcion => recepcion.fechaRecepcion === this.fechaProduccion
        );
        recepcionesCargadas = true;
        finalizarCarga();
      },
      error: error => {
        console.error('Error cargando recepciones de leche', error);
        this.recepcionesFecha = [];
        recepcionesCargadas = true;
        finalizarCarga();
      }
    });

    this.recepcionLecheService.listarSaldosTanques().subscribe({
      next: saldos => {
        this.saldosTanques = (saldos || []).filter(saldo => saldo.activo !== false);
        saldosCargados = true;
        finalizarCarga();
      },
      error: error => {
        console.error('Error cargando saldos de tanques', error);
        this.saldosTanques = [];
        saldosCargados = true;
        finalizarCarga();
      }
    });

    this.programacionService.obtenerLecheReservada(this.fechaProduccion).subscribe({
      next: data => {
        this.lecheReservadaEnProgramaciones = Number(data.litrosReservados || 0);
        reservadaCargada = true;
        finalizarCarga();
      },
      error: () => {
        this.lecheReservadaEnProgramaciones = 0;
        reservadaCargada = true;
        finalizarCarga();
      }
    });
  }

  onFechaProduccionChange(): void {
    this.cargarDisponibilidadLeche();
  }

  onProductoChange(): void {
    this.skusDisponibles = [];
    this.formulaVigente = null;
    this.skus = [{ idSku: 0, unidades: 0 }];
    this.numBachesPlanManual = null;
    this.bachesPlanEditadoManual = false;

    if (!this.idProducto) {
      return;
    }

    this.cargarSkusPorProducto(this.idProducto);
    this.cargarFormulaVigente(this.idProducto);
  }

  cargarSkusPorProducto(idProducto: number): void {
    this.programacionService.listarSkusPorProducto(idProducto).subscribe({
      next: skus => this.skusDisponibles = skus || [],
      error: error => console.error('Error cargando SKUs', error)
    });
  }

  cargarFormulaVigente(idProducto: number): void {
    this.cargandoFormula = true;

    this.programacionService.obtenerFormulaVigente(idProducto).subscribe({
      next: formula => {
        this.formulaVigente = formula;
        this.cargandoFormula = false;
      },
      error: error => {
        console.error('Error cargando fórmula vigente', error);
        this.cargandoFormula = false;
      }
    });
  }

  obtenerProductoSeleccionado(): any | undefined {
    return this.productos.find(
      producto => Number(producto.id) === Number(this.idProducto)
    );
  }

  obtenerNombreLinea(): string {
    const producto = this.obtenerProductoSeleccionado();
    return producto?.nombreLinea || producto?.lineaNombre || producto?.linea || '-';
  }

  agregarFila(): void {
    this.skus.push({ idSku: 0, unidades: 0 });
  }

  eliminarFila(index: number): void {
    this.skus.splice(index, 1);

    if (this.skus.length === 0) {
      this.agregarFila();
    }
  }

  obtenerSku(idSku: number): any | undefined {
    return this.skusDisponibles.find(
      sku => Number(sku.id) === Number(idSku)
    );
  }

  obtenerDescripcionSku(fila: SimularSkuRequest): string {
    const sku = this.obtenerSku(fila.idSku);

    if (!sku) {
      return '-';
    }

    return sku.descripcion || sku.nombre || sku.codigo || '-';
  }

  obtenerEnvaseSku(fila: SimularSkuRequest): string {
    const sku = this.obtenerSku(fila.idSku);

    if (!sku) {
      return '-';
    }

    return (
      sku.envase ||
      sku.nombreEnvase ||
      sku.tipoEnvase ||
      sku.presentacion ||
      sku.formato ||
      '-'
    );
  }

  obtenerPesoUndSku(fila: SimularSkuRequest): number {
    const sku = this.obtenerSku(fila.idSku);

    if (!sku) {
      return 0;
    }

    return Number(
      sku.pesoNetoGr ||
      sku.pesoUndGr ||
      sku.pesoUnidadGr ||
      sku.pesoUnidad ||
      sku.peso ||
      0
    );
  }

  obtenerKgBatchFormula(): number {
    const valorDirecto = Number(
      this.formulaVigente?.kgBatchTotal ??
      this.formulaVigente?.kgBatch ??
      this.formulaVigente?.kgBachePlan ??
      this.formulaVigente?.cantidadBatchKg ??
      this.formulaVigente?.totalKgBatch ??
      this.formulaVigente?.tamanoBatchKg ??
      this.formulaVigente?.tamanioBatchKg ??
      0
    );

    if (!Number.isNaN(valorDirecto) && valorDirecto > 0) {
      return valorDirecto;
    }

    const detalles = this.formulaVigente?.detalles || [];

    const totalDetalles = detalles.reduce((total: number, detalle: any) => {
      return total + Number(detalle.cantidadKg || detalle.cantidad || 0);
    }, 0);

    if (!Number.isNaN(totalDetalles) && totalDetalles > 0) {
      return Number(totalDetalles.toFixed(3));
    }

    if (this.idProducto) {
      return this.kgBatchExcelFallback;
    }

    return 0;
  }

  obtenerRendimientoFormula(): number {
    const posiblesValores = [
      this.formulaVigente?.rendimientoTeoricoPct,
      this.formulaVigente?.rendimientoPct,
      this.formulaVigente?.rendimientoPorcentaje,
      this.formulaVigente?.rendimientoEstimadoPct,
      this.formulaVigente?.rendimientoEstimado,
      this.formulaVigente?.rendimiento,
      this.formulaVigente?.porcentajeRendimiento,
      this.formulaVigente?.rendimientoTeorico
    ];

    for (const valor of posiblesValores) {
      const numero = Number(valor);

      if (!Number.isNaN(numero) && numero > 0) {
        return numero > 1 ? numero : Number((numero * 100).toFixed(3));
      }
    }

    return Number((this.rendimientoExcelDecimalFallback * 100).toFixed(3));
  }

  private obtenerRendimientoDecimal(): number {
    const rendimiento = this.obtenerRendimientoFormula();

    if (rendimiento <= 0) {
      return 0;
    }

    return rendimiento > 1 ? rendimiento / 100 : rendimiento;
  }

  calcularKgProductoTerminado(fila: SimularSkuRequest): number {
    const unidades = Number(fila.unidades || 0);
    const pesoNetoGr = this.obtenerPesoUndSku(fila);

    if (unidades <= 0 || pesoNetoGr <= 0) {
      return 0;
    }

    return Number(((unidades * pesoNetoGr) / 1000).toFixed(3));
  }

  calcularKgEntradaRequeridos(fila: SimularSkuRequest): number {
    const kgPt = this.calcularKgProductoTerminado(fila);
    const rendimientoDecimal = this.obtenerRendimientoDecimal();

    if (kgPt <= 0 || rendimientoDecimal <= 0) {
      return 0;
    }

    return Number((kgPt / rendimientoDecimal).toFixed(3));
  }

  calcularBatchesTeoricos(fila: SimularSkuRequest): number {
    const kgEntrada = this.calcularKgEntradaRequeridos(fila);
    const kgBatchFormula = this.obtenerKgBatchFormula();

    if (kgEntrada <= 0 || kgBatchFormula <= 0) {
      return 0;
    }

    return Number((kgEntrada / kgBatchFormula).toFixed(3));
  }

  calcularTotalUnidades(): number {
    return this.skus
      .reduce((total, fila) => total + Number(fila.unidades || 0), 0);
  }

  calcularTotalKgPt(): number {
    return Number(
      this.skus
        .reduce((total, fila) => total + this.calcularKgProductoTerminado(fila), 0)
        .toFixed(3)
    );
  }

  calcularTotalKgEntrada(): number {
    return Number(
      this.skus
        .reduce((total, fila) => total + this.calcularKgEntradaRequeridos(fila), 0)
        .toFixed(3)
    );
  }

  calcularTotalBatchesTeoricos(): number {
    return Number(
      this.skus
        .reduce((total, fila) => total + this.calcularBatchesTeoricos(fila), 0)
        .toFixed(3)
    );
  }

  calcularBatchesSugeridos(): number {
    const totalBatchesTeoricos = this.calcularTotalBatchesTeoricos();

    if (totalBatchesTeoricos <= 0) {
      return 0;
    }

    return Math.ceil(totalBatchesTeoricos);
  }

  calcularTotalBatchesOperativos(): number {
    return this.calcularBatchesSugeridos();
  }

  obtenerBachesPlanParaVista(): number {
    if (this.bachesPlanEditadoManual && this.numBachesPlanManual !== null) {
      return Number(this.numBachesPlanManual || 0);
    }

    return this.calcularBatchesSugeridos();
  }

  onBachesPlanManualChange(value: number | string | null): void {
    const numero = Number(value);

    if (value === null || value === '' || Number.isNaN(numero)) {
      this.numBachesPlanManual = null;
      this.bachesPlanEditadoManual = false;
      return;
    }

    this.numBachesPlanManual = numero;
    this.bachesPlanEditadoManual = true;
  }

  usarBachesSugeridos(): void {
    this.numBachesPlanManual = null;
    this.bachesPlanEditadoManual = false;
  }

  calcularKgEntradaPorBatchPlan(): number {
    const batchesPlan = this.obtenerBachesPlanParaVista();
    const kgBatch = this.obtenerKgBatchFormula();

    if (batchesPlan <= 0 || kgBatch <= 0) {
      return 0;
    }

    return Number((batchesPlan * kgBatch).toFixed(3));
  }

  calcularDiferenciaKgEntrada(): number {
    return Number((this.calcularKgEntradaPorBatchPlan() - this.calcularTotalKgEntrada()).toFixed(3));
  }

  tieneSkusValidos(): boolean {
    return this.skus.some(
      fila => Number(fila.idSku) > 0 && Number(fila.unidades) > 0
    );
  }

  obtenerLitrosRecepcion(recepcion: RecepcionLeche): number {
    return Number(
      recepcion.cantidadRecibidaLitros ??
      recepcion.cantidadRemisionLitros ??
      0
    );
  }

  obtenerRecepcionesPorEstado(estados: string[]): RecepcionLeche[] {
    return this.recepcionesFecha.filter(recepcion =>
      estados.includes(recepcion.estadoCalidad || 'SIN_CALIDAD')
    );
  }

  calcularSaldoActualTanquesLitros(): number {
    return Number(
      this.saldosTanques
        .reduce((total, tanque) => total + Number(tanque.saldoLitros || 0), 0)
        .toFixed(2)
    );
  }

  calcularLechePendienteCalidadLitros(): number {
    return Number(
      this.obtenerRecepcionesPorEstado(['SIN_CALIDAD'])
        .reduce((total, recepcion) => total + this.obtenerLitrosRecepcion(recepcion), 0)
        .toFixed(2)
    );
  }

  calcularLecheRetenidaLitros(): number {
    return Number(
      this.obtenerRecepcionesPorEstado(['RETENIDA', 'NO_APROBADA'])
        .reduce((total, recepcion) => total + this.obtenerLitrosRecepcion(recepcion), 0)
        .toFixed(2)
    );
  }

  calcularLecheAprobadaLitros(): number {
    const saldoActual = this.calcularSaldoActualTanquesLitros();
    const pendiente = this.calcularLechePendienteCalidadLitros();
    const retenida = this.calcularLecheRetenidaLitros();

    const disponible = saldoActual - pendiente - retenida;

    return Number(Math.max(disponible, 0).toFixed(2));
  }

  calcularLecheTotalFisicaLitros(): number {
    const saldoActual = this.calcularSaldoActualTanquesLitros();

    if (saldoActual > 0) {
      return saldoActual;
    }

    return Number(
      this.recepcionesFecha
        .reduce((total, recepcion) => total + this.obtenerLitrosRecepcion(recepcion), 0)
        .toFixed(2)
    );
  }

  calcularLecheRequeridaLitros(): number {
    return Number(this.calcularKgEntradaPorBatchPlan().toFixed(2));
  }

  calcularLecheAprobadaDisponibleDespuesPlan(): number {
    return Number((
      this.calcularLecheAprobadaLitros()
      - this.lecheReservadaEnProgramaciones
      - this.calcularLecheRequeridaLitros()
    ).toFixed(2));
  }

  calcularLecheTotalPotencialLitros(): number {
    return Number((this.calcularLecheAprobadaLitros() + this.calcularLechePendienteCalidadLitros()).toFixed(2));
  }

  tieneLechePendienteCalidad(): boolean {
    return this.calcularLechePendienteCalidadLitros() > 0;
  }

  tieneLecheRetenida(): boolean {
    return this.calcularLecheRetenidaLitros() > 0;
  }

  programacionDependeDeLechePendiente(): boolean {
    const requerida = this.calcularLecheRequeridaLitros();

    return requerida > this.calcularLecheAprobadaLitros()
      && requerida <= this.calcularLecheTotalPotencialLitros();
  }

  noHayLecheSuficienteParaProgramar(): boolean {
    const requerida = this.calcularLecheRequeridaLitros();

    if (requerida <= 0) {
      return false;
    }

    return requerida > this.calcularLecheTotalPotencialLitros();
  }

  obtenerMensajeDisponibilidadLeche(): string {
    if (this.cargandoDisponibilidadLeche) {
      return 'Consultando recepciones, saldos de tanques y liberaciones de calidad...';
    }

    if (this.calcularLecheTotalFisicaLitros() <= 0 && this.recepcionesFecha.length === 0) {
      return 'No hay leche disponible ni recepciones registradas para la fecha programada.';
    }

    if (this.noHayLecheSuficienteParaProgramar()) {
      return 'No existe leche suficiente para cubrir esta programación, ni siquiera contando la leche pendiente por calidad.';
    }

    if (this.programacionDependeDeLechePendiente()) {
      return 'La programación depende de leche que todavía no ha sido liberada por calidad. Puede planearse, pero no debería iniciar producción hasta liberar la leche.';
    }

    if (this.tieneLechePendienteCalidad()) {
      return 'Hay leche pendiente de liberación por calidad. La leche aprobada es la única disponible para iniciar producción.';
    }

    return 'La leche aprobada disponible en tanques cubre la programación actual.';
  }

  puedeConfirmarProgramacion(): boolean {
    if (!this.tieneSkusValidos() || this.guardando) {
      return false;
    }

    return !this.noHayLecheSuficienteParaProgramar();
  }

  crearProgramacion(): void {
    if (
      !this.idProducto ||
      !this.formulaVigente ||
      !this.tieneSkusValidos() ||
      !this.idJefeLineaEjecutor ||
      !this.idTurno ||
      !this.fechaProduccion
    ) {
      this.notification.warning('Seleccione fecha, producto, turno, jefe de línea y al menos un SKU.');
      return;
    }

    if (this.noHayLecheSuficienteParaProgramar()) {
      this.notification.error('No existe leche suficiente para crear la programación.');
      return;
    }

    const productoSeleccionado = this.obtenerProductoSeleccionado();

    if (!productoSeleccionado) {
      this.notification.error('No se encontró el producto seleccionado.');
      return;
    }

    const skusValidos = this.skus
      .filter(fila => Number(fila.idSku) > 0 && Number(fila.unidades) > 0)
      .map(fila => ({
        idSku: Number(fila.idSku),
        unidadesObjetivo: Number(fila.unidades)
      }));

    const observacionesBase = this.observaciones?.trim() || 'Programación generada desde pantalla tipo Excel';

    const observacionesLeche = this.programacionDependeDeLechePendiente()
      ? ' Programación condicional: depende de leche pendiente de liberación por calidad.'
      : '';

    // kgBachePlan debe ser el kg de LECHE por batch, no el total de ingredientes de la fórmula
    // Si hay SKUs calculados, usar calcularTotalKgEntrada() / numBaches (leche real por batch)
    // Si no hay SKUs, usar obtenerKgBatchFormula() como aproximación
    const numBachesPlan = this.obtenerBachesPlanParaVista();
    const totalKgEntradaLeche = this.calcularTotalKgEntrada();
    const kgBachePlanLeche = (totalKgEntradaLeche > 0 && numBachesPlan > 0)
      ? Number((totalKgEntradaLeche / numBachesPlan).toFixed(3))
      : this.obtenerKgBatchFormula();

    const body = {
      fechaProduccion: this.fechaProduccion,
      idLinea: Number(productoSeleccionado.idLinea),
      idProducto: Number(this.idProducto),
      idTurno: Number(this.idTurno),
      idJefeLineaEjecutor: Number(this.idJefeLineaEjecutor),
      numBachesPlan,
      kgBachePlan: kgBachePlanLeche,
      idFormulaVersion: Number(this.formulaVigente.idFormulaVersion ?? this.formulaVigente.id),
      observaciones: `${observacionesBase}${observacionesLeche}`,
      skus: skusValidos
    };

    this.guardando = true;

    this.programacionService.crearProgramacion(body).subscribe({
      next: () => {
        this.guardando = false;
        this.notification.success('Programación creada correctamente.');

        this.idProducto = null;
        this.idJefeLineaEjecutor = null;
        this.idTurno = null;
        this.fechaProduccion = this.fechaHoyLocal();
        this.observaciones = '';
        this.numBachesPlanManual = null;
        this.bachesPlanEditadoManual = false;
        this.formulaVigente = null;
        this.skusDisponibles = [];
        this.skus = [{ idSku: 0, unidades: 0 }];
        this.cargarDisponibilidadLeche();

        window.scrollTo({ top: 0, behavior: 'smooth' });
      },
      error: error => {
        this.guardando = false;
        const msg = error?.error?.message || 'Error creando programación.';
        this.notification.error(msg);
      }
    });
  }

  private fechaHoyLocal(): string {
    const hoy = new Date();
    const year = hoy.getFullYear();
    const month = String(hoy.getMonth() + 1).padStart(2, '0');
    const day = String(hoy.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}