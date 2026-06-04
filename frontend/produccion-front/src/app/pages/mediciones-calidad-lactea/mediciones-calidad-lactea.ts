import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthService } from '../../core/services/auth';
import { CatalogoService } from '../../core/services/catalogo';
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
import {
  OrdenProduccionResponse,
  OrdenProduccionService,
  ProgramacionSkuResponse
} from '../../core/services/orden-produccion';

interface MarcaCatalogo {
  id: number;
  nombre: string;
  esPropia?: boolean;
  activo?: boolean;
}

interface GrupoPesoProducto {
  tanda: string;
  controles: ControlPesoProductoResponse[];
}

type EstadoCalidadOrden = 'SIN_CONSULTAR' | 'PENDIENTE' | 'EN_PROCESO' | 'TANDAS_CERRADAS' | 'COMPLETA';

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
  marcas: MarcaCatalogo[] = [];

  idOrdenSeleccionada = 0;
  pestanaActiva: 'rapida' | 'proceso' | 'peso' | 'resumen' = 'rapida';

  cargando = false;
  guardando = false;
  cerrandoTandas = false;
  reabriendoTandas = false;
  resumenWhatsappVisible = false;
  detalleCalidadAbierto = false;
  error = '';

  busquedaOrdenCalidad = '';
  filtroEstadoCalidad: 'TODOS' | EstadoCalidadOrden = 'TODOS';
  filtroFechaCalidad = '';

  idMedicionEditando: number | null = null;
  idProcesoEditando: number | null = null;
  idPesoEditando: number | null = null;

  controlProcesoDetalle: ControlCalidadProcesoResponse | null = null;

  readonly presentacionesEnvasado = [
    'Bolsa',
    'Taza',
    'Tetero',
    'Dispensador',
    'Doypack',
    'Garrafa',
    'Balde',
    'Bipack',
    'Otro'
  ];

  readonly opcionesColorVisual = [
    'Normal',
    'Característico',
    'Más claro de lo esperado',
    'Más oscuro de lo esperado',
    'No conforme',
    'Otro'
  ];

  readonly opcionesSaborVisual = [
    'Característico',
    'Conforme',
    'Dulce alto',
    'Dulce bajo',
    'Sabor extraño',
    'No conforme',
    'Otro'
  ];

  readonly opcionesTexturaVisual = [
    'Conforme',
    'Homogénea',
    'Líquida',
    'Espesa',
    'Grumosa',
    'Cristalizada',
    'No conforme',
    'Otro'
  ];

  private readonly valoresEvaluacionNoConforme = [
    'NO CONFORME',
    'SABOR EXTRAÑO',
    'MAS CLARO DE LO ESPERADO',
    'MAS OSCURO DE LO ESPERADO',
    'GRUMOSA',
    'CRISTALIZADA'
  ];

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
    private catalogoService: CatalogoService,
    public authService: AuthService,
    private notification: NotificationService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) { }

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.cargando = true;
    this.error = '';

    this.ordenService.listar().subscribe({
      next: (ordenes) => {
        this.ordenes = [...ordenes].sort((a, b) => b.id - a.id);

        const ordenActiva = ordenes.find(o => o.estado === 'EN_EJECUCION') || ordenes[0];

        if (ordenActiva) {
          this.idOrdenSeleccionada = ordenActiva.id;
          this.cargarDatosOrden(false);
        } else {
          this.cargando = false;
        }
      },
      error: () => {
        this.error = 'No se pudieron cargar las órdenes de producción.';
        this.cargando = false;
      }
    });
  }

  cargarDatosOrden(abrirDetalle = true): void {
    if (!this.idOrdenSeleccionada) {
      this.batches = [];
      this.mediciones = [];
      this.controlesProceso = [];
      this.controlesPeso = [];
      this.controlProcesoDetalle = null;
      this.cargando = false;
      return;
    }

    this.cargando = true;
    this.error = '';

    if (abrirDetalle) {
      this.detalleCalidadAbierto = true;
    }

    forkJoin({
      orden: this.ordenService.obtenerPorId(this.idOrdenSeleccionada).pipe(
        catchError(err => {
          console.error('Error cargando orden:', err);
          return of(null);
        })
      ),
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
          return of([]);
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
      ),
      marcas: this.catalogoService.listarMarcas(true).pipe(
        catchError(err => {
          console.error('Error cargando marcas:', err);
          return of([]);
        })
      )
    }).subscribe({
      next: ({ orden, batches, mediciones, controlesProceso, controlesPeso, marcas }) => {
        this.ngZone.run(() => {
          if (orden) {
            this.actualizarOrdenLocal(orden);
          }

          this.batches = [...batches];
          this.mediciones = [...mediciones];
          this.controlesProceso = [...controlesProceso];
          this.controlesPeso = [...controlesPeso];
          this.marcas = [...marcas];

          this.controlProcesoDetalle = null;
          this.reiniciarProcesoFormConSiguienteBatch();
          this.pesoForm = this.crearPesoForm();
          this.autocompletarPesoDesdeOrden();
          this.autocompletarReferencia();

          this.cargando = false;
          this.cdr.detectChanges();
        });
      },
      error: () => {
        this.error = 'No se pudieron cargar los datos de calidad.';
        this.cargando = false;
      }
    });
  }

  abrirDetalleOrden(orden: OrdenProduccionResponse): void {
    this.idOrdenSeleccionada = orden.id;
    this.pestanaActiva = 'rapida';
    this.resumenWhatsappVisible = false;
    this.controlProcesoDetalle = null;
    this.idMedicionEditando = null;
    this.idProcesoEditando = null;
    this.idPesoEditando = null;

    this.formulario = {
      tipoMedicion: 'BACHE',
      idEjecucionBatch: 0,
      referencia: '',
      brix: null,
      ph: null,
      observaciones: ''
    };

    this.procesoForm = this.crearProcesoForm();
    this.pesoForm = this.crearPesoForm();

    this.cargarDatosOrden(true);
  }

  cerrarDetalleCalidad(): void {
    this.detalleCalidadAbierto = false;
    this.idMedicionEditando = null;
    this.idProcesoEditando = null;
    this.idPesoEditando = null;
    this.controlProcesoDetalle = null;
    this.resumenWhatsappVisible = false;
  }

  onCambioOrden(): void {
    this.formulario.idEjecucionBatch = 0;
    this.formulario.referencia = '';
    this.formulario.brix = null;
    this.formulario.ph = null;
    this.formulario.observaciones = '';

    this.idMedicionEditando = null;
    this.idProcesoEditando = null;
    this.idPesoEditando = null;
    this.controlProcesoDetalle = null;
    this.resumenWhatsappVisible = false;

    this.procesoForm = this.crearProcesoForm();
    this.pesoForm = this.crearPesoForm();

    this.cargarDatosOrden(true);
  }

  limpiarFiltrosCalidad(): void {
    this.busquedaOrdenCalidad = '';
    this.filtroEstadoCalidad = 'TODOS';
    this.filtroFechaCalidad = '';
  }

  abrirDetalleProceso(control: ControlCalidadProcesoResponse): void {
    this.controlProcesoDetalle = control;
    this.cdr.detectChanges();
  }

  cerrarDetalleProceso(): void {
    this.controlProcesoDetalle = null;
    this.cdr.detectChanges();
  }

  onCambioTipo(): void {
    if (this.formulario.tipoMedicion === 'TANDA' && this.tandasCerradas) {
      this.notification.warning('Las tandas de esta orden ya están cerradas. No se pueden agregar más tandas.');
      this.formulario.tipoMedicion = 'BACHE';
    }

    if (this.formulario.tipoMedicion !== 'BACHE') {
      this.formulario.idEjecucionBatch = 0;
    }

    this.idMedicionEditando = null;
    this.formulario.referencia = '';
    this.formulario.brix = null;
    this.formulario.ph = null;
    this.formulario.observaciones = '';

    this.autocompletarReferencia();
  }

  onCambioBatch(): void {
    this.autocompletarReferencia();
  }

  onCambioEvaluacionProceso(): void {
    if (!this.evaluacionProcesoSugiereRetencion()) {
      return;
    }

    if (!this.procesoForm.liberado && !this.procesoForm.retenido) {
      this.procesoForm.retenido = true;
      this.notification.warning('La evaluación visual sugiere retener el producto. Se marcó Retenido automáticamente.');
      return;
    }

    if (this.procesoForm.liberado) {
      this.notification.warning('La evaluación visual tiene una novedad. Revise si el producto debe quedar retenido en lugar de liberado.');
    }
  }

  prepararNuevaTanda(): void {
    if (!this.puedeRegistrarCalidad) {
      this.notification.warning('No tiene permisos para registrar tandas.');
      return;
    }

    if (this.tandasCerradas) {
      this.notification.warning('Las tandas de esta orden ya están cerradas. La coordinadora de calidad debe reabrirlas si se requiere una corrección.');
      return;
    }

    this.pestanaActiva = 'rapida';
    this.idMedicionEditando = null;

    this.formulario = {
      tipoMedicion: 'TANDA',
      idEjecucionBatch: 0,
      referencia: `Tanda ${this.siguienteNumeroTanda()}`,
      brix: null,
      ph: null,
      observaciones: ''
    };

    this.cdr.detectChanges();
  }

  registrar(): void {
    if (!this.puedeRegistrarCalidad) {
      this.notification.warning('No tiene permisos para registrar mediciones de calidad.');
      return;
    }

    if (this.idMedicionEditando && !this.puedeCorregirCalidad) {
      this.notification.warning('Solo coordinación de calidad puede corregir mediciones ya registradas.');
      return;
    }

    const idUsuarioCalidad = this.authService.getIdUsuario();

    if (!this.validarMedicionRapidaBase(idUsuarioCalidad)) {
      return;
    }

    this.autocompletarReferencia();

    if (!this.validarMedicionRapidaFormulario()) {
      return;
    }

    this.guardando = true;

    const request = {
      idOrdenProduccion: this.idOrdenSeleccionada,
      idEjecucionBatch: this.formulario.tipoMedicion === 'BACHE'
        ? this.formulario.idEjecucionBatch
        : null,
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
      next: (medicionGuardada) => {
        this.ngZone.run(() => {
          const eraTanda = this.formulario.tipoMedicion === 'TANDA';

          if (this.idMedicionEditando) {
            this.mediciones = this.mediciones.map(m =>
              Number(m.id) === Number(medicionGuardada.id) ? medicionGuardada : m
            );

            this.notification.toast('Medición de calidad actualizada.');
          } else {
            this.mediciones = [medicionGuardada, ...this.mediciones];

            if (eraTanda) {
              this.notification.toast(`${medicionGuardada.referencia} registrada. Puede agregar otra tanda si lo necesita.`);
            } else {
              this.notification.toast('Medición de calidad registrada.');
            }
          }

          if (eraTanda && !this.idMedicionEditando && !this.tandasCerradas) {
            this.prepararNuevaTanda();
          } else {
            this.limpiarFormulario();
          }

          this.guardando = false;
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        this.notification.error(err.error?.message || 'No se pudo registrar la medición.');
        this.guardando = false;
      },
      complete: () => {
        this.guardando = false;
      }
    });
  }

  limpiarFormulario(): void {
    const tipoActual = this.formulario.tipoMedicion;

    this.idMedicionEditando = null;

    this.formulario = {
      tipoMedicion: tipoActual,
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
    this.reiniciarProcesoFormConSiguienteBatch();
  }

  cancelarEdicionPeso(): void {
    this.idPesoEditando = null;
    this.pesoForm = this.crearPesoForm();
    this.autocompletarPesoDesdeOrden();
  }

  editarMedicion(medicion: MedicionCalidadLacteaResponse): void {
    if (!this.puedeCorregirCalidad) {
      this.notification.warning('Solo coordinación de calidad puede corregir mediciones registradas.');
      return;
    }

    if (medicion.tipoMedicion === 'TANDA' && this.tandasCerradas) {
      this.notification.warning('Las tandas están cerradas. Reabra las tandas si necesita corregir una tanda.');
      return;
    }

    this.idMedicionEditando = medicion.id;

    this.formulario = {
      tipoMedicion: medicion.tipoMedicion,
      idEjecucionBatch: medicion.tipoMedicion === 'BACHE'
        ? (medicion.idEjecucionBatch || 0)
        : 0,
      referencia: medicion.referencia,
      brix: medicion.brix ?? null,
      ph: medicion.ph ?? null,
      observaciones: medicion.observaciones || ''
    };

    this.pestanaActiva = 'rapida';
    this.cdr.detectChanges();
  }

  async eliminarMedicion(medicion: MedicionCalidadLacteaResponse): Promise<void> {
    if (!this.puedeEliminarCalidad) {
      this.notification.warning('Solo coordinación de calidad puede eliminar mediciones.');
      return;
    }

    if (medicion.tipoMedicion === 'TANDA' && this.tandasCerradas) {
      this.notification.warning('Las tandas están cerradas. Reabra las tandas si necesita eliminar una tanda.');
      return;
    }

    const confirmado = await this.notification.confirm({
      title: 'Eliminar medición',
      text: `¿Desea eliminar la medición ${medicion.referencia}? Esta acción quedará en auditoría y no se puede deshacer.`,
      confirmText: 'Sí, eliminar',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    this.medicionService.eliminar(medicion.id).subscribe({
      next: () => {
        this.ngZone.run(() => {
          this.mediciones = this.mediciones.filter(m =>
            Number(m.id) !== Number(medicion.id)
          );

          if (this.idMedicionEditando === medicion.id) {
            this.limpiarFormulario();
          }

          if (medicion.tipoMedicion === 'TANDA' && this.formulario.tipoMedicion === 'TANDA') {
            this.prepararNuevaTanda();
          }

          this.notification.toast('Medición eliminada.');
          this.cdr.detectChanges();
        });
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo eliminar la medición.');
      }
    });
  }

  async alternarEstadoTandas(): Promise<void> {
    if (!this.puedeCerrarTandasCalidad) {
      this.notification.warning('Solo coordinación de calidad puede cerrar o reabrir tandas.');
      return;
    }

    if (!this.idOrdenSeleccionada) {
      this.notification.warning('Debe seleccionar una orden de producción.');
      return;
    }

    if (this.totalTandasRegistradas === 0) {
      this.notification.warning('Debe registrar al menos una tanda antes de cerrar el registro de tandas.');
      return;
    }

    if (this.tandasCerradas) {
      await this.confirmarReaperturaTandas();
      return;
    }

    await this.confirmarCierreTandas();
  }

  private async confirmarCierreTandas(): Promise<void> {
    const confirmado = await this.notification.confirm({
      title: 'Cerrar registro de tandas',
      text: 'Después de cerrar, las auxiliares no podrán agregar, editar ni eliminar tandas. Solo coordinación de calidad podrá reabrirlas. ¿Desea continuar?',
      confirmText: 'Sí, cerrar tandas',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    const idUsuarioCierre = this.authService.getIdUsuario();

    if (!idUsuarioCierre) {
      this.notification.warning('No se pudo identificar el usuario autenticado.');
      return;
    }

    this.cerrandoTandas = true;

    this.ordenService.cerrarTandas(this.idOrdenSeleccionada, {
      idUsuarioCierre,
      observaciones: `Registro cerrado por coordinación de calidad con ${this.medicionesTanda.length} tanda(s) registradas.`
    }).subscribe({
      next: (ordenActualizada) => {
        this.ngZone.run(() => {
          this.actualizarOrdenLocal(ordenActualizada);

          if (this.formulario.tipoMedicion === 'TANDA') {
            this.formulario.tipoMedicion = 'BACHE';
            this.limpiarFormulario();
          }

          this.notification.toast('Registro de tandas cerrado.');
          this.cerrandoTandas = false;
          this.cdr.detectChanges();
        });
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo cerrar el registro de tandas.');
        this.cerrandoTandas = false;
      },
      complete: () => this.cerrandoTandas = false
    });
  }

  private async confirmarReaperturaTandas(): Promise<void> {
    const confirmado = await this.notification.confirm({
      title: 'Reabrir registro de tandas',
      text: 'Al reabrir, se podrán agregar, editar o eliminar tandas nuevamente. Esta acción debe usarse solo para correcciones autorizadas. ¿Desea continuar?',
      confirmText: 'Sí, reabrir',
      cancelText: 'Cancelar',
      icon: 'question'
    });

    if (!confirmado) return;

    this.reabriendoTandas = true;

    this.ordenService.reabrirTandas(this.idOrdenSeleccionada).subscribe({
      next: (ordenActualizada) => {
        this.ngZone.run(() => {
          this.actualizarOrdenLocal(ordenActualizada);
          this.notification.toast('Registro de tandas reabierto.');
          this.reabriendoTandas = false;
          this.cdr.detectChanges();
        });
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo reabrir el registro de tandas.');
        this.reabriendoTandas = false;
      },
      complete: () => this.reabriendoTandas = false
    });
  }

  registrarProceso(): void {
    if (!this.puedeRegistrarCalidad) {
      this.notification.warning('No tiene permisos para registrar controles de proceso.');
      return;
    }

    if (this.idProcesoEditando && !this.puedeCorregirCalidad) {
      this.notification.warning('Solo coordinación de calidad puede corregir controles de proceso.');
      return;
    }

    const idRealizadoPor = this.authService.getIdUsuario();

    if (!this.validarBase(idRealizadoPor)) return;

    if (!this.idProcesoEditando && this.procesoTodosBatchesRegistrados) {
      this.notification.warning('Todos los batches de esta orden ya tienen control de proceso registrado.');
      return;
    }

    this.sincronizarDatosBatchProceso();

    if (!this.validarFormularioProceso()) return;

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
        this.notification.toast(
          this.idProcesoEditando
            ? 'Control de proceso actualizado.'
            : 'Control de proceso registrado.'
        );

        this.idProcesoEditando = null;
        this.controlProcesoDetalle = null;
        this.cargarDatosOrden(false);
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo registrar el control de proceso.');
        this.guardando = false;
      },
      complete: () => this.guardando = false
    });
  }

  registrarPeso(): void {
    if (!this.puedeRegistrarCalidad) {
      this.notification.warning('No tiene permisos para registrar controles de peso.');
      return;
    }

    if (this.idPesoEditando && !this.puedeCorregirCalidad) {
      this.notification.warning('Solo coordinación de calidad puede corregir controles de peso.');
      return;
    }

    const idRealizadoPor = this.authService.getIdUsuario();

    if (!this.validarBase(idRealizadoPor)) return;

    this.pesoForm.lote = this.generarLotePesoAutomatico();

    if (!this.validarFormularioPeso()) return;

    if (this.controlPesoDuplicadoLocal()) {
      this.notification.warning('Ya existe un control de peso para esta misma tanda, presentación/SKU y rango de batches. Use editar si necesita corregirlo.');
      return;
    }

    const muestras = this.pesoForm.muestras
      .filter(m => m.pesoNeto !== null && m.pesoNeto !== undefined && Number(m.pesoNeto) > 0)
      .map(m => ({
        numeroMuestra: m.numeroMuestra,
        pesoBruto: m.pesoBruto,
        tara: m.tara,
        pesoNeto: Number(m.pesoNeto)
      }));

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
        this.notification.toast(
          this.idPesoEditando
            ? 'Control de peso actualizado.'
            : 'Control de peso registrado.'
        );

        this.idPesoEditando = null;
        this.pesoForm = this.crearPesoForm();
        this.autocompletarPesoDesdeOrden();
        this.cargarDatosOrden(false);
      },
      error: err => {
        this.notification.error(err.error?.message || 'No se pudo registrar el control de peso.');
        this.guardando = false;
      },
      complete: () => this.guardando = false
    });
  }

  onCambioBatchProceso(idBatch?: number): void {
    if (idBatch !== undefined && idBatch !== null) {
      this.procesoForm.idEjecucionBatch = Number(idBatch);
    }

    this.sincronizarDatosBatchProceso();
  }

  onCambioTandaPeso(): void {
    this.pesoForm.lote = this.generarLotePesoAutomatico();
  }

  onCambioSkuPeso(idSku?: number | null): void {
    if (!idSku) {
      this.pesoForm.idSku = null;
      this.pesoForm.presentacion = '';
      this.pesoForm.marca = '';
      return;
    }

    const sku = this.obtenerSkuOrden(idSku);

    if (!sku) {
      return;
    }

    this.pesoForm.idSku = Number(sku.idSku);
    this.pesoForm.producto = this.obtenerOrdenSeleccionada()?.nombreProducto || this.pesoForm.producto;
    this.pesoForm.presentacion = this.obtenerPresentacionSku(sku);
    this.pesoForm.marca = this.obtenerMarcaSku(sku);
  }

  onCambioRangoBatchesPeso(rango: string): void {
    this.pesoForm.rangoBatches = rango;
  }

  editarProceso(control: ControlCalidadProcesoResponse): void {
    if (!this.puedeCorregirCalidad) {
      this.notification.warning('Solo coordinación de calidad puede corregir controles de proceso.');
      return;
    }

    this.controlProcesoDetalle = null;
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

    if (!this.procesoForm.lote?.trim()) {
      this.sincronizarDatosBatchProceso();
    }

    this.pestanaActiva = 'proceso';
    this.cdr.detectChanges();
  }

  async eliminarProceso(control: ControlCalidadProcesoResponse): Promise<void> {
    if (!this.puedeEliminarCalidad) {
      this.notification.warning('Solo coordinación de calidad puede eliminar controles de proceso.');
      return;
    }

    const confirmado = await this.notification.confirm({
      title: 'Eliminar control de proceso',
      text: '¿Desea eliminar este control de proceso? Esta acción quedará en auditoría y no se puede deshacer.',
      confirmText: 'Sí, eliminar',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    this.controlCalidadService.eliminarProceso(control.id).subscribe({
      next: () => {
        this.ngZone.run(() => {
          this.controlesProceso = this.controlesProceso.filter(c =>
            Number(c.id) !== Number(control.id)
          );

          if (this.idProcesoEditando === control.id) {
            this.cancelarEdicionProceso();
          }

          if (this.controlProcesoDetalle?.id === control.id) {
            this.controlProcesoDetalle = null;
          }

          this.notification.toast('Control de proceso eliminado.');
          this.reiniciarProcesoFormConSiguienteBatch();
          this.cdr.detectChanges();
        });
      },
      error: err => this.notification.error(err.error?.message || 'No se pudo eliminar el control de proceso.')
    });
  }

  editarPeso(control: ControlPesoProductoResponse): void {
    if (!this.puedeCorregirCalidad) {
      this.notification.warning('Solo coordinación de calidad puede corregir controles de peso.');
      return;
    }

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

    if (this.pesoForm.idSku && !this.pesoForm.marca) {
      const sku = this.obtenerSkuOrden(this.pesoForm.idSku);
      if (sku) {
        this.pesoForm.marca = this.obtenerMarcaSku(sku);
      }
    }

    this.pestanaActiva = 'peso';
    this.cdr.detectChanges();
  }

  async eliminarPeso(control: ControlPesoProductoResponse): Promise<void> {
    if (!this.puedeEliminarCalidad) {
      this.notification.warning('Solo coordinación de calidad puede eliminar controles de peso.');
      return;
    }

    const confirmado = await this.notification.confirm({
      title: 'Eliminar control de peso',
      text: '¿Desea eliminar este control de peso? Esta acción quedará en auditoría y no se puede deshacer.',
      confirmText: 'Sí, eliminar',
      cancelText: 'Cancelar',
      icon: 'warning'
    });

    if (!confirmado) return;

    this.controlCalidadService.eliminarPeso(control.id).subscribe({
      next: () => {
        this.ngZone.run(() => {
          this.controlesPeso = this.controlesPeso.filter(c =>
            Number(c.id) !== Number(control.id)
          );

          if (this.idPesoEditando === control.id) {
            this.cancelarEdicionPeso();
          }

          this.notification.toast('Control de peso eliminado.');
          this.cdr.detectChanges();
        });
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
    return this.ordenes.find(o => Number(o.id) === Number(this.idOrdenSeleccionada));
  }

  obtenerBatch(idBatch?: number | null): EjecucionBatch | undefined {
    return this.batches.find(b => Number(b.id) === Number(idBatch));
  }

  obtenerSkuOrden(idSku?: number | null): ProgramacionSkuResponse | undefined {
    const orden = this.obtenerOrdenSeleccionada();

    if (!orden?.skus?.length || !idSku) {
      return undefined;
    }

    return orden.skus.find(sku => Number(sku.idSku) === Number(idSku));
  }

  get puedeRegistrarCalidad(): boolean {
    return this.authService.canWriteCalidad();
  }

  get puedeCorregirCalidad(): boolean {
    return this.authService.isAdmin() || this.authService.isCoordinadorCalidad();
  }

  get puedeEliminarCalidad(): boolean {
    return this.authService.isAdmin() || this.authService.isCoordinadorCalidad();
  }

  get puedeCerrarTandasCalidad(): boolean {
    return this.authService.canCloseTandasCalidad();
  }

  get puedeVerificarCalidad(): boolean {
    return this.authService.canVerifyCalidad();
  }

  get ordenesFiltradasCalidad(): OrdenProduccionResponse[] {
    const busqueda = this.normalizarTexto(this.busquedaOrdenCalidad);

    return this.ordenes.filter(orden => {
      const textoOrden = this.normalizarTexto([
        orden.id,
        orden.nombreProducto,
        orden.fechaProduccion,
        orden.estado,
        (orden as any).numeroOrden
      ].join(' '));

      const coincideBusqueda = !busqueda || textoOrden.includes(busqueda);
      const coincideFecha = !this.filtroFechaCalidad || orden.fechaProduccion === this.filtroFechaCalidad;

      const estado = this.estadoCalidadOrden(orden);
      const coincideEstado = this.filtroEstadoCalidad === 'TODOS' || estado === this.filtroEstadoCalidad;

      return coincideBusqueda && coincideFecha && coincideEstado;
    });
  }

  get ordenDetalleSeleccionada(): OrdenProduccionResponse | undefined {
    return this.obtenerOrdenSeleccionada();
  }

  get totalOrdenesCalidad(): number {
    return this.ordenes.length;
  }

  get totalOrdenesFiltradasCalidad(): number {
    return this.ordenesFiltradasCalidad.length;
  }

  get totalOrdenesCalidadTandasCerradas(): number {
    return this.ordenes.filter(orden => Boolean(orden.tandasCerradas)).length;
  }

  get totalOrdenesCalidadEnEjecucion(): number {
    return this.ordenes.filter(orden => orden.estado === 'EN_EJECUCION').length;
  }

  get totalOrdenesCalidadCompletasEstimadas(): number {
    return this.ordenes.filter(orden => this.estadoCalidadOrden(orden) === 'COMPLETA').length;
  }

  get medicionesRapidasBatch(): MedicionCalidadLacteaResponse[] {
    return this.mediciones.filter(m =>
      m.tipoMedicion === 'BACHE' &&
      m.idEjecucionBatch !== null &&
      m.idEjecucionBatch !== undefined
    );
  }

  get medicionesBatch(): MedicionCalidadLacteaResponse[] {
    return this.mediciones
      .filter(m => m.tipoMedicion === 'BACHE')
      .sort((a, b) => this.extraerNumeroReferencia(a.referencia, 'B.') - this.extraerNumeroReferencia(b.referencia, 'B.'));
  }

  get medicionMezcla(): MedicionCalidadLacteaResponse | null {
    return this.mediciones.find(m => m.tipoMedicion === 'MEZCLA') || null;
  }

  get medicionesTanda(): MedicionCalidadLacteaResponse[] {
    return this.mediciones
      .filter(m => m.tipoMedicion === 'TANDA')
      .sort((a, b) => this.extraerNumeroReferencia(a.referencia, 'Tanda ') - this.extraerNumeroReferencia(b.referencia, 'Tanda '));
  }

  get totalBatches(): number {
    return this.batches.length;
  }

  get totalBatchesMedidos(): number {
    return this.medicionesBatch.length;
  }

  get mezclaRegistrada(): boolean {
    return this.medicionMezcla !== null;
  }

  get totalTandasRegistradas(): number {
    return this.medicionesTanda.length;
  }

  get productoSeleccionadoEsLecheCondensada(): boolean {
    return this.esLecheCondensada(this.obtenerNombreProductoOrdenSeleccionada());
  }

  get etiquetaRequisitoPhRapido(): string {
    return this.medicionRapidaRequierePh()
      ? 'pH obligatorio'
      : 'pH opcional para leche condensada';
  }

  get referenciaActualEsTanda(): boolean {
    return this.formulario.tipoMedicion === 'TANDA';
  }

  get textoBotonMedicionRapida(): string {
    if (this.idMedicionEditando) {
      return 'ACTUALIZAR MEDICIÓN';
    }

    if (this.formulario.tipoMedicion === 'TANDA') {
      return 'REGISTRAR TANDA';
    }

    return 'REGISTRAR MEDICIÓN';
  }

  get tandasCerradas(): boolean {
    return Boolean(this.obtenerOrdenSeleccionada()?.tandasCerradas);
  }

  get estadoTandasTexto(): string {
    return this.tandasCerradas ? 'Tandas cerradas' : 'Tandas abiertas';
  }

  get puedeGestionarEstadoTandas(): boolean {
    return this.puedeCerrarTandasCalidad
      && this.totalTandasRegistradas > 0
      && !this.cerrandoTandas
      && !this.reabriendoTandas;
  }

  get textoBotonEstadoTandas(): string {
    if (this.cerrandoTandas) {
      return 'Cerrando tandas...';
    }

    if (this.reabriendoTandas) {
      return 'Reabriendo tandas...';
    }

    return this.tandasCerradas ? 'Reabrir tandas' : 'Cerrar registro de tandas';
  }

  get claseBotonEstadoTandas(): string {
    return this.tandasCerradas
      ? 'bg-amber-500 hover:bg-amber-600'
      : 'bg-emerald-600 hover:bg-emerald-700';
  }

  get idsBatchesConControlProceso(): Set<number> {
    return new Set(
      this.controlesProceso
        .map(control => Number(control.idEjecucionBatch))
        .filter(id => !Number.isNaN(id) && id > 0)
    );
  }

  get batchesDisponiblesParaProceso(): EjecucionBatch[] {
    if (this.idProcesoEditando) {
      return this.batches;
    }

    const idsRegistrados = this.idsBatchesConControlProceso;
    return this.batches.filter(batch => !idsRegistrados.has(Number(batch.id)));
  }

  get totalBatchesConControlProceso(): number {
    return this.idsBatchesConControlProceso.size;
  }

  get procesoTodosBatchesRegistrados(): boolean {
    return this.batches.length > 0 && this.batchesDisponiblesParaProceso.length === 0;
  }

  get skusOrden(): ProgramacionSkuResponse[] {
    return this.obtenerOrdenSeleccionada()?.skus || [];
  }

  get opcionesRangoBatchesPeso(): string[] {
    if (!this.batches.length) {
      return [];
    }

    const batchesOrdenados = [...this.batches].sort((a, b) => Number(a.numeroBatch) - Number(b.numeroBatch));
    const primero = batchesOrdenados[0];
    const ultimo = batchesOrdenados[batchesOrdenados.length - 1];

    const opciones = [
      `B${primero.numeroBatch}-B${ultimo.numeroBatch}`,
      ...batchesOrdenados.map(batch => `B${batch.numeroBatch}`)
    ];

    if (batchesOrdenados.length > 1) {
      for (let i = 0; i < batchesOrdenados.length - 1; i++) {
        opciones.push(`B${batchesOrdenados[i].numeroBatch}-B${batchesOrdenados[i + 1].numeroBatch}`);
      }
    }

    return this.unicos(opciones);
  }

  get controlesPesoAgrupados(): GrupoPesoProducto[] {
    const grupos = new Map<string, ControlPesoProductoResponse[]>();

    this.controlesPeso.forEach(control => {
      const llave = control.numeroTanda || 'Sin tanda';
      const controles = grupos.get(llave) || [];
      controles.push(control);
      grupos.set(llave, controles);
    });

    return Array.from(grupos.entries()).map(([tanda, controles]) => ({
      tanda,
      controles: controles.sort((a, b) =>
        String(a.presentacion || '').localeCompare(String(b.presentacion || ''))
      )
    }));
  }

  get resumenWhatsappCalidad(): string {
    const orden = this.obtenerOrdenSeleccionada();
    const producto = orden?.nombreProducto || 'Producto';

    const lineas: string[] = [];
    lineas.push(producto);
    lineas.push('');

    if (this.medicionesBatch.length) {
      lineas.push('Batches:');
      this.medicionesBatch.forEach(medicion => {
        lineas.push(`${medicion.referencia}: Brix ${medicion.brix ?? '-'}${medicion.ph ? ` - pH ${medicion.ph}` : ''}`);
      });
      lineas.push('');
    }

    if (this.medicionMezcla) {
      lineas.push(`Mezcla: Brix ${this.medicionMezcla.brix ?? '-'}${this.medicionMezcla.ph ? ` - pH ${this.medicionMezcla.ph}` : ''}`);
      lineas.push('');
    }

    if (this.medicionesTanda.length) {
      lineas.push('Tandas:');
      this.medicionesTanda.forEach(tanda => {
        lineas.push(`${tanda.referencia}: Brix ${tanda.brix ?? '-'}${tanda.ph ? ` - pH ${tanda.ph}` : ''}`);
      });
      lineas.push('');
    }

    if (this.controlesPeso.length) {
      lineas.push('Peso producto terminado:');
      this.controlesPeso.forEach(control => {
        lineas.push(
          `${control.numeroTanda || '-'} | ${control.marca || '-'} ${control.presentacion || '-'} | ${control.rangoBatches || '-'} | Prom. ${control.pesoNetoPromedio ?? '-'} | ${control.liberado ? 'Liberado' : 'Retenido'}`
        );
      });
    }

    return lineas.join('\n').trim();
  }

  batchYaMedido(idBatch: number): boolean {
    return this.medicionesRapidasBatch.some(m =>
      Number(m.idEjecucionBatch) === Number(idBatch) &&
      Number(m.id) !== Number(this.idMedicionEditando)
    );
  }

  get batchesDisponiblesParaMedicion(): EjecucionBatch[] {
    if (this.idMedicionEditando) {
      return this.batches;
    }

    const idsMedidos = new Set(
      this.medicionesRapidasBatch
        .map(m => Number(m.idEjecucionBatch))
        .filter(id => !Number.isNaN(id))
    );

    return this.batches.filter(batch => !idsMedidos.has(Number(batch.id)));
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

  async copiarResumenWhatsapp(): Promise<void> {
    const texto = this.resumenWhatsappCalidad;

    if (!texto) {
      this.notification.warning('No hay información suficiente para generar el resumen.');
      return;
    }

    try {
      await navigator.clipboard.writeText(texto);
      this.notification.toast('Resumen copiado para WhatsApp.');
    } catch {
      this.notification.warning('No se pudo copiar automáticamente. Seleccione el texto y cópielo manualmente.');
      this.resumenWhatsappVisible = true;
    }
  }

  estadoCalidadOrden(orden: OrdenProduccionResponse): EstadoCalidadOrden {
    const esOrdenSeleccionada = Number(orden.id) === Number(this.idOrdenSeleccionada);

    if (!esOrdenSeleccionada) {
      if (Boolean(orden.tandasCerradas)) {
        return 'TANDAS_CERRADAS';
      }

      if (orden.estado === 'EN_EJECUCION') {
        return 'PENDIENTE';
      }

      return 'SIN_CONSULTAR';
    }

    const batchesOk = this.totalBatches > 0 && this.totalBatchesMedidos >= this.totalBatches;
    const mezclaOk = this.mezclaRegistrada;
    const procesoOk = this.totalBatches > 0 && this.totalBatchesConControlProceso >= this.totalBatches;
    const pesoOk = this.controlesPeso.length > 0;

    if (batchesOk && mezclaOk && this.tandasCerradas && procesoOk && pesoOk) {
      return 'COMPLETA';
    }

    if (this.tandasCerradas) {
      return 'TANDAS_CERRADAS';
    }

    if (this.mediciones.length || this.controlesProceso.length || this.controlesPeso.length) {
      return 'EN_PROCESO';
    }

    return 'PENDIENTE';
  }

  textoEstadoCalidadOrden(orden: OrdenProduccionResponse): string {
    const estado = this.estadoCalidadOrden(orden);

    if (estado === 'COMPLETA') return 'Completa';
    if (estado === 'TANDAS_CERRADAS') return 'Tandas cerradas';
    if (estado === 'EN_PROCESO') return 'En proceso';
    if (estado === 'PENDIENTE') return 'Pendiente';
    return 'Por consultar';
  }

  claseEstadoCalidadOrden(orden: OrdenProduccionResponse): string {
    const estado = this.estadoCalidadOrden(orden);

    if (estado === 'COMPLETA') return 'bg-emerald-100 text-emerald-700';
    if (estado === 'TANDAS_CERRADAS') return 'bg-blue-100 text-blue-700';
    if (estado === 'EN_PROCESO') return 'bg-violet-100 text-violet-700';
    if (estado === 'PENDIENTE') return 'bg-amber-100 text-amber-700';
    return 'bg-slate-100 text-slate-500';
  }

  obtenerResumenOrdenLista(orden: OrdenProduccionResponse): string {
    const esOrdenSeleccionada = Number(orden.id) === Number(this.idOrdenSeleccionada);

    if (!esOrdenSeleccionada) {
      if (Boolean(orden.tandasCerradas)) {
        return 'Tandas cerradas. Abra el detalle para consultar mediciones.';
      }

      return 'Abra el detalle para consultar el avance de calidad.';
    }

    return `Batches ${this.totalBatchesMedidos}/${this.totalBatches || 0} · Mezcla ${this.mezclaRegistrada ? 'OK' : 'Pendiente'} · Tandas ${this.totalTandasRegistradas} · Proceso ${this.totalBatchesConControlProceso}/${this.totalBatches || 0} · Peso ${this.controlesPeso.length}`;
  }

  private validarBase(idUsuario: number): boolean {
    if (!this.idOrdenSeleccionada) {
      this.notification.warning('Debe seleccionar una orden de producción.');
      return false;
    }

    if (!idUsuario) {
      this.notification.warning('No se pudo identificar el usuario autenticado.');
      return false;
    }

    return true;
  }

  private validarMedicionRapidaBase(idUsuarioCalidad: number): boolean {
    if (!this.idOrdenSeleccionada) {
      this.notification.warning('Debe seleccionar una orden de producción.');
      return false;
    }

    if (!idUsuarioCalidad) {
      this.notification.warning('No se pudo identificar el usuario autenticado.');
      return false;
    }

    if (!this.formulario.tipoMedicion) {
      this.notification.warning('Debe seleccionar el tipo de medición.');
      return false;
    }

    if (this.formulario.tipoMedicion === 'TANDA' && this.tandasCerradas) {
      this.notification.warning('Las tandas de esta orden ya están cerradas. No se pueden registrar nuevas tandas.');
      return false;
    }

    return true;
  }

  private validarMedicionRapidaFormulario(): boolean {
    if (this.formulario.tipoMedicion === 'BACHE') {
      if (!this.formulario.idEjecucionBatch) {
        this.notification.warning('Debe seleccionar un batch.');
        return false;
      }

      const batch = this.obtenerBatch(this.formulario.idEjecucionBatch);

      if (!batch) {
        this.notification.warning('El batch seleccionado no existe o no pertenece a la orden actual.');
        return false;
      }
    }

    if (this.formulario.tipoMedicion !== 'BACHE' && this.formulario.idEjecucionBatch) {
      this.formulario.idEjecucionBatch = 0;
    }

    if (!this.formulario.referencia?.trim()) {
      this.notification.warning('No se pudo generar la referencia automática de la medición.');
      return false;
    }

    if (this.formulario.tipoMedicion === 'BACHE' && !this.formulario.referencia.startsWith('B.')) {
      this.notification.warning('La referencia automática del batch no es válida.');
      return false;
    }

    if (this.formulario.tipoMedicion === 'MEZCLA' && this.formulario.referencia !== 'Mezcla') {
      this.notification.warning('La referencia automática de mezcla no es válida.');
      return false;
    }

    if (this.formulario.tipoMedicion === 'TANDA' && !this.formulario.referencia.startsWith('Tanda ')) {
      this.notification.warning('La referencia automática de tanda no es válida.');
      return false;
    }

    if (!this.idMedicionEditando && this.formulario.tipoMedicion === 'MEZCLA' && this.mezclaRegistrada) {
      this.notification.warning('Esta orden ya tiene medición de mezcla registrada. Use editar si necesita corregirla.');
      return false;
    }

    if (this.formulario.brix === null || this.formulario.brix === undefined) {
      this.notification.warning(`Debe registrar el Brix de ${this.obtenerNombreTipoMedicion()}.`);
      return false;
    }

    if (Number.isNaN(Number(this.formulario.brix))) {
      this.notification.warning('El Brix debe ser un valor numérico válido.');
      return false;
    }

    if (Number(this.formulario.brix) < 0) {
      this.notification.warning('El Brix no puede ser negativo.');
      return false;
    }

    if (Number(this.formulario.brix) > 100) {
      this.notification.warning('El Brix no puede ser mayor a 100. Revise el valor digitado.');
      return false;
    }

    if (this.medicionRapidaRequierePh()) {
      if (this.formulario.ph === null || this.formulario.ph === undefined) {
        this.notification.warning(`Debe registrar el pH de ${this.obtenerNombreTipoMedicion()}.`);
        return false;
      }
    }

    if (this.formulario.ph !== null && this.formulario.ph !== undefined) {
      if (Number.isNaN(Number(this.formulario.ph))) {
        this.notification.warning('El pH debe ser un valor numérico válido.');
        return false;
      }

      if (Number(this.formulario.ph) < 0) {
        this.notification.warning('El pH no puede ser negativo.');
        return false;
      }

      if (Number(this.formulario.ph) > 14) {
        this.notification.warning('El pH no puede ser mayor a 14. Revise el valor digitado.');
        return false;
      }
    }

    if (
      !this.idMedicionEditando &&
      this.formulario.tipoMedicion === 'BACHE' &&
      this.formulario.idEjecucionBatch &&
      this.batchYaMedido(this.formulario.idEjecucionBatch)
    ) {
      this.notification.warning('Este batch ya tiene medición registrada. Use editar si necesita corregirla.');
      return false;
    }

    return true;
  }

  private obtenerNombreTipoMedicion(): string {
    if (this.formulario.tipoMedicion === 'BACHE') {
      return 'batch';
    }

    if (this.formulario.tipoMedicion === 'MEZCLA') {
      return 'la mezcla';
    }

    if (this.formulario.tipoMedicion === 'TANDA') {
      return 'la tanda';
    }

    return 'la medición';
  }

  private medicionRapidaRequierePh(): boolean {
    return !this.esLecheCondensada(this.obtenerNombreProductoOrdenSeleccionada());
  }

  private obtenerNombreProductoOrdenSeleccionada(): string {
    const orden = this.obtenerOrdenSeleccionada();
    return orden?.nombreProducto || '';
  }

  private esLecheCondensada(nombreProducto: string): boolean {
    return this.normalizarTexto(nombreProducto).includes('LECHE CONDENSADA');
  }

  private validarFormularioProceso(): boolean {
    if (!this.procesoForm.idEjecucionBatch) {
      this.notification.warning('Debe seleccionar el batch / marmita.');
      return false;
    }

    if (!this.idProcesoEditando && this.batchProcesoYaRegistrado(this.procesoForm.idEjecucionBatch)) {
      this.notification.warning('Este batch ya tiene control de proceso registrado. El sistema seleccionará el siguiente batch pendiente.');
      this.reiniciarProcesoFormConSiguienteBatch();
      return false;
    }

    if (!this.procesoForm.producto?.trim()) {
      this.notification.warning('Debe registrar el producto.');
      return false;
    }

    if (!this.procesoForm.lote?.trim()) {
      this.notification.warning('No se pudo generar el lote del proceso. Revise la orden y el batch seleccionados.');
      return false;
    }

    if (this.procesoForm.phLeche === null || this.procesoForm.phLeche === undefined) {
      this.notification.warning('Debe registrar el pH de la leche.');
      return false;
    }

    if (this.procesoForm.acidezLeche === null || this.procesoForm.acidezLeche === undefined) {
      this.notification.warning('Debe registrar la acidez de la leche.');
      return false;
    }

    if (this.procesoForm.densidadLeche === null || this.procesoForm.densidadLeche === undefined) {
      this.notification.warning('Debe registrar la densidad de la leche.');
      return false;
    }

    if (this.procesoForm.grasaLeche === null || this.procesoForm.grasaLeche === undefined) {
      this.notification.warning('Debe registrar la grasa de la leche.');
      return false;
    }

    if (!this.procesoForm.horaInicioHidrolisis) {
      this.notification.warning('Debe registrar la hora de inicio de hidrólisis.');
      return false;
    }

    if (this.procesoForm.phInicial === null || this.procesoForm.phInicial === undefined) {
      this.notification.warning('Debe registrar el pH inicial.');
      return false;
    }

    if (!this.procesoForm.horaFinHidrolisis) {
      this.notification.warning('Debe registrar la hora de fin de hidrólisis.');
      return false;
    }

    if (this.procesoForm.phFinal === null || this.procesoForm.phFinal === undefined) {
      this.notification.warning('Debe registrar el pH final.');
      return false;
    }

    if (this.procesoForm.brixInicial === null || this.procesoForm.brixInicial === undefined) {
      this.notification.warning('Debe registrar el Brix inicial.');
      return false;
    }

    if (this.procesoForm.brixFinal === null || this.procesoForm.brixFinal === undefined) {
      this.notification.warning('Debe registrar el Brix final.');
      return false;
    }

    if (this.procesoForm.temperaturaCoccion === null || this.procesoForm.temperaturaCoccion === undefined) {
      this.notification.warning('Debe registrar la temperatura de cocción.');
      return false;
    }

    if (this.procesoForm.temperaturaEnvasado === null || this.procesoForm.temperaturaEnvasado === undefined) {
      this.notification.warning('Debe registrar la temperatura de envasado.');
      return false;
    }

    if (!this.procesoForm.colorVisual?.trim()) {
      this.notification.warning('Debe seleccionar el color visual.');
      return false;
    }

    if (!this.procesoForm.saborVisual?.trim()) {
      this.notification.warning('Debe seleccionar el sabor.');
      return false;
    }

    if (!this.procesoForm.texturaVisual?.trim()) {
      this.notification.warning('Debe seleccionar la textura.');
      return false;
    }

    if (!this.procesoForm.fechaVencimiento) {
      this.notification.warning('Debe registrar la fecha de vencimiento.');
      return false;
    }

    if (!this.procesoForm.presentacionEnvasado?.trim()) {
      this.notification.warning('Debe registrar la presentación de envasado.');
      return false;
    }

    if (!this.procesoForm.liberado && !this.procesoForm.retenido) {
      this.notification.warning('Debe marcar si el producto queda liberado o retenido.');
      return false;
    }

    if (this.procesoForm.liberado && this.procesoForm.retenido) {
      this.notification.warning('No puede marcar Liberado y Retenido al mismo tiempo.');
      return false;
    }

    if (this.evaluacionProcesoSugiereRetencion() && this.procesoForm.liberado) {
      this.notification.warning('La evaluación visual tiene una novedad. Revise si el producto debe quedar retenido antes de liberarlo.');
      return false;
    }

    return true;
  }

  private validarFormularioPeso(): boolean {
    if (!this.pesoForm.fechaControl) {
      this.notification.warning('Debe registrar la fecha del control de peso.');
      return false;
    }

    if (!this.pesoForm.producto?.trim()) {
      this.notification.warning('Debe registrar el producto.');
      return false;
    }

    if (!this.pesoForm.marca?.trim()) {
      this.notification.warning('No se pudo identificar la marca desde el SKU seleccionado.');
      return false;
    }

    if (!this.pesoForm.idSku) {
      this.notification.warning('Debe seleccionar la presentación / SKU.');
      return false;
    }

    if (!this.pesoForm.numeroTanda?.trim()) {
      this.notification.warning('Debe seleccionar una tanda registrada.');
      return false;
    }

    if (!this.tandaExiste(this.pesoForm.numeroTanda)) {
      this.notification.warning('La tanda seleccionada no existe. Primero registre la tanda en Brix / pH rápido.');
      return false;
    }

    if (!this.pesoForm.lote?.trim()) {
      this.notification.warning('No se pudo generar el lote del producto terminado.');
      return false;
    }

    if (!this.pesoForm.fechaVencimiento) {
      this.notification.warning('Debe registrar la fecha de vencimiento.');
      return false;
    }

    if (!this.pesoForm.presentacion?.trim()) {
      this.notification.warning('Debe seleccionar la presentación.');
      return false;
    }

    if (!this.pesoForm.rangoBatches?.trim()) {
      this.notification.warning('Debe seleccionar el rango de batches.');
      return false;
    }

    const muestrasValidas = this.pesoForm.muestras.filter(m =>
      m.pesoNeto !== null &&
      m.pesoNeto !== undefined &&
      Number(m.pesoNeto) > 0
    );

    if (muestrasValidas.length < 10) {
      this.notification.warning('Debe registrar el peso neto de las 10 muestras.');
      return false;
    }

    if (
      this.pesoForm.pesoNetoPromedio === null ||
      this.pesoForm.pesoNetoPromedio === undefined ||
      Number(this.pesoForm.pesoNetoPromedio) <= 0
    ) {
      this.notification.warning('Debe registrar el promedio de peso neto.');
      return false;
    }

    if (
      this.pesoForm.cantidadPorCaja === null ||
      this.pesoForm.cantidadPorCaja === undefined ||
      Number(this.pesoForm.cantidadPorCaja) <= 0
    ) {
      this.notification.warning('Debe registrar la cantidad por caja.');
      return false;
    }

    if (!this.pesoForm.liberado && !this.pesoForm.retenido) {
      this.notification.warning('Debe marcar si el producto terminado queda liberado o retenido.');
      return false;
    }

    if (this.pesoForm.liberado && this.pesoForm.retenido) {
      this.notification.warning('No puede marcar Liberado y Retenido al mismo tiempo.');
      return false;
    }

    if (
      (!this.pesoForm.aparienciaOk || !this.pesoForm.etiquetadoOk || !this.pesoForm.tapadoOk) &&
      this.pesoForm.liberado
    ) {
      this.notification.warning('No puede liberar producto terminado si apariencia, etiquetado o tapado no están conformes.');
      return false;
    }

    return true;
  }

  private tandaExiste(referenciaTanda: string): boolean {
    return this.medicionesTanda.some(tanda => tanda.referencia === referenciaTanda);
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

  private reiniciarProcesoFormConSiguienteBatch(): void {
    this.procesoForm = this.crearProcesoForm();

    const siguienteBatch = this.batchesDisponiblesParaProceso[0];

    if (siguienteBatch) {
      this.procesoForm.idEjecucionBatch = Number(siguienteBatch.id);
      this.sincronizarDatosBatchProceso();
      return;
    }

    this.procesoForm.idEjecucionBatch = 0;
    this.procesoForm.lote = '';
    this.procesoForm.numeroMarmita = null;
  }

  private autocompletarPesoDesdeOrden(): void {
    const orden = this.obtenerOrdenSeleccionada();

    if (!orden) {
      return;
    }

    this.pesoForm.producto = orden.nombreProducto || this.pesoForm.producto;
    this.pesoForm.lote = this.generarLotePesoAutomatico();

    const primerSku = this.skusOrden[0];

    if (primerSku && !this.pesoForm.idSku) {
      this.pesoForm.idSku = Number(primerSku.idSku);
      this.pesoForm.presentacion = this.obtenerPresentacionSku(primerSku);
      this.pesoForm.marca = this.obtenerMarcaSku(primerSku);
    }

    if (!this.pesoForm.rangoBatches && this.opcionesRangoBatchesPeso.length) {
      this.pesoForm.rangoBatches = this.opcionesRangoBatchesPeso[0];
    }
  }

  private sincronizarDatosBatchProceso(): void {
    const batch = this.obtenerBatch(this.procesoForm.idEjecucionBatch);

    if (!batch) {
      this.procesoForm.numeroMarmita = null;
      this.procesoForm.lote = '';
      return;
    }

    this.procesoForm.numeroMarmita = this.obtenerNumeroMarmita(batch);
    this.procesoForm.lote = this.generarLoteProcesoAutomatico();
  }

  private obtenerNumeroMarmita(batch: EjecucionBatch): number | null {
    const nombreMarmita = String(batch.nombreMarmita || '');
    const numeroDesdeNombre = Number(nombreMarmita.replace(/\D/g, ''));

    if (!Number.isNaN(numeroDesdeNombre) && numeroDesdeNombre > 0) {
      return numeroDesdeNombre;
    }

    if (batch.idMarmita !== null && batch.idMarmita !== undefined) {
      return Number(batch.idMarmita);
    }

    return batch.numeroBatch ?? null;
  }

  private batchProcesoYaRegistrado(idBatch: number): boolean {
    return this.controlesProceso.some(control =>
      Number(control.idEjecucionBatch) === Number(idBatch) &&
      Number(control.id) !== Number(this.idProcesoEditando)
    );
  }

  private evaluacionProcesoSugiereRetencion(): boolean {
    const valores = [
      this.procesoForm.colorVisual,
      this.procesoForm.saborVisual,
      this.procesoForm.texturaVisual
    ].map(valor => this.normalizarTexto(valor));

    return valores.some(valor => this.valoresEvaluacionNoConforme.includes(valor));
  }

  private controlPesoDuplicadoLocal(): boolean {
    if (!this.pesoForm.numeroTanda || !this.pesoForm.idSku || !this.pesoForm.rangoBatches) {
      return false;
    }

    const tanda = this.normalizarTexto(this.pesoForm.numeroTanda);
    const rango = this.normalizarTexto(this.pesoForm.rangoBatches);
    const idSku = Number(this.pesoForm.idSku);

    return this.controlesPeso.some(control =>
      Number(control.id) !== Number(this.idPesoEditando) &&
      this.normalizarTexto(control.numeroTanda || '') === tanda &&
      Number(control.idSku) === idSku &&
      this.normalizarTexto(control.rangoBatches || '') === rango
    );
  }

  private autocompletarReferencia(): void {
    if (this.idMedicionEditando) {
      return;
    }

    if (this.formulario.tipoMedicion === 'BACHE') {
      const batch = this.obtenerBatch(this.formulario.idEjecucionBatch);
      this.formulario.referencia = batch ? `B.${batch.numeroBatch}` : '';
      return;
    }

    if (this.formulario.tipoMedicion === 'MEZCLA') {
      this.formulario.idEjecucionBatch = 0;
      this.formulario.referencia = 'Mezcla';
      return;
    }

    if (this.formulario.tipoMedicion === 'TANDA') {
      this.formulario.idEjecucionBatch = 0;

      if (this.tandasCerradas) {
        this.formulario.referencia = '';
        return;
      }

      this.formulario.referencia = `Tanda ${this.siguienteNumeroTanda()}`;
    }
  }

  private siguienteNumeroTanda(): number {
    const numeros = this.mediciones
      .filter(m => m.tipoMedicion === 'TANDA')
      .map(m => this.extraerNumeroReferencia(m.referencia, 'Tanda '))
      .filter(numero => !Number.isNaN(numero) && numero !== 999999);

    if (!numeros.length) {
      return 1;
    }

    return Math.max(...numeros) + 1;
  }

  private generarLoteProcesoAutomatico(): string {
    const orden = this.obtenerOrdenSeleccionada();
    const batch = this.obtenerBatch(this.procesoForm.idEjecucionBatch);

    if (!orden || !batch) {
      return '';
    }

    const numeroOrden = this.obtenerNumeroOrdenLegible(orden);
    return `${numeroOrden}-B${batch.numeroBatch}`;
  }

  private generarLotePesoAutomatico(): string {
    const orden = this.obtenerOrdenSeleccionada();

    if (!orden || !this.pesoForm.numeroTanda?.trim()) {
      return '';
    }

    const numeroOrden = this.obtenerNumeroOrdenLegible(orden);
    return `${numeroOrden}-${this.normalizarSegmentoLote(this.pesoForm.numeroTanda)}`;
  }

  private obtenerNumeroOrdenLegible(orden: OrdenProduccionResponse): string {
    const posibleNumeroOrden = (orden as any).numeroOrden;

    if (posibleNumeroOrden && String(posibleNumeroOrden).trim()) {
      return this.normalizarSegmentoLote(String(posibleNumeroOrden).trim());
    }

    const fecha = orden.fechaProduccion
      ? orden.fechaProduccion.replaceAll('-', '')
      : new Date().toISOString().slice(0, 10).replaceAll('-', '');

    return `OP-${fecha}-${orden.id}`;
  }

  private actualizarOrdenLocal(ordenActualizada: OrdenProduccionResponse): void {
    this.ordenes = this.ordenes.map(orden =>
      Number(orden.id) === Number(ordenActualizada.id) ? ordenActualizada : orden
    );

    if (!this.ordenes.some(orden => Number(orden.id) === Number(ordenActualizada.id))) {
      this.ordenes = [ordenActualizada, ...this.ordenes];
    }
  }

  private obtenerPresentacionSku(sku: ProgramacionSkuResponse): string {
    const peso = Number(sku.pesoUnidadGr || 0);

    if (peso > 0) {
      return `${peso} g`;
    }

    return sku.descripcionSku || sku.codigoSku || 'Presentación programada';
  }

  private obtenerMarcaSku(sku: ProgramacionSkuResponse): string {
    const textoSkuNormalizado = this.normalizarTexto(`${sku.codigoSku || ''} ${sku.descripcionSku || ''}`);

    const marcasOrdenadas = [...this.marcas]
      .filter(marca => marca?.nombre)
      .sort((a, b) => b.nombre.length - a.nombre.length);

    const marcaEncontrada = marcasOrdenadas.find(marca =>
      textoSkuNormalizado.includes(this.normalizarTexto(marca.nombre))
    );

    return marcaEncontrada?.nombre || '';
  }

  private unicos(valores: string[]): string[] {
    return [...new Set(
      valores
        .map(valor => String(valor || '').trim())
        .filter(valor => Boolean(valor))
    )];
  }

  private normalizarSegmentoLote(valor: string): string {
    return String(valor)
      .trim()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, '-')
      .replace(/[^A-Za-z0-9.-]/g, '')
      .toUpperCase();
  }

  private normalizarTexto(valor: string): string {
    return String(valor || '')
      .trim()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, ' ')
      .toUpperCase();
  }

  private extraerNumeroReferencia(referencia: string, prefijo: string): number {
    const numero = Number(String(referencia || '').replace(prefijo, '').trim());
    return Number.isNaN(numero) ? 999999 : numero;
  }
}