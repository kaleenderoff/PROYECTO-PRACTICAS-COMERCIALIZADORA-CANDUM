import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FormulaService } from '../../core/services/formula';
import { AuthService } from '../../core/services/auth';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-insumos',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule
    ],
    templateUrl: './insumos.html',
    
})
export class Insumos implements OnInit {

    private formulaService = inject(FormulaService);
    public authService = inject(AuthService);

    insumos: any[] = [];
    filtro = '';
    mostrarFormulario = false;

    modoEdicion = false;
    idInsumoEditando: number | null = null;

    // Paginación
    paginaActual = 1;
    itemsPorPagina = 10;

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

    toggleFormulario(): void {
        this.mostrarFormulario = !this.mostrarFormulario;
        if (!this.mostrarFormulario) {
            this.limpiarFormulario();
            this.limpiarMensajes();
        }
    }

    cargarInsumos(): void {
        this.formulaService.listarInsumos().subscribe({
            next: data => {
                this.insumos = data;
                this.paginaActual = 1;
            },
            error: error => {
                console.error(error);
                this.mensajeError = 'Error cargando insumos.';
            }
        });
    }

    guardarInsumo(): void {
        if (!this.authService.canManageCatalogosTecnicos()) return;
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
        if (!this.authService.canManageCatalogosTecnicos()) return;
        this.formulaService.crearInsumo(this.nuevoInsumo).subscribe({
            next: () => {
                this.mensajeOk = 'Insumo creado correctamente.';
                this.mostrarFormulario = false;
                this.limpiarFormulario();
                this.cargarInsumos();
            },
            error: error => this.mostrarError(error, 'Error creando insumo.')
        });
    }

    editarInsumo(insumo: any): void {
        if (!this.authService.canManageCatalogosTecnicos()) return;
        this.limpiarMensajes();

        const id = insumo.id ?? insumo.idInsumo;

        if (!id) {
            this.mensajeError = 'No se encontró el ID del insumo.';
            return;
        }

        this.modoEdicion = true;
        this.idInsumoEditando = id;
        this.mostrarFormulario = true;

        this.nuevoInsumo = {
            nombre: insumo.nombre || '',
            tipo: insumo.tipo || 'MATERIA_PRIMA',
            unidadMedida: insumo.unidadMedida || 'kg',
            activo: insumo.activo ?? true
        };

        window.scrollTo({ top: 0, behavior: 'smooth' });
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    toggleEstado(insumo: any): void {
        if (!this.authService.canManageCatalogosTecnicos()) return;
        const id = insumo.id ?? insumo.idInsumo;
        if (!id) return;

        const payload = {
            nombre: insumo.nombre,
            tipo: insumo.tipo,
            unidadMedida: insumo.unidadMedida,
            activo: !insumo.activo
        };

        this.formulaService.actualizarInsumo(id, payload).subscribe({
            next: () => {
                this.mensajeOk = `Insumo ${payload.activo ? 'activado' : 'inactivado'} correctamente.`;
                this.cargarInsumos();
                setTimeout(() => this.mensajeOk = '', 3000);
            },
            error: error => this.mostrarError(error, 'Error al cambiar estado.')
        });
    }

    actualizarInsumo(): void {
        if (!this.authService.canManageCatalogosTecnicos()) return;
        if (!this.idInsumoEditando) {
            this.mensajeError = 'No se encontró el insumo a editar.';
            return;
        }

        this.formulaService.actualizarInsumo(this.idInsumoEditando, this.nuevoInsumo).subscribe({
            next: () => {
                this.mensajeOk = 'Insumo actualizado correctamente.';
                this.mostrarFormulario = false;
                this.limpiarFormulario();
                this.cargarInsumos();
            },
            error: error => this.mostrarError(error, 'Error actualizando insumo.')
        });
    }

    cancelarEdicion(): void {
        this.mostrarFormulario = false;
        this.limpiarFormulario();
        this.limpiarMensajes();
    }

    get insumosFiltrados(): any[] {
        if (!this.filtro.trim()) {
            return this.insumos;
        }

        const filtroNormalizado = this.filtro.toLowerCase();

        return this.insumos.filter((i: any) =>
            String(i.nombre || '').toLowerCase().includes(filtroNormalizado) ||
            String(i.tipo || '').toLowerCase().includes(filtroNormalizado) ||
            String(i.unidadMedida || '').toLowerCase().includes(filtroNormalizado)
        );
    }

    get insumosPaginados(): any[] {
        const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
        return this.insumosFiltrados.slice(inicio, inicio + this.itemsPorPagina);
    }

    get totalPaginas(): number {
        return Math.ceil(this.insumosFiltrados.length / this.itemsPorPagina);
    }

    get paginas(): number[] {
        return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
    }

    cambiarPagina(p: number): void {
        if (p >= 1 && p <= this.totalPaginas) {
            this.paginaActual = p;
        }
    }

    eliminarInsumo(id: number): void {
        Swal.fire({
            title: '¿Estás seguro?',
            text: 'Esta acción eliminará permanentemente este insumo.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#059669',
            cancelButtonColor: '#ef4444',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                this.formulaService.eliminarInsumo(id).subscribe({
                    next: () => {
                        this.mensajeOk = 'Insumo eliminado correctamente';
                        this.cargarInsumos();
                        setTimeout(() => this.mensajeOk = '', 3000);
                    },
                    error: (err) => {
                        this.mensajeError = err.error?.message || 'Error al eliminar el insumo';
                        setTimeout(() => this.mensajeError = '', 3000);
                    }
                });
            }
        });
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
