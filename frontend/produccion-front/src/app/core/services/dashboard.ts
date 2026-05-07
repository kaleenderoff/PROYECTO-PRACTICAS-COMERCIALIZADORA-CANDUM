import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ProduccionVsEmpaque {
  fecha: string;
  totalProducido: number;
  totalEmpaquetado: number;
  diferencia: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly apiUrl = 'http://localhost:8082/api/dashboard';

  constructor(private http: HttpClient) { }

  obtenerProduccionVsEmpaque(): Observable<ProduccionVsEmpaque[]> {
    return this.http.get<ProduccionVsEmpaque[]>(`${this.apiUrl}/produccion-vs-empaque`);
  }
}