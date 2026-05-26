import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ResumenConsumoInsumos {
  fecha: string;
  totalOrdenes: number;
  totalBatches: number;
  totalKgEntrada: number;
  totalKgProducidos: number;
  rendimientoPromedio: number;
  insumos: ConsumoInsumoDetalle[];
}

export interface ConsumoInsumoDetalle {
  nombreInsumo: string;
  unidadMedida: string;
  cantidadTotal: number;
}

export interface ResumenRecepcionDescremado {
  fecha: string;
  totalRecepciones: number;
  totalLitrosRecibidos: number;
  totalKgCrema: number;
  recepciones: any[];
  descremados: any[];
}

@Injectable({
  providedIn: 'root'
})
export class ReporteLacteoService {

  private readonly apiUrl = `${environment.apiUrl}/reportes/lacteos`;
  private readonly dashboardUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  // ─── JSON ────────────────────────────────────────────────────────────────────

  obtenerConsumoInsumos(fecha: string): Observable<ResumenConsumoInsumos> {
    return this.http.get<ResumenConsumoInsumos>(`${this.apiUrl}/consumo-insumos`, {
      params: { fecha }
    });
  }

  obtenerRecepcionDescremado(fecha: string): Observable<ResumenRecepcionDescremado> {
    return this.http.get<ResumenRecepcionDescremado>(`${this.apiUrl}/recepcion-descremado`, {
      params: { fecha }
    });
  }

  // ─── EXCEL DOWNLOADS ─────────────────────────────────────────────────────────

  descargarExcelConsumoInsumos(fecha: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/consumo-insumos/excel`, {
      params: { fecha },
      responseType: 'blob'
    });
  }

  descargarExcelRecepcionDescremado(fecha: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/recepcion-descremado/excel`, {
      params: { fecha },
      responseType: 'blob'
    });
  }

  descargarExcelProduccionVsEmpaque(): Observable<Blob> {
    return this.http.get(`${this.dashboardUrl}/produccion-vs-empaque/excel`, {
      responseType: 'blob'
    });
  }

  // ─── HELPER ──────────────────────────────────────────────────────────────────

  static triggerDownload(blob: Blob, filename: string): void {
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  }
}
