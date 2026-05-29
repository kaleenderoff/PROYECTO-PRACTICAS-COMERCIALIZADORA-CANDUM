import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

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
  idMedicionEditando: number | null = null;
  idProcesoEditando: number | null = null;
  idPesoEditando: number | null = null;

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
    public authService: AuthService,
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

    forkJoin({
      batches: this.batchService.listarPorOrden(this.idOrdenSeleccionada).pipe(
        catchError(err => {
          console.error('Error cargando batches:', err);
          return of([]);
        })
      ),
      mediciones: this.medicionService.listarPorOrden(this.idOrdenSeleccionada).pipe(
        catchError(err => {
          console.error('Error cargando mediciones de calidad:', err);
          this.notification.error(err.error?.message || 'No se pudieron cargar las mediciones de calidad.');
          return throwError(() => err);
        })
      ),
      controlesProceso: this.controlCalidadService.listarProcesosPorOrden(this.idOrdenSeleccionada).pipe(
        catchError(err => {
          console.error('Error cargando controles de proceso:', err);
          return of([]);
        })
      ),
      controlesPeso: this.controlCalidadService.listarPesosPorOrden(this.idOrdenSeleccionada).pipe(
        catchError(err => {
          console.error('Error cargando controles de peso:', err);
          return of([]);
        })
      )
    }).subscribe({
      next: ({ batches, mediciones, controlesProceso, controlesPeso }) => {
        this.batches = batches;
        this.mediciones = mediciones;
        this.controlesProceso = controlesProceso;
        this.controlesPeso = controlesPeso;
        this.autocompletarReferencia();
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los datos de calidad.';
        this.cargando = false;
      }
    });
  }

  onCambioOrden(): void {
    this.formulario.idEjecucionBatch = 0;
    this.formulario.referencia = '';
    this.idMedicionEditando = null;
    this.idProcesoEditando = null;
    this.idPesoEditando = null;
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
    if (!this.authService.canWriteCalidad()) return;

    const idUsuarioCalidad = this.authService.getIdUsuario();

    if (!this.idOrdenSeleccionada) {
      this.notification.warning('Debe seleccionar una orden de produccion.');
      return;
    }

    if (!idUsuarioCalidad) {
      this.notification.warning('No se pudo identificar el usuario autenticado.');
      return;
    }

    if (this.formulario.tipoMedicion === 'BACHE' && !this.formulario.idEjecucionBatch) {
      this.notification.warning('Debe seleccionar un batch.');
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

    if (
      !this.idMedicionEditando &&
      this.formulario.tipoMedicion === 'BACHE' &&
      this.formulario.idEjecucionBatch &&
      this.batchYaMedido(this.formulario.idEjecucionBatch)
    ) {
      this.notification.warning('Este batch ya tiene medicion registrada. Use editar si necesita corregirla.');
      return;
    }

    this.guardando = true;

    const request = {
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.formulario.idEjecucionBatch || null,
      tipoMedicion: this.formulario.tipoMedicion,
      referencia: this.formulario.referencia.trim(),
      brix: this.formulario.brix,
      ph: this.formulario.ph,
      idUsuarioCalidad,
      observaciones: this.formulario.observaciones?.trim() || null
    };

    const operacion = this.idMedicionEditando
      ? this.medicionService.actualizar(this.idMedicionEditando, request)
      : this.medicionService.registrar(request);

    operacion.subscribe({
      next: () => {
        this.notification.toast(this.idMedicionEditando ? 'Medicion de calidad actualizada.' : 'Medicion de calidad registrada.');
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
    this.idMedicionEditando = null;

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

  cancelarEdicionProceso(): void {
    this.idProcesoEditando = null;
    this.procesoForm = this.crearProcesoForm();
  }

  cancelarEdicionPeso(): void {
    this.idPesoEditando = null;
    this.pesoForm = this.crearPesoForm();
  }

  editarMedicion(medicion: MedicionCalidadLacteaResponse): void {
    if (!this.authService.canWriteCalidad()) return;

    this.idMedicionEditando = medicion.id;

    this.formulario = {
      tipoMedicion: medicion.tipoMedicion,
      idEjecucionBatch: medicion.idEjecucionBatch || 0,
      referencia: medicion.referencia,
      brix: medicion.brix ?? null,
      ph: medicion.ph ?? null,
      observaciones: medicion.observaciones || ''
    };

    this.pestanaActiva = 'rapida';
  }

  async eliminarMedicion(medicion: MedicionCalidadLacteaResponse): Promise<void> {
    if (!this.authService.canWriteCalidad()) return;

    const confirmado = await this.notification.confirm({
      title: 'Eliminar medición',
      text: `¿Desea eliminar la medición ${medicion.referencia}? Esta acción no se puede deshacer.`,
      confirmText: 'Sí, eliminar',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    this.medicionService.eliminar(medicion.id).subscribe({
      next: () => {
        this.notification.toast('Medicion eliminada.');
        this.cargarDatosOrden();
      },
      error: err => this.notification.error(err.error?.message || 'No se pudo eliminar la medicion.')
    });
  }

  registrarProceso(): void {
    if (!this.authService.canWriteCalidad()) return;

    const idRealizadoPor = this.authService.getIdUsuario();
    if (!this.validarBase(idRealizadoPor)) return;

    this.guardando = true;

    const request = {
      ...this.procesoForm,
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.procesoForm.idEjecucionBatch || null,
      horaInicioHidrolisis: this.procesoForm.horaInicioHidrolisis || null,
      horaFinHidrolisis: this.procesoForm.horaFinHidrolisis || null,
      fechaVencimiento: this.procesoForm.fechaVencimiento || null,
      idRealizadoPor,
      idVerificadoPor: this.procesoForm.idVerificadoPor || null
    };

    const operacion = this.idProcesoEditando
      ? this.controlCalidadService.actualizarProceso(this.idProcesoEditando, request)
      : this.controlCalidadService.registrarProceso(request);

    operacion.subscribe({
      next: () => {
        this.notification.toast(this.idProcesoEditando ? 'Control de proceso actualizado.' : 'Control de proceso registrado.');
        this.idProcesoEditando = null;
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
    if (!this.authService.canWriteCalidad()) return;

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

    const request = {
      ...this.pesoForm,
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.pesoForm.idEjecucionBatch || null,
      idSku: this.pesoForm.idSku || null,
      fechaVencimiento: this.pesoForm.fechaVencimiento || null,
      idRealizadoPor,
      idVerificadoPor: this.pesoForm.idVerificadoPor || null,
      muestras
    };

    const operacion = this.idPesoEditando
      ? this.controlCalidadService.actualizarPeso(this.idPesoEditando, request)
      : this.controlCalidadService.registrarPeso(request);

    operacion.subscribe({
      next: () => {
        this.notification.toast(this.idPesoEditando ? 'Control de peso actualizado.' : 'Control de peso registrado.');
        this.idPesoEditando = null;
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

  editarProceso(control: ControlCalidadProcesoResponse): void {
    if (!this.authService.canWriteCalidad()) return;

    this.idProcesoEditando = control.id;

    this.procesoForm = {
      idEjecucionBatch: control.idEjecucionBatch || 0,
      fechaProduccion: control.fechaProduccion,
      tipoProducto: control.tipoProducto || '',
      producto: control.producto || '',
      lote: control.lote || '',
      numeroMarmita: control.numeroMarmita || null,
      productoEnProceso: control.productoEnProceso || '',
      phLeche: control.phLeche || null,
      acidezLeche: control.acidezLeche || null,
      densidadLeche: control.densidadLeche || null,
      grasaLeche: control.grasaLeche || null,
      horaInicioHidrolisis: control.horaInicioHidrolisis || '',
      phInicial: control.phInicial || null,
      horaFinHidrolisis: control.horaFinHidrolisis || '',
      temperaturaInicial: control.temperaturaInicial || null,
      temperaturaFinal: control.temperaturaFinal || null,
      acidezInicial: control.acidezInicial || null,
      acidezFinal: control.acidezFinal || null,
      phFinal: control.phFinal || null,
      brixInicial: control.brixInicial || null,
      brixFinal: control.brixFinal || null,
      presion: control.presion || null,
      temperaturaCoccion: control.temperaturaCoccion || null,
      temperaturaEnvasado: control.temperaturaEnvasado || null,
      colorVisual: control.colorVisual || '',
      saborVisual: control.saborVisual || '',
      texturaVisual: control.texturaVisual || '',
      presentacionEnvasado: control.presentacionEnvasado || '',
      fechaVencimiento: control.fechaVencimiento || null,
      liberado: !!control.liberado,
      retenido: !!control.retenido,
      idRealizadoPor: 0,
      idVerificadoPor: control.idVerificadoPor || null,
      observaciones: control.observaciones || ''
    };

    this.pestanaActiva = 'proceso';
  }

  async eliminarProceso(control: ControlCalidadProcesoResponse): Promise<void> {
    if (!this.authService.canWriteCalidad()) return;

    const confirmado = await this.notification.confirm({
      title: 'Eliminar control de proceso',
      text: '¿Desea eliminar este control de proceso? Esta acción no se puede deshacer.',
      confirmText: 'Sí, eliminar',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    this.controlCalidadService.eliminarProceso(control.id).subscribe({
      next: () => {
        this.notification.toast('Control de proceso eliminado.');
        this.cargarDatosOrden();
      },
      error: err => this.notification.error(err.error?.message || 'No se pudo eliminar el control de proceso.')
    });
  }

  editarPeso(control: ControlPesoProductoResponse): void {
    if (!this.authService.canWriteCalidad()) return;

    this.idPesoEditando = control.id;

    this.pesoForm = {
      idEjecucionBatch: control.idEjecucionBatch || 0,
      idSku: control.idSku || null,
      fechaControl: control.fechaControl,
      producto: control.producto || '',
      marca: control.marca || '',
      lote: control.lote || '',
      fechaVencimiento: control.fechaVencimiento || null,
      presentacion: control.presentacion || '',
      numeroTanda: control.numeroTanda || '',
      rangoBatches: control.rangoBatches || '',
      pesoBrutoPromedio: control.pesoBrutoPromedio || null,
      taraPromedio: control.taraPromedio || null,
      pesoNetoPromedio: control.pesoNetoPromedio || null,
      aparienciaOk: !!control.aparienciaOk,
      etiquetadoOk: !!control.etiquetadoOk,
      tapadoOk: !!control.tapadoOk,
      cantidadPorCaja: control.cantidadPorCaja || null,
      liberado: !!control.liberado,
      retenido: !!control.retenido,
      idRealizadoPor: 0,
      idVerificadoPor: control.idVerificadoPor || null,
      observaciones: control.observaciones || '',
      muestras: Array.from({ length: 10 }, (_, index) => {
        const muestra = control.muestras?.[index];
        return {
          numeroMuestra: index + 1,
          pesoBruto: muestra?.pesoBruto ?? null,
          tara: muestra?.tara ?? null,
          pesoNeto: muestra?.pesoNeto ?? null
        };
      })
    };

    this.pestanaActiva = 'peso';
  }

  async eliminarPeso(control: ControlPesoProductoResponse): Promise<void> {
    if (!this.authService.canWriteCalidad()) return;

    const confirmado = await this.notification.confirm({
      title: 'Eliminar control de peso',
      text: '¿Desea eliminar este control de peso? Esta acción no se puede deshacer.',
      confirmText: 'Sí, eliminar',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    this.controlCalidadService.eliminarPeso(control.id).subscribe({
      next: () => {
        this.notification.toast('Control de peso eliminado.');
        this.cargarDatosOrden();
      },
      error: err => this.notification.error(err.error?.message || 'No se pudo eliminar el control de peso.')
    });
  }

  recalcularPromediosPeso(): void {
    const muestras = this.pesoForm.muestras.filter(m =>
      m.pesoNeto !== null &&
      m.pesoNeto !== undefined &&
      Number(m.pesoNeto) > 0
    );

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

  get medicionesRapidasBatch(): MedicionCalidadLacteaResponse[] {
    return this.mediciones.filter(m =>
      m.tipoMedicion === 'BACHE' &&
      m.idEjecucionBatch !== null &&
      m.idEjecucionBatch !== undefined
    );
  }

  batchYaMedido(idBatch: number): boolean {
    return this.medicionesRapidasBatch.some(m => Number(m.idEjecucionBatch) === Number(idBatch));
  }

  get batchesDisponiblesParaMedicion(): EjecucionBatch[] {
    if (this.idMedicionEditando) {
      return this.batches;
    }

    return this.batches.filter(batch => !this.batchYaMedido(batch.id));
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