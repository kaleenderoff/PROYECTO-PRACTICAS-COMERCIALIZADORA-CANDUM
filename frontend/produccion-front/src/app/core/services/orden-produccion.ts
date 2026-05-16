import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface ProgramacionSkuResponse {
  id: number;
  idProgramacion: number;
  idSku: number;
  codigoSku: string;
  descripcionSku: string;
  pesoUnidadGr: number;
  unidadesObjetivo: number;
  kgProductoTerminado: number;
  rendimientoTeoricoPct: number;
  kgBatchFormula: number;
  kgBatchCalculado: number;
  numBachesCalculado: number;
  cantidadReal?: number;
  unidadesReales?: number;
  observaciones?: string;
}

export interface RegistrarProduccionSkuRequest {
  idOrdenDetalle: number;
  cantidadReal: number;
  unidadesReales: number;
  observaciones?: string;
}

export interface OrdenProduccionResponse {
  id: number;
  numeroOrden: string;
  idProgramacion: number;
  idLinea: number;
  nombreLinea: string;
  idProducto: number;
  nombreProducto: string;
  idTurno: number;
  nombreTurno: string;
  idJefeLineaEjecutor?: number;
  nombreJefeLineaEjecutor?: string;
  idCreadaPor: number;
  nombreCreadaPor: string;
  fechaProduccion: string;
  estado: string;
  observaciones?: string;
  fechaInicioReal?: string;
  fechaFinReal?: string;
  idTanqueLeche?: number;
  nombreTanqueLeche?: string;

  // Resumen Operativo
  numBachesPlan: number;
  kgBachePlan: number;
  kgPTTotalPlan: number;
  kgEntradaTotalPlan: number;
  nombreFormula: string;
  versionFormula: string;

  // Detalle SKUs
  skus: ProgramacionSkuResponse[];

  // Métricas Reales
  kgEntradaReal: any;
  kgProducidoBatches: any;
  kgPtReal: any;
  rendimientoReal: any;
  mermaReal: any;
  mermaEmpaque: any;
}

@Injectable({
  providedIn: 'root',
})
export class OrdenProduccionService {

  private readonly apiUrl = `${environment.apiUrl}/ordenes-produccion`;

  constructor(private http: HttpClient) { }

  listar(): Observable<OrdenProduccionResponse[]> {
    return this.http.get<OrdenProduccionResponse[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<OrdenProduccionResponse> {
    return this.http.get<OrdenProduccionResponse>(`${this.apiUrl}/${id}`);
  }

  iniciar(id: number, idJefeLineaEjecutor: number): Observable<OrdenProduccionResponse> {
    return this.http.patch<OrdenProduccionResponse>(
      `${this.apiUrl}/${id}/iniciar`,
      { idJefeLineaEjecutor }
    );
  }

  finalizar(id: number): Observable<OrdenProduccionResponse> {
    return this.http.patch<OrdenProduccionResponse>(
      `${this.apiUrl}/${id}/finalizar`,
      {}
    );
  }

  cancelar(id: number, observaciones: string): Observable<OrdenProduccionResponse> {
    return this.http.patch<OrdenProduccionResponse>(
      `${this.apiUrl}/${id}/cancelar`,
      { observaciones }
    );
  }

  registrarSkus(id: number, producciones: RegistrarProduccionSkuRequest[]): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/skus`, producciones);
  }

  actualizarTanqueLeche(id: number, idTanque: number): Observable<OrdenProduccionResponse> {
    return this.http.patch<OrdenProduccionResponse>(`${this.apiUrl}/${id}/tanque?idTanque=${idTanque}`, {});
  }
}
