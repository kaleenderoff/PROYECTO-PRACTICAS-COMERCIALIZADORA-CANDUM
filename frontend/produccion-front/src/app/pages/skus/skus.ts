import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CatalogoService } from '../../core/services/catalogo';
import { AuthService } from '../../core/services/auth';

@Component({
    selector: 'app-skus',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule
    ],
    templateUrl: './skus.html'
})
export class Skus implements OnInit {

    private catalogoService = inject(CatalogoService);
    public authService = inject(AuthService);

    skus: any[] = [];
    productos: any[] = [];
    marcas: any[] = [];
    tiposEnvase = ['DISPENSADOR', 'DOYPACK', 'GARRAFA', 'BALDE', 'TAZA', 'TETERO', 'BIPACK', 'BOLSA', 'OTRO'];
    
    filtro = '';
    modoEdicion = false;
    idSkuEditando: number | null = null;

    // Paginación
    paginaActual = 1;
    itemsPorPagina = 10;

    nuevoSku = {
        codigoSku: '',
        descripcion: '',
        idProducto: null as number | null,
        idMarca: null as number | null,
        pesoNetoGr: 0,
        unidadMedida: 'gr',
        tipoEnvase: 'OTRO',
        unidadesPorCaja: 1,
        esExport: false,
        activo: true
    };

    mensajeOk = '';
    mensajeError = '';

    ngOnInit(): void {
        this.cargarSkus();
        this.cargarCatalogos();
    }

    cargarSkus(): void {
        this.catalogoService.listarSkus(false).subscribe({
            next: data => {
                this.skus = data;
                this.paginaActual = 1;
            },
            error: error => this.mostrarError(error, 'Error cargando SKUs.')
        });
    }

    cargarCatalogos(): void {
        this.catalogoService.listarProductos().subscribe(data => this.productos = data);
        this.catalogoService.listarMarcas().subscribe(data => this.marcas = data);
    }

    guardarSku(): void {
        this.limpiarMensajes();

        if (!this.nuevoSku.codigoSku.trim() || !this.nuevoSku.idProducto || !this.nuevoSku.idMarca) {
            this.mensajeError = 'Complete los campos obligatorios (Producto, Marca, Código).';
            return;
        }

        if (this.modoEdicion) {
            this.actualizarSku();
        } else {
            this.crearSku();
        }
    }

    crearSku(): void {
        this.catalogoService.crearSku(this.nuevoSku).subscribe({
            next: () => {
                this.mensajeOk = 'SKU creado correctamente.';
                this.limpiarFormulario();
                this.cargarSkus();
            },
            error: error => this.mostrarError(error, 'Error creando SKU.')
        });
    }

    editarSku(sku: any): void {
        this.limpiarMensajes();
        this.modoEdicion = true;
        this.idSkuEditando = sku.id;

        this.nuevoSku = {
            codigoSku: sku.codigoSku,
            descripcion: sku.descripcion,
            idProducto: sku.idProducto,
            idMarca: sku.idMarca,
            pesoNetoGr: sku.pesoNetoGr,
            unidadMedida: sku.unidadMedida || 'gr',
            tipoEnvase: sku.tipoEnvase,
            unidadesPorCaja: sku.unidadesPorCaja,
            esExport: sku.esExport,
            activo: sku.activo
        };

        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    actualizarSku(): void {
        if (!this.idSkuEditando) return;

        this.catalogoService.actualizarSku(this.idSkuEditando, this.nuevoSku).subscribe({
            next: () => {
                this.mensajeOk = 'SKU actualizado correctamente.';
                this.limpiarFormulario();
                this.cargarSkus();
            },
            error: error => this.mostrarError(error, 'Error actualizando SKU.')
        });
    }

    toggleEstado(sku: any): void {
        const body = { ...sku, activo: !sku.activo };
        this.catalogoService.actualizarSku(sku.id, body).subscribe({
            next: () => {
                this.mensajeOk = `SKU ${!sku.activo ? 'activado' : 'inactivado'} correctamente.`;
                this.cargarSkus();
            },
            error: error => this.mostrarError(error, 'Error cambiando estado.')
        });
    }

    cancelarEdicion(): void {
        this.limpiarFormulario();
        this.limpiarMensajes();
    }

    get skusFiltrados(): any[] {
        let filtrados = this.skus;
        if (this.filtro.trim()) {
            const f = this.filtro.toLowerCase();
            filtrados = this.skus.filter((s: any) => 
                s.codigoSku.toLowerCase().includes(f) || 
                s.descripcion.toLowerCase().includes(f) ||
                s.nombreProducto.toLowerCase().includes(f)
            );
        }
        return filtrados;
    }

    get skusPaginados(): any[] {
        const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
        return this.skusFiltrados.slice(inicio, inicio + this.itemsPorPagina);
    }

    get totalPaginas(): number {
        return Math.ceil(this.skusFiltrados.length / this.itemsPorPagina);
    }

    get paginas(): number[] {
        const paginas = [];
        for (let i = 1; i <= this.totalPaginas; i++) {
            paginas.push(i);
        }
        return paginas;
    }

    cambiarPagina(p: number): void {
        if (p >= 1 && p <= this.totalPaginas) {
            this.paginaActual = p;
        }
    }

    limpiarFormulario(): void {
        this.modoEdicion = false;
        this.idSkuEditando = null;
        this.nuevoSku = {
            codigoSku: '',
            descripcion: '',
            idProducto: null,
            idMarca: null,
            pesoNetoGr: 0,
            unidadMedida: 'gr',
            tipoEnvase: 'OTRO',
            unidadesPorCaja: 1,
            esExport: false,
            activo: true
        };
    }

    limpiarMensajes(): void {
        this.mensajeOk = '';
        this.mensajeError = '';
    }

    private mostrarError(error: any, mensajeDefault: string): void {
        console.error(error);
        this.mensajeError = error?.error?.message || error?.error?.mensaje || mensajeDefault;
    }
}
