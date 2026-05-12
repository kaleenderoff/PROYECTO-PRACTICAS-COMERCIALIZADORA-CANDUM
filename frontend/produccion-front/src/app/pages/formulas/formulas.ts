import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FormulaService } from '../../core/services/formula';
import { ProgramacionProduccionService } from '../../core/services/programacion-produccion';

@Component({
    selector: 'app-formulas',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './formulas.html',
    styleUrl: './formulas.scss'
})
export class Formulas implements OnInit {

    private formulaService = inject(FormulaService);
    private programacionService = inject(ProgramacionProduccionService);

    productos: any[] = [];
    insumos: any[] = [];
    formulas: any[] = [];
    versiones: any[] = [];

    idProducto: number | null = null;
    idFormula: number | null = null;
    idFormulaVersion: number | null = null;

    formulaSeleccionada: any | null = null;

    mensajeError = '';
    mensajeOk = '';

    nuevaFormula = {
        nombre: ''
    };

    nuevaVersion = {
        version: '',
        fechaInicioVigencia: '',
        kgBatchTotal: 0,
        reduccionEvaporacionPct: 0,
        rendimientoTeoricoPct: 0,
        brixObjetivoMin: 0,
        brixObjetivoMax: 0,
        aprobadoPor: '',
        documentoAprobacion: '',
        observacionesTecnicas: '',
        idCreadoPor: 1
    };

    nuevoDetalle = {
        idInsumo: 0,
        cantidadKg: 0,
        porcentaje: 0,
        tipoCalculo: 'PORCENTAJE_BATCH' as 'FIJO' | 'PORCENTAJE_BATCH',
        esCritico: false,
        ordenAdicion: 1
    };

    ngOnInit(): void {
        this.cargarProductos();
        this.cargarInsumos();
    }

    cargarProductos(): void {
        this.programacionService.listarProductos().subscribe({
            next: data => this.productos = data,
            error: error => this.mostrarError(error)
        });
    }

    cargarInsumos(): void {
        this.formulaService.listarInsumos().subscribe({
            next: data => this.insumos = data,
            error: error => this.mostrarError(error)
        });
    }

    onProductoChange(): void {
        this.formulas = [];
        this.versiones = [];
        this.idFormula = null;
        this.idFormulaVersion = null;
        this.formulaSeleccionada = null;
        this.limpiarMensajes();

        if (!this.idProducto) return;

        this.formulaService.listarFormulasPorProducto(this.idProducto).subscribe({
            next: data => this.formulas = data,
            error: error => this.mostrarError(error)
        });
    }

    crearFormula(): void {
        if (!this.idProducto || !this.nuevaFormula.nombre.trim()) return;

        const body = {
            idProducto: this.idProducto,
            nombre: this.nuevaFormula.nombre.trim()
        };

        this.formulaService.crearFormula(body).subscribe({
            next: () => {
                this.nuevaFormula.nombre = '';
                this.mostrarOk('Fórmula creada correctamente.');
                this.onProductoChange();
            },
            error: error => this.mostrarError(error)
        });
    }

    onFormulaChange(): void {
        this.versiones = [];
        this.idFormulaVersion = null;
        this.formulaSeleccionada = null;
        this.limpiarMensajes();

        if (!this.idFormula) return;

        this.cargarVersiones();
    }

    cargarVersiones(mantenerVersion = false): void {
        if (!this.idFormula) return;

        const versionActual = this.idFormulaVersion;

        this.formulaService.listarVersiones(this.idFormula).subscribe({
            next: data => {
                this.versiones = data;

                if (mantenerVersion && versionActual) {
                    this.idFormulaVersion = versionActual;
                    this.onVersionChange();
                }
            },
            error: error => this.mostrarError(error)
        });
    }

    crearVersion(): void {
        this.limpiarMensajes();

        if (!this.idFormula) {
            this.mensajeError = 'Seleccione una fórmula.';
            return;
        }

        if (!this.nuevaVersion.version.trim()) {
            this.mensajeError = 'Ingrese el nombre de la versión.';
            return;
        }

        this.formulaService.crearVersion(this.idFormula, this.nuevaVersion).subscribe({
            next: versionCreada => {
                this.mostrarOk('Versión creada correctamente.');

                const idNuevaVersion =
                    versionCreada.idFormulaVersion ??
                    versionCreada.id ??
                    null;

                this.nuevaVersion = {
                    version: '',
                    fechaInicioVigencia: '',
                    kgBatchTotal: 0,
                    reduccionEvaporacionPct: 0,
                    rendimientoTeoricoPct: 0,
                    brixObjetivoMin: 0,
                    brixObjetivoMax: 0,
                    aprobadoPor: '',
                    documentoAprobacion: '',
                    observacionesTecnicas: '',
                    idCreadoPor: 1
                };

                this.formulaService.listarVersiones(this.idFormula!).subscribe({
                    next: versiones => {
                        this.versiones = versiones;

                        if (idNuevaVersion) {
                            this.idFormulaVersion = idNuevaVersion;
                        } else if (versiones.length > 0) {
                            this.idFormulaVersion = versiones[0].idFormulaVersion;
                        }

                        this.onVersionChange();
                    },
                    error: error => this.mostrarError(error)
                });
            },
            error: error => this.mostrarError(error)
        });
    }

    onVersionChange(): void {
        this.formulaSeleccionada = this.versiones.find(
            v => Number(v.idFormulaVersion) === Number(this.idFormulaVersion)
        ) || null;

        this.limpiarMensajes();
        this.reiniciarDetalle();
    }

    puedeEditarFormula(): boolean {
        if (!this.formulaSeleccionada) return false;

        return !['VIGENTE', 'REEMPLAZADA', 'INACTIVA'].includes(
            this.formulaSeleccionada.estado
        );
    }

    obtenerKgBatchBase(): number {
        return Number(
            this.formulaSeleccionada?.kgBatchTotal ||
            this.nuevaVersion.kgBatchTotal ||
            0
        );
    }

    calcularPorcentaje(): void {
        const kgBatch = this.obtenerKgBatchBase();
        const cantidadKg = Number(this.nuevoDetalle.cantidadKg || 0);

        if (this.nuevoDetalle.tipoCalculo === 'PORCENTAJE_BATCH') {

            const pct = Number(this.nuevoDetalle.porcentaje || 0);

            if (kgBatch <= 0 || pct <= 0) {
                this.nuevoDetalle.cantidadKg = 0;
                return;
            }

            this.nuevoDetalle.cantidadKg = Number(
                ((pct * kgBatch) / 100).toFixed(3)
            );

            return;
        }

        if (kgBatch <= 0 || cantidadKg <= 0) {
            this.nuevoDetalle.porcentaje = 0;
            return;
        }

        this.nuevoDetalle.porcentaje = Number(
            ((cantidadKg / kgBatch) * 100).toFixed(4)
        );
    }

    onTipoCalculoChange(): void {
        this.nuevoDetalle.cantidadKg = 0;
        this.nuevoDetalle.porcentaje = 0;
    }

    calcularCantidadGramos(): number {
        return Number((Number(this.nuevoDetalle.cantidadKg || 0) * 1000).toFixed(1));
    }

    calcularGramosDetalle(detalle: any): number {
        return Number((Number(detalle.cantidadKg || 0) * 1000).toFixed(1));
    }

    calcularTotalKg(): number {
        if (!this.formulaSeleccionada?.detalles) return 0;

        return Number(
            this.formulaSeleccionada.detalles
                .reduce((total: number, d: any) => total + Number(d.cantidadKg || 0), 0)
                .toFixed(3)
        );
    }

    calcularTotalPorcentaje(): number {
        if (!this.formulaSeleccionada?.detalles) return 0;

        return Number(
            this.formulaSeleccionada.detalles
                .reduce((total: number, d: any) => total + Number(d.porcentaje || 0), 0)
                .toFixed(4)
        );
    }

    calcularTotalGramos(): number {
        return Number((this.calcularTotalKg() * 1000).toFixed(1));
    }

    agregarDetalle(): void {
        this.limpiarMensajes();

        if (!this.idFormulaVersion || !this.nuevoDetalle.idInsumo) {
            this.mensajeError = 'Seleccione un insumo.';
            return;
        }

        if (!this.puedeEditarFormula()) {
            this.mensajeError = 'No puedes modificar una versión VIGENTE. Crea una nueva versión BORRADOR.';
            return;
        }

        if (this.nuevoDetalle.tipoCalculo === 'FIJO') {

            if (Number(this.nuevoDetalle.cantidadKg) <= 0) {
                this.mensajeError = 'Ingrese una cantidad kg mayor a 0.';
                return;
            }

            this.calcularPorcentaje();

        } else {

            if (Number(this.nuevoDetalle.porcentaje) <= 0) {
                this.mensajeError = 'Ingrese un porcentaje mayor a 0.';
                return;
            }

            if (this.obtenerKgBatchBase() <= 0) {
                this.mensajeError = 'La versión no tiene kg batch definido.';
                return;
            }

            this.calcularPorcentaje();
        }

        this.formulaService.agregarDetalle(this.idFormulaVersion, this.nuevoDetalle).subscribe({
            next: formula => {
                this.formulaSeleccionada = formula;
                this.mostrarOk('Ingrediente agregado correctamente.');
                this.reiniciarDetalle();
                this.cargarVersiones(true);
            },
            error: error => this.mostrarError(error)
        });
    }

    eliminarDetalle(detalle: any): void {
        this.limpiarMensajes();

        if (!this.puedeEditarFormula()) {
            this.mensajeError = 'No puedes eliminar ingredientes de una versión VIGENTE. Crea una nueva versión BORRADOR.';
            return;
        }

        const idDetalle = detalle.idDetalle ?? detalle.id;

        if (!idDetalle) {
            this.mensajeError = 'No se encontró el ID del detalle.';
            return;
        }

        this.formulaService.eliminarDetalle(idDetalle).subscribe({
            next: () => {
                this.mostrarOk('Ingrediente eliminado correctamente.');

                if (this.formulaSeleccionada?.detalles) {
                    this.formulaSeleccionada.detalles =
                        this.formulaSeleccionada.detalles.filter((d: any) =>
                            Number(d.idDetalle ?? d.id) !== Number(idDetalle)
                        );
                }

                this.cargarVersiones(true);
            },
            error: error => this.mostrarError(error)
        });
    }

    marcarVigente(): void {
        this.limpiarMensajes();

        if (!this.idFormulaVersion) return;

        this.formulaService.marcarVersionVigente(this.idFormulaVersion).subscribe({
            next: formula => {
                this.formulaSeleccionada = formula;
                this.mostrarOk('Versión marcada como vigente.');
                this.cargarVersiones(true);
            },
            error: error => this.mostrarError(error)
        });
    }

    calcularSiguienteOrden(): number {
        const total = this.formulaSeleccionada?.detalles?.length || 0;
        return total + 1;
    }

    reiniciarDetalle(): void {
        this.nuevoDetalle = {
            idInsumo: 0,
            cantidadKg: 0,
            porcentaje: 0,
            tipoCalculo: 'PORCENTAJE_BATCH',
            esCritico: false,
            ordenAdicion: this.calcularSiguienteOrden()
        };
    }

    private mostrarError(error: any): void {
        console.error(error);

        this.mensajeError =
            error?.error?.message ||
            error?.error?.mensaje ||
            'Ocurrió un error en la operación.';
    }

    private mostrarOk(mensaje: string): void {
        this.mensajeOk = mensaje;
        this.mensajeError = '';
    }

    private limpiarMensajes(): void {
        this.mensajeError = '';
        this.mensajeOk = '';
    }
}