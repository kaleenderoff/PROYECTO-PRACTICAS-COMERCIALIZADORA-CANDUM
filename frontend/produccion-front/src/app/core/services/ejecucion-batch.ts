import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface IniciarBatchRequest {
  idOrdenProduccion: number;
  idMarmita: number;
  kgEntrada: number;
}

export interface FinalizarBatchRequest {
  kgProducidos: number;
  observaciones?: string;
  conNovedad?: boolean;
  huboReproceso?: boolean;
  batchConforme?: boolean;
  brixFinal?: number;
}

export interface EjecucionBatch {
  id: number;
  idOrdenProduccion: number;
  numeroBatch: number;
  idMarmita: number;
  nombreMarmita: string;
  kgEntrada: number;
  kgProducidos?: number;
  rendimientoPct?: number;
  estado: string;
  observaciones?: string;
  fechaInicio?: string;
  fechaFin?: string;
  brixFinal?: number;
}

@Injectable({
  providedIn: 'root'
})
export class EjecucionBatchService {
  private apiUrl = `${environment.apiUrl}/ejecucion-batch`;

  constructor(private http: HttpClient) {}

  iniciar(request: IniciarBatchRequest): Observable<EjecucionBatch> {
    return this.http.post<EjecucionBatch>(`${this.apiUrl}/iniciar`, request);
  }

  finalizar(id: number, request: FinalizarBatchRequest): Observable<EjecucionBatch> {
    return this.http.patch<EjecucionBatch>(`${this.apiUrl}/${id}/finalizar`, request);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  listarPorOrden(idOrden: number): Observable<EjecucionBatch[]> {
    return this.http.get<EjecucionBatch[]>(`${this.apiUrl}/orden/${idOrden}`);
  }

  obtener(id: number): Observable<EjecucionBatch> {
    return this.http.get<EjecucionBatch>(`${this.apiUrl}/${id}`);
  }
}
