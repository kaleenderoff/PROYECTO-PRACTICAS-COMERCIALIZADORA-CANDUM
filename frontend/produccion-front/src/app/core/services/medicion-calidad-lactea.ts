import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export type TipoMedicionCalidadLactea = 'BACHE' | 'MEZCLA' | 'TANDA';

export interface MedicionCalidadLacteaRequest {
  idProduccionLactea?: number | null;
  idProduccionLacteaBatch?: number | null;
  idOrdenProduccion?: number | null;
  idEjecucionBatch?: number | null;
  tipoMedicion: TipoMedicionCalidadLactea;
  referencia: string;
  brix?: number | null;
  ph?: number | null;
  fechaHoraMedicion?: string | null;
  idUsuarioCalidad: number;
  observaciones?: string | null;
}

export interface MedicionCalidadLacteaResponse {
  id: number;
  idProduccionLactea?: number | null;
  idProduccionLacteaBatch?: number | null;
  idOrdenProduccion?: number | null;
  idEjecucionBatch?: number | null;
  tipoMedicion: TipoMedicionCalidadLactea;
  referencia: string;
  brix?: number | null;
  ph?: number | null;
  fechaHoraMedicion: string;
  idUsuarioCalidad: number;
  observaciones?: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class MedicionCalidadLacteaService {
  private readonly apiUrl = `${environment.apiUrl}/mediciones-calidad-lactea`;

  constructor(private http: HttpClient) { }

  registrar(request: MedicionCalidadLacteaRequest): Observable<MedicionCalidadLacteaResponse> {
    return this.http.post<MedicionCalidadLacteaResponse>(this.apiUrl, request);
  }

  listarPorOrden(idOrdenProduccion: number): Observable<MedicionCalidadLacteaResponse[]> {
    return this.http.get<MedicionCalidadLacteaResponse[]>(`${this.apiUrl}/orden/${idOrdenProduccion}`);
  }
}
