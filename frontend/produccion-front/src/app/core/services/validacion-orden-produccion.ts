import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface ValidacionOrdenProduccionRequest {
  idOrden: number;
  aprobado: boolean;
  idJefeProduccion: number;
  observacion?: string;
  requiereRevision?: boolean;
}

export interface ValidacionOrdenProduccionResponse {
  id: number;
  idOrden: number;
  numeroOrden: string;
  aprobado: boolean;
  idJefeProduccion: number;
  observacion?: string;
  fechaValidacion: string;
  requiereRevision: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ValidacionOrdenProduccionService {

  private readonly apiUrl = `${environment.apiUrl}/validaciones-orden-produccion`;

  constructor(private http: HttpClient) { }

  obtenerPorOrden(idOrden: number): Observable<ValidacionOrdenProduccionResponse> {
    return this.http.get<ValidacionOrdenProduccionResponse>(`${this.apiUrl}/orden/${idOrden}`);
  }

  validar(request: ValidacionOrdenProduccionRequest): Observable<ValidacionOrdenProduccionResponse> {
    return this.http.post<ValidacionOrdenProduccionResponse>(this.apiUrl, request);
  }
}
