import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface AuditoriaResponse {
  id: number;
  idUsuario: number;
  nombreUsuario: string;
  accion: string;
  entidadAfectada: string;
  idRegistroAfectado?: number;
  detalle?: string;
  fechaHora: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuditoriaService {

  private readonly apiUrl = `${environment.apiUrl}/auditoria`;

  constructor(private http: HttpClient) { }

  listarUltimos(limite = 100): Observable<AuditoriaResponse[]> {
    return this.http.get<AuditoriaResponse[]>(`${this.apiUrl}?limite=${limite}`);
  }
}
