import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import {
  EjecucionBatch,
  EjecucionBatchService,
  TipoNovedad
} from '../../core/services/ejecucion-batch';

import {
  OrdenProduccionService,
  OrdenProduccionResponse
} from '../../core/services/orden-produccion';

import { CatalogoService } from '../../core/services/catalogo';
import { NotificationService } from '../../core/services/notification';
import { RecepcionLecheService, SaldoTanqueLeche } from '../../core/services/recepcion-leche';

@Component({
  selector: 'app-orden-ejecucion',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './orden-ejecucion.html'
})
export class OrdenEjecucion implements OnInit {

  idOrden: number = 0;
  orden: OrdenProduccionResponse | null = null;
  batches: EjecucionBatch[] = [];
  marmitas: any[] = [];
  tanques: SaldoTanqueLeche[] = [];
  Math = Math;

  cargando = false;
  error = '';
  mensajeExito = '';

  nuevoBatch = {
    numeroBatch: 1,
    idMarmita: 0,
    kgEntrada: 0
  };

  batchAFinalizar: EjecucionBatch | null = null;
  batchEditando: EjecucionBatch | null = null;

  edicionBatch = {
    kgProducidos: 0,
    brixFinal: null as number | null,
    observaciones: '',
    conNovedad: false,
    tipoNovedad: null as TipoNovedad | null,
    huboReproceso: false,
    batchConforme: true
  };

  readonly tiposNovedad: { valor: TipoNovedad; etiqueta: string }[] = [
    { valor: 'BAJA_GRASA', etiqueta: 'Baja grasa' },
    { valor: 'FALLA_CALDERA', etiqueta: 'Falla caldera' },
    { valor: 'RETRASO_LECHE', etiqueta: 'Retraso de leche' },
    { valor: 'FALLA_EQUIPO', etiqueta: 'Falla de equipo' },
    { valor: 'BRIX_FUERA_RANGO', etiqueta: 'Brix fuera de rango' },
    { valor: 'REPROCESO', etiqueta: 'Reproceso' },
    { valor: 'CAMBIO_PROCESO', etiqueta: 'Cambio de proceso' },
    { valor: 'OTRO', etiqueta: 'Otro' },
  ];

  finalizacion = {
    kgProducidos: 0,
    observaciones: '',
    conNovedad: false,
    tipoNovedad: null as TipoNovedad | null,
    huboReproceso: false,
    batchConforme: true,
    brixFinal: null as number | null
  };

  skusEditables: any[] = [];

  mostrarConfirmacionCierre = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private batchService: EjecucionBatchService,
    private ordenService: OrdenProduccionService,
    private catalogoService: CatalogoService,
    private recepcionLecheService: RecepcionLecheService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.idOrden = Number(this.route.snapshot.paramMap.get('id'));

    if (this.idOrden) {
      this.cargarDatos();
    }
  }

  cargarDatos(): void {
    this.cargando = true;

    forkJoin({
      orden: this.ordenService.obtenerPorId(this.idOrden),
      batches: this.batchService.listarPorOrden(this.idOrden),
      marmitas: this.catalogoService.listarMarmitas(),
      tanques: this.recepcionLecheService.listarSaldosTanques()
    }).subscribe({
      next: ({ orden, batches, marmitas, tanques }) => {
        this.orden = orden;
        this.batches = batches;
        this.marmitas = marmitas;
        this.tanques = tanques.filter(t => t.activo);

        if (this.batches.length > 0) {
          this.nuevoBatch.numeroBatch = Math.max(...this.batches.map(x => x.numeroBatch)) + 1;
        } else {
          this.nuevoBatch.numeroBatch = 1;
        }

        this.actualizarMarmitaSugerida();
        this.usarKgBatchFormula();
        this.prepararSkusEditables();

        this.cargando = false;
      },
      error: () => {
        this.notification.error('Error al cargar los datos de la orden.');
        this.cargando = false;
      }
    });
  }

  cambiarTanqueOrden(event: Event): void {
    const select = event.target as HTMLSelectElement;
    const idTanque = Number(select.value);

    if (!this.orden || !idTanque || this.estaFinalizada) {
      return;
    }

    this.cargando = true;

    this.ordenService.actualizarTanqueLeche(this.orden.id, idTanque).subscribe({
      next: (ordenActualizada) => {
        this.orden = ordenActualizada;
        this.notification.toast('Tanque de leche descremada actualizado.');
        this.cargando = false;
      },
      error: (err) => {
        this.notification.error(err.error?.message || 'No se pudo actualizar el tanque de leche descremada.');
        this.cargando = false;
      }
    });
  }

  actualizarMarmitaSugerida(): void {
    if (this.marmitas.length > 0) {
      const proximoNum = this.batches.length > 0
        ? Math.max(...this.batches.map(x => x.numeroBatch)) + 1
        : 1;

      const index = (proximoNum - 1) % this.marmitas.length;
      this.nuevoBatch.idMarmita = this.marmitas[index].id;
    }
  }

  obtenerKgBatchFormula(): number {
    return Number(this.orden?.kgBachePlan || 0);
  }

  usarKgBatchFormula(): void {
    const kgBatchFormula = this.obtenerKgBatchFormula();

    if (kgBatchFormula > 0 && !this.estaFinalizada && !this.limiteAlcanzado) {
      this.nuevoBatch.kgEntrada = Number(kgBatchFormula.toFixed(3));
    }
  }

  obtenerRendimientoTeoricoPct(): number {
    if (!this.orden?.skus || this.orden.skus.length === 0) {
      return 0;
    }

    const skuConRendimiento = this.orden.skus.find(
      sku => Number(sku.rendimientoTeoricoPct || 0) > 0
    );

    return Number(skuConRendimiento?.rendimientoTeoricoPct || 0);
  }

  obtenerKgSalidaSugerida(batch?: EjecucionBatch): number {
    const rendimientoTeoricoPct = this.obtenerRendimientoTeoricoPct();

    if (rendimientoTeoricoPct <= 0) {
      return 0;
    }

    const kgEntradaBase = Number(
      batch?.kgEntrada ||
      this.nuevoBatch.kgEntrada ||
      this.orden?.kgBachePlan ||
      0
    );

    if (kgEntradaBase <= 0) {
      return 0;
    }

    return Number(((kgEntradaBase * rendimientoTeoricoPct) / 100).toFixed(2));
  }

  obtenerKgSalidaSugeridaTexto(batch?: EjecucionBatch): string {
    const sugerida = this.obtenerKgSalidaSugerida(batch);

    if (sugerida <= 0) {
      return 'Ingrese kg producidos';
    }

    return sugerida.toFixed(2);
  }

  usarKgSalidaSugerida(batch: EjecucionBatch): void {
    const sugerida = this.obtenerKgSalidaSugerida(batch);

    if (sugerida > 0 && batch.estado === 'EN_PROCESO' && !this.estaFinalizada) {
      batch.kgProducidos = sugerida;
    }
  }

  iniciarBatch(): void {
    const idMarmita = Number(this.nuevoBatch.idMarmita);
    const kgEntrada = Number(this.nuevoBatch.kgEntrada);

    if (idMarmita <= 0) {
      this.notification.warning('Debe seleccionar una marmita.');
      return;
    }

    if (kgEntrada <= 0) {
      this.notification.warning('La cantidad de entrada debe ser mayor a 0 kg.');
      return;
    }

    if (!this.orden?.idTanqueLeche) {
      this.notification.warning('Debe seleccionar el tanque de leche descremada antes de iniciar un batch.');
      return;
    }

    if (this.lecheRequeridaPorBatchLitros > 0 && this.saldoActualTanqueLitros < this.lecheRequeridaPorBatchLitros) {
      this.notification.warning(
        `No hay leche suficiente para iniciar el batch. Saldo actual: ${this.saldoActualTanqueLitros.toFixed(2)} L. Requerido: ${this.lecheRequeridaPorBatchLitros.toFixed(2)} L.`
      );
      return;
    }

    this.cargando = true;

    this.batchService.iniciar({
      idOrdenProduccion: Number(this.idOrden),
      idMarmita: idMarmita,
      kgEntrada: kgEntrada
    }).subscribe({
      next: () => {
        this.notification.toast('Batch iniciado correctamente');
        this.cargarDatos();
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al iniciar el batch.';
        this.notification.error(msg);
        this.cargando = false;
      }
    });
  }

  get estaFinalizada(): boolean {
    return this.orden?.estado === 'FINALIZADA';
  }

  get batchesLiquidados(): EjecucionBatch[] {
    return this.batches.filter(b => b.estado === 'FINALIZADO' || b.estado === 'CON_NOVEDAD');
  }

  get batchesEnProceso(): EjecucionBatch[] {
    return this.batches.filter(b => b.estado === 'EN_PROCESO');
  }

  get totalBatchesActivos(): number {
    return this.batchesEnProceso.length;
  }

  get totalBatchesCompletados(): number {
    return this.batchesLiquidados.length;
  }

  get totalBatchesPlanificados(): number {
    return Number(this.orden?.numBachesPlan || 0);
  }

  get totalBatchesPendientes(): number {
    if (this.totalBatchesPlanificados <= 0) {
      return 0;
    }

    return Math.max(
      this.totalBatchesPlanificados - this.totalBatchesCompletados - this.totalBatchesActivos,
      0
    );
  }

  get progresoBatchesTexto(): string {
    if (this.totalBatchesPlanificados <= 0) {
      return `${this.totalBatchesCompletados} batches finalizados`;
    }

    return `${this.totalBatchesCompletados} / ${this.totalBatchesPlanificados} batches finalizados`;
  }

  get kgEntradaAcumulados(): number {
    return this.batchesLiquidados.reduce((sum, b) => sum + Number(b.kgEntrada || 0), 0);
  }

  get kgProducidosAcumulados(): number {
    return this.batchesLiquidados.reduce((sum, b) => sum + Number(b.kgProducidos || 0), 0);
  }

  get reduccionEvaporacionReal(): number {
    return this.kgEntradaAcumulados - this.kgProducidosAcumulados;
  }

  get rendimientoPromedio(): number {
    if (this.kgEntradaAcumulados <= 0) return 0;

    return (this.kgProducidosAcumulados / this.kgEntradaAcumulados) * 100;
  }

  get totalKgSkus(): number {
    return this.skusEditables.reduce((sum, s) => sum + (s.cantidadReal || 0), 0);
  }

  get estadoMarmitas(): any[] {
    return this.marmitas.map(m => {
      const batchActivo = this.batches.find(b => b.idMarmita === m.id && b.estado === 'EN_PROCESO');
      const batchesRealizados = this.batches.filter(b => b.idMarmita === m.id).length;

      return {
        ...m,
        ocupada: !!batchActivo,
        batchActual: batchActivo ? batchActivo.numeroBatch : null,
        totalBatches: batchesRealizados
      };
    });
  }

  get balanceEmpaque(): number {
    return this.kgProducidosAcumulados - this.totalKgSkus;
  }

  get desviacionConsistencia(): number {
    return this.totalKgSkus - this.kgProducidosAcumulados;
  }

  get marmitasActivas(): number {
    return this.totalBatchesActivos;
  }

  get limiteAlcanzado(): boolean {
    const plan = this.orden?.numBachesPlan;

    if (!plan || plan <= 0) return false;

    return this.batches.length >= plan;
  }

  get tanqueSeleccionado(): SaldoTanqueLeche | null {
    if (!this.orden?.idTanqueLeche) return null;

    return this.tanques.find(t => Number(t.idTanque) === Number(this.orden?.idTanqueLeche)) || null;
  }

  get saldoActualTanqueLitros(): number {
    return Number(this.tanqueSeleccionado?.saldoLitros || 0);
  }

  get lecheConsumidaProduccionLitros(): number {
    return this.batchesLiquidados.reduce(
      (sum, b) => sum + Number(b.litrosLecheDescontados || 0),
      0
    );
  }

  get lecheRequeridaPorBatchLitros(): number {
    const batchConMovimiento = this.batchesLiquidados.find(
      b => Number(b.litrosLecheDescontados || 0) > 0 && Number(b.kgEntrada || 0) > 0
    );

    if (batchConMovimiento) {
      const factorLeche = Number(batchConMovimiento.litrosLecheDescontados) / Number(batchConMovimiento.kgEntrada);
      return Number((this.obtenerKgBatchFormula() * factorLeche).toFixed(3));
    }

    return 0;
  }

  get batchesPendientesPorConsumirLeche(): number {
    const plan = Number(this.orden?.numBachesPlan || 0);

    if (plan <= 0) return 0;

    return Math.max(plan - this.totalBatchesCompletados, 0);
  }

  get lechePendientePorConsumirLitros(): number {
    if (this.lecheRequeridaPorBatchLitros <= 0) return 0;

    return Number((this.batchesPendientesPorConsumirLeche * this.lecheRequeridaPorBatchLitros).toFixed(3));
  }

  get saldoProyectadoFinalLitros(): number {
    return Number((this.saldoActualTanqueLitros - this.lechePendientePorConsumirLitros).toFixed(3));
  }

  get balanceLecheSuficiente(): boolean {
    if (!this.orden?.idTanqueLeche) return false;

    if (this.lechePendientePorConsumirLitros <= 0) return true;

    return this.saldoActualTanqueLitros >= this.lechePendientePorConsumirLitros;
  }

  finalizarInline(batch: EjecucionBatch): void {
    if (!batch.kgProducidos || batch.kgProducidos <= 0) {
      this.notification.warning('Debe ingresar los Kg producidos (Salida).');
      return;
    }

    if (batch.brixFinal == null || Number(batch.brixFinal) <= 0) {
      this.notification.warning('Debe ingresar el Brix final antes de finalizar el batch.');
      return;
    }

    this.cargando = true;

    this.batchService.finalizar(batch.id, {
      kgProducidos: Number(batch.kgProducidos),
      observaciones: '',
      conNovedad: batch.conNovedad || false,
      tipoNovedad: batch.conNovedad ? (batch.tipoNovedad as any) : null,
      huboReproceso: false,
      batchConforme: true,
      brixFinal: Number(batch.brixFinal)
    }).subscribe({
      next: () => {
        this.notification.toast('Batch finalizado correctamente');
        this.cargarDatos();
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al finalizar el batch.';
        this.notification.error(msg);
        this.cargando = false;
      }
    });
  }

  eliminarBatch(batch: EjecucionBatch): void {
    this.notification.warning(`¿Está seguro de eliminar el batch #${batch.numeroBatch}? Esta acción es irreversible.`);

    import('sweetalert2').then(Swal => {
      Swal.default.fire({
        title: '¿Anular este batch?',
        html: `<p>Se anulará el <strong>Batch #${batch.numeroBatch}</strong> de la marmita <strong>${batch.nombreMarmita}</strong>.<br><br>
               <span style="color:#6b7280; font-size:0.85em;">Usa esta opción si el batch fue iniciado por error, por caída de sesión, o si necesitas reiniciarlo desde cero.</span></p>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc2626',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Sí, anular batch',
        cancelButtonText: 'Cancelar'
      }).then((result) => {
        if (result.isConfirmed) {
          this.cargando = true;

          this.batchService.eliminar(batch.id).subscribe({
            next: () => {
              this.notification.toast(`Batch #${batch.numeroBatch} eliminado`);
              this.cargarDatos();
            },
            error: (err) => {
              this.notification.error(err.error?.message || 'Error al eliminar el batch.');
              this.cargando = false;
            }
          });
        }
      });
    });
  }

  abrirEdicionBatch(batch: EjecucionBatch): void {
    this.batchEditando = batch;

    this.edicionBatch = {
      kgProducidos: Number(batch.kgProducidos || 0),
      brixFinal: batch.brixFinal != null ? Number(batch.brixFinal) : null,
      observaciones: batch.observaciones || '',
      conNovedad: batch.estado === 'CON_NOVEDAD' || Boolean(batch.conNovedad),
      tipoNovedad: batch.tipoNovedad ? batch.tipoNovedad as TipoNovedad : null,
      huboReproceso: Boolean(batch.huboReproceso),
      batchConforme: batch.batchConforme !== false
    };
  }

  cancelarEdicionBatch(): void {
    this.batchEditando = null;

    this.edicionBatch = {
      kgProducidos: 0,
      brixFinal: null,
      observaciones: '',
      conNovedad: false,
      tipoNovedad: null,
      huboReproceso: false,
      batchConforme: true
    };
  }

  guardarEdicionBatch(): void {
    if (!this.batchEditando) {
      return;
    }

    if (!this.edicionBatch.kgProducidos || Number(this.edicionBatch.kgProducidos) <= 0) {
      this.notification.warning('Debe ingresar los Kg producidos.');
      return;
    }

    if (this.edicionBatch.brixFinal == null || Number(this.edicionBatch.brixFinal) <= 0) {
      this.notification.warning('Debe ingresar el Brix final.');
      return;
    }

    this.cargando = true;

    this.batchService.actualizarFinalizado(this.batchEditando.id, {
      kgProducidos: Number(this.edicionBatch.kgProducidos),
      brixFinal: Number(this.edicionBatch.brixFinal),
      observaciones: this.edicionBatch.observaciones,
      conNovedad: this.edicionBatch.conNovedad,
      tipoNovedad: this.edicionBatch.conNovedad ? this.edicionBatch.tipoNovedad : null,
      huboReproceso: this.edicionBatch.huboReproceso,
      batchConforme: this.edicionBatch.batchConforme
    }).subscribe({
      next: () => {
        this.notification.toast('Batch actualizado correctamente.');
        this.cancelarEdicionBatch();
        this.cargarDatos();
      },
      error: (err) => {
        this.notification.error(err.error?.message || 'No se pudo actualizar el batch.');
        this.cargando = false;
      }
    });
  }

  prepararSkusEditables(): void {
    if (!this.orden?.skus) return;

    this.skusEditables = this.orden.skus.map(s => ({
      idOrdenDetalle: s.id,
      codigoSku: s.codigoSku,
      descripcionSku: s.descripcionSku,
      unidadesPlan: s.unidadesObjetivo,
      kgPlan: s.kgProductoTerminado,
      pesoUnidadGr: s.pesoUnidadGr,
      cantidadReal: s.cantidadReal || 0,
      unidadesReales: s.unidadesReales || 0,
      observaciones: s.observaciones || '',
      desviacion: (s.unidadesReales || 0) - s.unidadesObjetivo
    }));
  }

  onUnidadesRealesChange(sku: any): void {
    sku.cantidadReal = (sku.unidadesReales * sku.pesoUnidadGr) / 1000;
    sku.desviacion = sku.unidadesReales - sku.unidadesPlan;
  }

  guardarProduccionReal(): void {
    this.cargando = true;

    this.ordenService.registrarSkus(this.idOrden, this.skusEditables).subscribe({
      next: () => {
        this.notification.success('Producción por SKU guardada correctamente.');
        this.cargarDatos();
      },
      error: (err) => {
        this.notification.error(err.error?.message || 'Error al guardar producción por SKU.');
        this.cargando = false;
      }
    });
  }

  finalizarOrden(): void {
    if (!this.orden?.idTanqueLeche) {
      this.notification.warning('Debe seleccionar el tanque de leche descremada en el detalle de la orden antes de finalizar.');
      return;
    }

    if (this.totalBatchesActivos > 0) {
      this.notification.warning('No se puede finalizar la orden con batches activos.');
      return;
    }

    if (this.totalKgSkus <= 0) {
      this.notification.warning('Debe registrar y guardar la producción real de los SKUs antes de finalizar la orden.');
      return;
    }

    this.mostrarConfirmacionCierre = true;
  }

  confirmarCierreFinal(): void {
    this.cargando = true;

    this.ordenService.finalizar(this.idOrden).subscribe({
      next: () => {
        this.mostrarConfirmacionCierre = false;
        this.notification.success('Orden de producción finalizada y liquidada correctamente.');

        setTimeout(() => {
          this.router.navigate(['/ordenes-produccion', this.idOrden]);
        }, 1500);
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al finalizar la orden.';
        this.notification.error(msg);
        this.cargando = false;
      }
    });
  }

  cancelarCierreFinal(): void {
    this.mostrarConfirmacionCierre = false;
  }
}