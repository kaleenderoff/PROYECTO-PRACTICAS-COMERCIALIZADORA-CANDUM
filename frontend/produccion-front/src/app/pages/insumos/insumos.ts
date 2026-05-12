import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FormulaService } from '../../core/services/formula';

@Component({
    selector: 'app-insumos',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule
    ],
    templateUrl: './insumos.html',
    styleUrl: './insumos.scss'
})
export class Insumos implements OnInit {

    private formulaService = inject(FormulaService);

    insumos: any[] = [];
    filtro = '';

    modoEdicion = false;
    idInsumoEditando: number | null = null;

    nuevoInsumo = {
        nombre: '',
        tipo: 'MATERIA_PRIMA',
        unidadMedida: 'kg',
        activo: true
    };

    mensajeOk = '';
    mensajeError = '';

    ngOnInit(): void {
        this.cargarInsumos();
    }

    cargarInsumos(): void {
        this.formulaService.listarInsumos().subscribe({
            next: data => {
                this.insumos = data;
            },
            error: error => {
                console.error(error);
                this.mensajeError = 'Error cargando insumos.';
            }
        });
    }

    guardarInsumo(): void {
        this.limpiarMensajes();

        if (!this.nuevoInsumo.nombre.trim()) {
            this.mensajeError = 'Ingrese nombre.';
            return;
        }

        if (this.modoEdicion) {
            this.actualizarInsumo();
        } else {
            this.crearInsumo();
        }
    }

    crearInsumo(): void {
        this.formulaService.crearInsumo(this.nuevoInsumo).subscribe({
            next: () => {
                this.mensajeOk = 'Insumo creado correctamente.';
                this.limpiarFormulario();
                this.cargarInsumos();
            },
            error: error => this.mostrarError(error, 'Error creando insumo.')
        });
    }

    editarInsumo(insumo: any): void {
        this.limpiarMensajes();

        const id = insumo.id ?? insumo.idInsumo;

        if (!id) {
            this.mensajeError = 'No se encontró el ID del insumo.';
            return;
        }

        this.modoEdicion = true;
        this.idInsumoEditando = id;

        this.nuevoInsumo = {
            nombre: insumo.nombre || '',
            tipo: insumo.tipo || 'MATERIA_PRIMA',
            unidadMedida: insumo.unidadMedida || 'kg',
            activo: insumo.activo ?? true
        };

        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    actualizarInsumo(): void {
        if (!this.idInsumoEditando) {
            this.mensajeError = 'No se encontró el insumo a editar.';
            return;
        }

        this.formulaService.actualizarInsumo(this.idInsumoEditando, this.nuevoInsumo).subscribe({
            next: () => {
                this.mensajeOk = 'Insumo actualizado correctamente.';
                this.limpiarFormulario();
                this.cargarInsumos();
            },
            error: error => this.mostrarError(error, 'Error actualizando insumo.')
        });
    }

    cancelarEdicion(): void {
        this.limpiarFormulario();
        this.limpiarMensajes();
    }

    get insumosFiltrados(): any[] {
        if (!this.filtro.trim()) {
            return this.insumos;
        }

        const filtroNormalizado = this.filtro.toLowerCase();

        return this.insumos.filter(i =>
            String(i.nombre || '').toLowerCase().includes(filtroNormalizado) ||
            String(i.tipo || '').toLowerCase().includes(filtroNormalizado) ||
            String(i.unidadMedida || '').toLowerCase().includes(filtroNormalizado)
        );
    }

    limpiarFormulario(): void {
        this.modoEdicion = false;
        this.idInsumoEditando = null;

        this.nuevoInsumo = {
            nombre: '',
            tipo: 'MATERIA_PRIMA',
            unidadMedida: 'kg',
            activo: true
        };
    }

    limpiarMensajes(): void {
        this.mensajeOk = '';
        this.mensajeError = '';
    }

    private mostrarError(error: any, mensajeDefault: string): void {
        console.error(error);

        this.mensajeError =
            error?.error?.message ||
            error?.error?.mensaje ||
            mensajeDefault;
    }
}