import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DescremadoRecepcion {
  id: number;
  idRecepcionLeche: number;
  idTanqueDestino?: number;
  litrosDescremados: number;
  cremaObtenidaKg?: number;
  idSkuCrema?: number;
  unidadesCrema?: number;
  kgPorUnidadCrema?: number;
  loteCrema?: string;
  observaciones?: string;
  createdAt?: string;
}

export interface DescremadoRecepcionRequest {
  idRecepcionLeche: number;
  idTanqueDestino?: number;
  litrosDescremados: number;
  cremaObtenidaKg?: number;
  idSkuCrema?: number;
  unidadesCrema?: number;
  kgPorUnidadCrema?: number;
  loteCrema?: string;
  observaciones?: string;
}

export interface SkuCatalogo {
  id: number;
  codigoSku: string;
  descripcion: string;
  idProducto: number;
  nombreProducto: string;
  idMarca: number;
  nombreMarca: string;
  pesoNetoGr: number;
  tipoEnvase: string;
  unidadesPorCaja?: number;
  esExport: boolean;
  activo: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class DescremadoService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  listar(): Observable<DescremadoRecepcion[]> {
    return this.http.get<DescremadoRecepcion[]>(`${this.apiUrl}/descremados-recepcion`);
  }

  registrar(request: DescremadoRecepcionRequest): Observable<DescremadoRecepcion> {
    return this.http.post<DescremadoRecepcion>(`${this.apiUrl}/descremados-recepcion`, request);
  }

  listarSkus(): Observable<SkuCatalogo[]> {
    return this.http.get<SkuCatalogo[]>(`${this.apiUrl}/catalogos/skus`);
  }
}
