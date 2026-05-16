import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ProduccionVsEmpaque {
  fecha: string;
  totalProducido: number;
  totalEmpaquetado: number;
  diferencia: number;
}

export interface RendimientoAnual {
  mes: number;
  ptDulceLeche: number;
  ptLecheCondensada: number;
  lecheRecibida: number;
  rendimientoDL: number;
  rendimientoLC: number;
}

export interface ResumenMesGerencial {
  lecheRecibidaTotal: number;
  kgCremaReal: number;
  kgCremaEsperada: number;
  ptDulceLecheKg: number;
  ptLecheCondensadaKg: number;
}

export interface KpisRendimiento {
  rendDulceLechePromedio: number;
  rendLecheCondensadaPromedio: number;
  metaDulceLeche: number;
  metaLecheCondensada: number;
  estadoDulceLeche: string;
  estadoLecheCondensada: string;
}

export interface DashboardGerencial {
  resumenMes: ResumenMesGerencial;
  tablaSemanal: any[];
  kpis: KpisRendimiento;
}

export interface DashboardProduccionSku {
  tipoProducto: string;
  items: SkuProduccionMensual[];
  totalKilos?: number;
  totalUnidades?: number;
}

export interface SkuProduccionMensual {
  sku: string;
  pesoNeto: number;
  unidadesMes: number;
  kilosMes: number;
  detalleDiario: any[];
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly apiUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) { }

  obtenerProduccionVsEmpaque(): Observable<ProduccionVsEmpaque[]> {
    return this.http.get<ProduccionVsEmpaque[]>(`${this.apiUrl}/produccion-vs-empaque`);
  }

  obtenerGerencial(mes: number, anio: number): Observable<DashboardGerencial> {
    return this.http.get<DashboardGerencial>(`${this.apiUrl}/gerencial`, {
      params: { mes: mes.toString(), anio: anio.toString() }
    });
  }

  obtenerProduccionSku(mes: number, anio: number): Observable<DashboardProduccionSku[]> {
    return this.http.get<DashboardProduccionSku[]>(`${this.apiUrl}/produccion-sku`, {
      params: { mes: mes.toString(), anio: anio.toString() }
    });
  }

  obtenerRendimientoAnual(anio: number): Observable<RendimientoAnual[]> {
    return this.http.get<RendimientoAnual[]>(`${this.apiUrl}/rendimiento-anual`, {
      params: { anio: anio.toString() }
    });
  }
}
