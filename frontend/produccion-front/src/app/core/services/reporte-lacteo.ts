import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ResumenConsumoInsumos {
  fecha: string;
  totales: TotalesConsumoInsumos;
  detalles: ConsumoInsumoDetalle[];
}

export interface TotalesConsumoInsumos {
  registros: number;
  producciones: number;
  batches: number;
  cantidadRequeridaTotal: number;
  cantidadUsadaTotal: number;
  diferenciaTotal: number;
}

export interface ConsumoInsumoDetalle {
  fechaProduccion: string;
  producto: string;
  numeroBatch?: number;
  codigoInsumo?: string;
  insumo: string;
  tipoInsumo: string;
  loteInsumo?: string;
  cantidadRequerida: number;
  cantidadUsada: number;
  diferencia: number;
  unidadMedida: string;
  usuario: string;
}

export interface ResumenRecepcionDescremado {
  fecha: string;
  totales: TotalesRecepcionDescremado;
  detalles: RecepcionDescremadoDetalle[];
}

export interface TotalesRecepcionDescremado {
  recepciones: number;
  proveedores: number;
  descremados: number;
  litrosRecibidos: number;
  litrosRemision: number;
  litrosDescremados: number;
  cremaObtenidaKg: number;
  unidadesCrema: number;
}

export interface RecepcionDescremadoDetalle {
  idRecepcion: number;
  fechaRecepcion: string;
  proveedor: string;
  tipoMateriaPrima: string;
  litrosRecibidos: number;
  litrosRemision: number;
  numeroRemision: string;
  tanqueRecepcion: string;
  recibidoPor?: string;
  observacionesRecepcion?: string;
  pesajes: any[];
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
