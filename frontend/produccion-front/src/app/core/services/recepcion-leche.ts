import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface RecepcionLechePesaje {
  id?: number;
  idRecepcionLeche?: number;
  numeroPesaje: number;
  pesoBrutoKg: number;
  taraKg: number;
  pesoNetoKg?: number;
  observaciones?: string;
}

export interface RecepcionLeche {
  id: number;
  fechaRecepcion: string;
  tipoMateriaPrima?: string;
  proveedor: string;
  cantidadRecibidaLitros: number;
  recibidoPor?: string;
  idTanque?: number | null;
  idUsuario: number;
  idMovimientoLeche?: number | null;
  numeroRemision?: string;
  cantidadRemisionLitros?: number;
  observaciones?: string;
  pesajes?: RecepcionLechePesaje[];
}

export interface RecepcionLecheRequest {
  fechaRecepcion: string;
  tipoMateriaPrima: string;
  proveedor: string;
  cantidadRecibidaLitros: number;
  recibidoPor?: string;
  idUsuario: number;
  idTanque: number;
  numeroRemision?: string;
  cantidadRemisionLitros?: number;
  observaciones?: string;
  pesajes: RecepcionLechePesajeRequest[];
}

export interface RecepcionLechePesajeRequest {
  numeroPesaje: number;
  pesoBrutoKg: number;
  taraKg: number;
  observaciones?: string;
}

export interface SaldoTanqueLeche {
  idTanque: number;
  nombre: string;
  tipo: string;
  saldoLitros: number;
  activo: boolean;
}

export interface Proveedor {
  id: number;
  nombre: string;
  activo: boolean;
}

export interface ProveedorRequest {
  nombre: string;
  activo: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class RecepcionLecheService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  listarRecepciones(): Observable<RecepcionLeche[]> {
    return this.http.get<RecepcionLeche[]>(`${this.apiUrl}/recepciones-leche`);
  }

  listarSaldosTanques(): Observable<SaldoTanqueLeche[]> {
    return this.http.get<SaldoTanqueLeche[]>(`${this.apiUrl}/tanques-leche/saldos`);
  }

  registrarRecepcion(request: RecepcionLecheRequest): Observable<RecepcionLeche> {
    return this.http.post<RecepcionLeche>(`${this.apiUrl}/recepciones-leche`, request);
  }

  listarProveedores(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`${this.apiUrl}/catalogos/proveedores`);
  }

  listarTodosProveedores(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(`${this.apiUrl}/catalogos/proveedores?activos=false`);
  }

  crearProveedor(request: ProveedorRequest): Observable<Proveedor> {
    return this.http.post<Proveedor>(`${this.apiUrl}/catalogos/proveedores`, request);
  }

  actualizarProveedor(id: number, request: ProveedorRequest): Observable<Proveedor> {
    return this.http.put<Proveedor>(`${this.apiUrl}/catalogos/proveedores/${id}`, request);
  }

  eliminarProveedor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/catalogos/proveedores/${id}`);
  }
}