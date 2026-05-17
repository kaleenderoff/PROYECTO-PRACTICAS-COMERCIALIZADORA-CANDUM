import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AuthService } from '../../core/services/auth';
import {
  ControlCalidadLacteaService,
  ControlCalidadProcesoResponse,
  ControlPesoProductoResponse
} from '../../core/services/control-calidad-lactea';
import { EjecucionBatch, EjecucionBatchService } from '../../core/services/ejecucion-batch';
import {
  MedicionCalidadLacteaResponse,
  MedicionCalidadLacteaService,
  TipoMedicionCalidadLactea
} from '../../core/services/medicion-calidad-lactea';
import { NotificationService } from '../../core/services/notification';
import { OrdenProduccionResponse, OrdenProduccionService } from '../../core/services/orden-produccion';

@Component({
  selector: 'app-mediciones-calidad-lactea',
  imports: [CommonModule, FormsModule],
  templateUrl: './mediciones-calidad-lactea.html'
})
export class MedicionesCalidadLactea implements OnInit {

  ordenes: OrdenProduccionResponse[] = [];
  batches: EjecucionBatch[] = [];
  mediciones: MedicionCalidadLacteaResponse[] = [];
  controlesProceso: ControlCalidadProcesoResponse[] = [];
  controlesPeso: ControlPesoProductoResponse[] = [];

  idOrdenSeleccionada = 0;
  pestanaActiva: 'rapida' | 'proceso' | 'peso' = 'rapida';
  cargando = false;
  guardando = false;
  error = '';

  formulario = {
    tipoMedicion: 'BACHE' as TipoMedicionCalidadLactea,
    idEjecucionBatch: 0,
    referencia: '',
    brix: null as number | null,
    ph: null as number | null,
    observaciones: ''
  };

  procesoForm = this.crearProcesoForm();
  pesoForm = this.crearPesoForm();

  constructor(
    private ordenService: OrdenProduccionService,
    private batchService: EjecucionBatchService,
    private medicionService: MedicionCalidadLacteaService,
    private controlCalidadService: ControlCalidadLacteaService,
    private authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.cargando = true;
    this.error = '';

    this.ordenService.listar().subscribe({
      next: (ordenes) => {
        this.ordenes = ordenes;
        const ordenActiva = ordenes.find(o => o.estado === 'EN_EJECUCION') || ordenes[0];
        if (ordenActiva) {
          this.idOrdenSeleccionada = ordenActiva.id;
          this.cargarDatosOrden();
        } else {
          this.cargando = false;
        }
      },
      error: () => {
        this.error = 'No se pudieron cargar las ordenes de produccion.';
        this.cargando = false;
      }
    });
  }

  cargarDatosOrden(): void {
    if (!this.idOrdenSeleccionada) {
      this.batches = [];
      this.mediciones = [];
      this.controlesProceso = [];
      this.controlesPeso = [];
      this.cargando = false;
      return;
    }

    this.cargando = true;
    this.error = '';

    this.batchService.listarPorOrden(this.idOrdenSeleccionada).subscribe({
      next: (batches) => {
        this.batches = batches;
        this.autocompletarReferencia();
      },
      error: () => {
        this.error = 'No se pudieron cargar los batches de la orden.';
        this.cargando = false;
      }
    });

    this.medicionService.listarPorOrden(this.idOrdenSeleccionada).subscribe({
      next: (mediciones) => {
        this.mediciones = mediciones;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las mediciones de calidad.';
        this.cargando = false;
      }
    });

    this.controlCalidadService.listarProcesosPorOrden(this.idOrdenSeleccionada).subscribe({
      next: controles => this.controlesProceso = controles,
      error: () => this.error = 'No se pudieron cargar los controles completos de proceso.'
    });

    this.controlCalidadService.listarPesosPorOrden(this.idOrdenSeleccionada).subscribe({
      next: controles => this.controlesPeso = controles,
      error: () => this.error = 'No se pudieron cargar los controles de peso.'
    });
  }

  onCambioOrden(): void {
    this.formulario.idEjecucionBatch = 0;
    this.formulario.referencia = '';
    this.procesoForm = this.crearProcesoForm();
    this.pesoForm = this.crearPesoForm();
    this.cargarDatosOrden();
  }

  onCambioTipo(): void {
    if (this.formulario.tipoMedicion !== 'BACHE') {
      this.formulario.idEjecucionBatch = 0;
    }
    this.autocompletarReferencia();
  }

  onCambioBatch(): void {
    this.autocompletarReferencia();
  }

  registrar(): void {
    const idUsuarioCalidad = this.authService.getIdUsuario();

    if (!this.idOrdenSeleccionada) {
      this.notification.warning('Debe seleccionar una orden de produccion.');
      return;
    }

    if (!idUsuarioCalidad) {
      this.notification.warning('No se pudo identificar el usuario autenticado.');
      return;
    }

    if (!this.formulario.referencia.trim()) {
      this.notification.warning('La referencia de la medicion es obligatoria.');
      return;
    }

    if (this.formulario.brix === null && this.formulario.ph === null) {
      this.notification.warning('Debe registrar Brix, pH o ambos.');
      return;
    }

    this.guardando = true;

    this.medicionService.registrar({
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.formulario.idEjecucionBatch || null,
      tipoMedicion: this.formulario.tipoMedicion,
      referencia: this.formulario.referencia.trim(),
      brix: this.formulario.brix,
      ph: this.formulario.ph,
      idUsuarioCalidad,
      observaciones: this.formulario.observaciones?.trim() || null
    }).subscribe({
      next: () => {
        this.notification.toast('Medicion de calidad registrada.');
        this.limpiarFormulario();
        this.cargarDatosOrden();
      },
      error: (err) => {
        this.notification.error(err.error?.message || 'No se pudo registrar la medicion.');
        this.guardando = false;
      },
      complete: () => {
        this.guardando = false;
      }
    });
  }

  limpiarFormulario(): void {
    this.formulario = {
      tipoMedicion: 'BACHE',
      idEjecucionBatch: 0,
      referencia: '',
      brix: null,
      ph: null,
      observaciones: ''
    };
    this.autocompletarReferencia();
  }

  registrarProceso(): void {
    const idRealizadoPor = this.authService.getIdUsuario();
    if (!this.validarBase(idRealizadoPor)) return;

    this.guardando = true;
    this.controlCalidadService.registrarProceso({
      ...this.procesoForm,
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.procesoForm.idEjecucionBatch || null,
      horaInicioHidrolisis: this.procesoForm.horaInicioHidrolisis || null,
      horaFinHidrolisis: this.procesoForm.horaFinHidrolisis || null,
      fechaVencimiento: this.procesoForm.fechaVencimiento || null,
      idRealizadoPor,
      idVerificadoPor: this.procesoForm.idVerificadoPor || null
    }).subscribe({
      next: () => {
        this.notification.toast('Control de proceso registrado.');
        this.procesoForm = this.crearProcesoForm();
        this.cargarDatosOrden();
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo registrar el control de proceso.');
        this.guardando = false;
      },
      complete: () => this.guardando = false
    });
  }

  registrarPeso(): void {
    const idRealizadoPor = this.authService.getIdUsuario();
    if (!this.validarBase(idRealizadoPor)) return;

    const muestras = this.pesoForm.muestras
      .filter(m => m.pesoNeto !== null && m.pesoNeto !== undefined && Number(m.pesoNeto) > 0)
      .map(m => ({
        numeroMuestra: m.numeroMuestra,
        pesoBruto: m.pesoBruto,
        tara: m.tara,
        pesoNeto: Number(m.pesoNeto)
      }));

    if (!muestras.length) {
      this.notification.warning('Debe registrar al menos una muestra de peso neto.');
      return;
    }

    this.guardando = true;
    this.controlCalidadService.registrarPeso({
      ...this.pesoForm,
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.pesoForm.idEjecucionBatch || null,
      idSku: this.pesoForm.idSku || null,
      fechaVencimiento: this.pesoForm.fechaVencimiento || null,
      idRealizadoPor,
      idVerificadoPor: this.pesoForm.idVerificadoPor || null,
      muestras
    }).subscribe({
      next: () => {
        this.notification.toast('Control de peso registrado.');
        this.pesoForm = this.crearPesoForm();
        this.cargarDatosOrden();
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo registrar el control de peso.');
        this.guardando = false;
      },
      complete: () => this.guardando = false
    });
  }

  onCambioBatchProceso(): void {
    const batch = this.obtenerBatch(this.procesoForm.idEjecucionBatch);
    this.procesoForm.numeroMarmita = batch?.numeroBatch || null;
  }

  recalcularPromediosPeso(): void {
    const muestras = this.pesoForm.muestras.filter(m => m.pesoNeto !== null && m.pesoNeto !== undefined && Number(m.pesoNeto) > 0);
    if (!muestras.length) {
      this.pesoForm.pesoNetoPromedio = null;
      return;
    }
    const total = muestras.reduce((sum, muestra) => sum + Number(muestra.pesoNeto || 0), 0);
    this.pesoForm.pesoNetoPromedio = Number((total / muestras.length).toFixed(3));
  }

  obtenerOrdenSeleccionada(): OrdenProduccionResponse | undefined {
    return this.ordenes.find(o => o.id === this.idOrdenSeleccionada);
  }

  obtenerBatch(idBatch?: number | null): EjecucionBatch | undefined {
    return this.batches.find(b => b.id === idBatch);
  }

  promedioBrix(): number {
    const valores = this.mediciones
      .map(m => m.brix)
      .filter((valor): valor is number => valor !== null && valor !== undefined);
    if (!valores.length) return 0;
    return valores.reduce((sum, valor) => sum + valor, 0) / valores.length;
  }

  promedioPh(): number {
    const valores = this.mediciones
      .map(m => m.ph)
      .filter((valor): valor is number => valor !== null && valor !== undefined);
    if (!valores.length) return 0;
    return valores.reduce((sum, valor) => sum + valor, 0) / valores.length;
  }

  private validarBase(idUsuario: number): boolean {
    if (!this.idOrdenSeleccionada) {
      this.notification.warning('Debe seleccionar una orden de produccion.');
      return false;
    }
    if (!idUsuario) {
      this.notification.warning('No se pudo identificar el usuario autenticado.');
      return false;
    }
    return true;
  }

  private crearProcesoForm() {
    const orden = this.obtenerOrdenSeleccionada();
    return {
      idEjecucionBatch: 0,
      fechaProduccion: orden?.fechaProduccion || new Date().toISOString().slice(0, 10),
      tipoProducto: orden?.nombreProducto || '',
      producto: orden?.nombreProducto || '',
      lote: '',
      numeroMarmita: null as number | null,
      productoEnProceso: '',
      phLeche: null as number | null,
      acidezLeche: null as number | null,
      densidadLeche: null as number | null,
      grasaLeche: null as number | null,
      horaInicioHidrolisis: '',
      phInicial: null as number | null,
      horaFinHidrolisis: '',
      temperaturaInicial: null as number | null,
      temperaturaFinal: null as number | null,
      acidezInicial: null as number | null,
      acidezFinal: null as number | null,
      phFinal: null as number | null,
      brixInicial: null as number | null,
      brixFinal: null as number | null,
      presion: null as number | null,
      temperaturaCoccion: null as number | null,
      temperaturaEnvasado: null as number | null,
      colorVisual: '',
      saborVisual: '',
      texturaVisual: '',
      presentacionEnvasado: '',
      fechaVencimiento: null as string | null,
      liberado: false,
      retenido: false,
      idRealizadoPor: 0,
      idVerificadoPor: null as number | null,
      observaciones: ''
    };
  }

  private crearPesoForm() {
    const orden = this.obtenerOrdenSeleccionada();
    return {
      idEjecucionBatch: 0,
      idSku: null as number | null,
      fechaControl: orden?.fechaProduccion || new Date().toISOString().slice(0, 10),
      producto: orden?.nombreProducto || '',
      marca: '',
      lote: '',
      fechaVencimiento: null as string | null,
      presentacion: '',
      numeroTanda: '',
      rangoBatches: '',
      pesoBrutoPromedio: null as number | null,
      taraPromedio: null as number | null,
      pesoNetoPromedio: null as number | null,
      aparienciaOk: true,
      etiquetadoOk: true,
      tapadoOk: true,
      cantidadPorCaja: null as number | null,
      liberado: false,
      retenido: false,
      idRealizadoPor: 0,
      idVerificadoPor: null as number | null,
      observaciones: '',
      muestras: Array.from({ length: 10 }, (_, index) => ({
        numeroMuestra: index + 1,
        pesoBruto: null as number | null,
        tara: null as number | null,
        pesoNeto: null as number | null
      }))
    };
  }

  private autocompletarReferencia(): void {
    if (this.formulario.tipoMedicion === 'BACHE') {
      const batch = this.obtenerBatch(this.formulario.idEjecucionBatch);
      this.formulario.referencia = batch ? `B.${batch.numeroBatch}` : '';
      return;
    }

    if (this.formulario.tipoMedicion === 'MEZCLA') {
      this.formulario.referencia = 'Mezcla';
      return;
    }

    if (this.formulario.tipoMedicion === 'TANDA' && !this.formulario.referencia.trim()) {
      this.formulario.referencia = 'Tanda';
    }
  }
}
