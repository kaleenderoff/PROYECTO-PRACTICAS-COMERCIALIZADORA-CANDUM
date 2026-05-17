import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface ControlCalidadProcesoRequest {
  idOrdenProduccion: number;
  idEjecucionBatch?: number | null;
  fechaProduccion: string;
  tipoProducto?: string | null;
  producto?: string | null;
  lote?: string | null;
  numeroMarmita?: number | null;
  productoEnProceso?: string | null;
  phLeche?: number | null;
  acidezLeche?: number | null;
  densidadLeche?: number | null;
  grasaLeche?: number | null;
  horaInicioHidrolisis?: string | null;
  phInicial?: number | null;
  horaFinHidrolisis?: string | null;
  temperaturaInicial?: number | null;
  temperaturaFinal?: number | null;
  acidezInicial?: number | null;
  acidezFinal?: number | null;
  phFinal?: number | null;
  brixInicial?: number | null;
  brixFinal?: number | null;
  presion?: number | null;
  temperaturaCoccion?: number | null;
  temperaturaEnvasado?: number | null;
  colorVisual?: string | null;
  saborVisual?: string | null;
  texturaVisual?: string | null;
  presentacionEnvasado?: string | null;
  fechaVencimiento?: string | null;
  liberado?: boolean;
  retenido?: boolean;
  idRealizadoPor: number;
  idVerificadoPor?: number | null;
  observaciones?: string | null;
}

export interface ControlCalidadProcesoResponse extends ControlCalidadProcesoRequest {
  id: number;
  createdAt?: string;
}

export interface ControlPesoMuestraRequest {
  numeroMuestra: number;
  pesoBruto?: number | null;
  tara?: number | null;
  pesoNeto: number;
}

export interface ControlPesoMuestraResponse extends ControlPesoMuestraRequest {
  id: number;
}

export interface ControlPesoProductoRequest {
  idOrdenProduccion: number;
  idEjecucionBatch?: number | null;
  idSku?: number | null;
  fechaControl: string;
  producto?: string | null;
  marca?: string | null;
  lote?: string | null;
  fechaVencimiento?: string | null;
  presentacion?: string | null;
  numeroTanda?: string | null;
  rangoBatches?: string | null;
  pesoBrutoPromedio?: number | null;
  taraPromedio?: number | null;
  pesoNetoPromedio?: number | null;
  aparienciaOk?: boolean | null;
  etiquetadoOk?: boolean | null;
  tapadoOk?: boolean | null;
  cantidadPorCaja?: number | null;
  liberado?: boolean;
  retenido?: boolean;
  idRealizadoPor: number;
  idVerificadoPor?: number | null;
  observaciones?: string | null;
  muestras: ControlPesoMuestraRequest[];
}

export interface ControlPesoProductoResponse extends ControlPesoProductoRequest {
  id: number;
  createdAt?: string;
  muestras: ControlPesoMuestraResponse[];
}

@Injectable({
  providedIn: 'root'
})
export class ControlCalidadLacteaService {
  private readonly apiUrl = `${environment.apiUrl}/controles-calidad-lactea`;

  constructor(private http: HttpClient) { }

  registrarProceso(request: ControlCalidadProcesoRequest): Observable<ControlCalidadProcesoResponse> {
    return this.http.post<ControlCalidadProcesoResponse>(`${this.apiUrl}/proceso`, request);
  }

  listarProcesosPorOrden(idOrden: number): Observable<ControlCalidadProcesoResponse[]> {
    return this.http.get<ControlCalidadProcesoResponse[]>(`${this.apiUrl}/proceso/orden/${idOrden}`);
  }

  registrarPeso(request: ControlPesoProductoRequest): Observable<ControlPesoProductoResponse> {
    return this.http.post<ControlPesoProductoResponse>(`${this.apiUrl}/peso`, request);
  }

  listarPesosPorOrden(idOrden: number): Observable<ControlPesoProductoResponse[]> {
    return this.http.get<ControlPesoProductoResponse[]>(`${this.apiUrl}/peso/orden/${idOrden}`);
  }
}
