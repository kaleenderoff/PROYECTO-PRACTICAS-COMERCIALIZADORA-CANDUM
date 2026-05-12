import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface OrdenProduccionResponse {
  id: number;
  numeroOrden: string;
  idProgramacion: number;
  idLinea: number;
  idProducto: number;
  idTurno: number;
  idJefeLineaEjecutor?: number;
  idCreadaPor: number;
  fechaProduccion: string;
  estado: string;
  observaciones?: string;
  fechaInicioReal?: string;
  fechaFinReal?: string;
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
}