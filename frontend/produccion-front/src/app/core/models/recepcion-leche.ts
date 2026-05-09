import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface RecepcionLeche {
    id: number;
    fechaRecepcion: string;
    tipoMateriaPrima?: string;
    proveedor: string;
    cantidadRecibidaLitros: number;
    recibidoPor?: string;
    idTanque: number;
    idUsuario: number;
    numeroRemision?: string;
    cantidadRemisionLitros?: number;
    observaciones?: string;
}

export interface RecepcionLecheRequest {
    fechaRecepcion: string;
    tipoMateriaPrima: string;
    proveedor: string;
    cantidadRecibidaLitros: number;
    recibidoPor?: string;
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
}