import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ProduccionLacteaBatch {
  id?: number;
  numeroBatch: number;
  idMarmita: number;
  litrosConsumidos: number;
  kilosProducidos: number;
  rendimiento?: number;
  idMovimientoLeche?: number;
  observaciones?: string;
}

export interface ProduccionLactea {
  id: number;
  idOrdenProduccion?: number | null;
  fechaProduccion: string;
  producto: string;
  idTanque: number;
  idUsuario: number;
  observaciones?: string;
  batches: ProduccionLacteaBatch[];
}

export interface ProduccionLacteaRequest {
  fechaProduccion: string;
  producto: string;
  idTanque: number;
  observaciones?: string;
  batches: ProduccionLacteaBatch[];
}

@Injectable({
  providedIn: 'root'
})
export class ProduccionLacteaService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  listar(): Observable<ProduccionLactea[]> {
    return this.http.get<ProduccionLactea[]>(`${this.apiUrl}/producciones-lactea`);
  }

  registrar(request: ProduccionLacteaRequest): Observable<ProduccionLactea> {
    return this.http.post<ProduccionLactea>(
      `${this.apiUrl}/producciones-lactea`,
      request
    );
  }
}
