import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Insumo {
    id: number;
    codigo?: string;
    nombre: string;
    descripcion?: string;
    tipo: string;
    unidadMedida: string;
    stockMinimo?: number;
    idProveedor?: number;
    nombreProveedor?: string;
    activo: boolean;
}

export interface SkuCatalogo {
    id: number;
    codigoSku: string;
    descripcion: string;
    idProducto: number;
    nombreProducto: string;
    idMarca: number;
    nombreMarca: string;
    pesoNetoGr: number;
    tipoEnvase: string;
    unidadesPorCaja?: number;
    esExport: boolean;
    activo: boolean;
}

export interface ProductoCatalogo {
    id: number;
    nombre: string;
    idLinea: number;
    nombreLinea: string;
    activo: boolean;
}

export interface OrdenProduccionIngrediente {
    idInsumo: number;
    nombreInsumo: string;
    porcentajeFormula: number;
    cantidadSugeridaKg: number;
    cantidadAjustadaKg: number;
    cantidadGramos: number;
}

export interface OrdenProduccionPresentacion {
    idSku: number;
    codigoSku: string;
    descripcion: string;
    pesoGramos: number;
    unidadesPlaneadas: number;
    kilosProductoTerminado: number;
    kilosBatch: number;
    numeroBatchesCalculado: number;
}

export interface OrdenProduccionCalculo {
    kilosProductoTerminadoTotal: number;
    kilosBatchTotal: number;
    numeroBatchesCalculado: number;
}

@Injectable({
    providedIn: 'root'
})
export class OrdenProduccionService {
    private readonly apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    listarInsumos(): Observable<Insumo[]> {
        return this.http.get<Insumo[]>(`${this.apiUrl}/catalogos/insumos`);
    }

    listarSkus(): Observable<SkuCatalogo[]> {
        return this.http.get<SkuCatalogo[]>(`${this.apiUrl}/catalogos/skus`);
    }

    listarProductos(): Observable<ProductoCatalogo[]> {
        return this.http.get<ProductoCatalogo[]>(`${this.apiUrl}/catalogos/productos`);
    }
}