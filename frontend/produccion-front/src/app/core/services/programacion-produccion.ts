import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

import {
    SimularProgramacionRequest
} from '../models/programacion/simular-programacion.request';

import {
    SimularProgramacionResponse
} from '../models/programacion/simular-programacion.response';

@Injectable({
    providedIn: 'root'
})
export class ProgramacionProduccionService {

    private http = inject(HttpClient);

    private readonly baseUrl = environment.apiUrl;

    private readonly programacionesUrl = `${this.baseUrl}/programaciones`;
    private readonly catalogosUrl = `${this.baseUrl}/catalogos`;
    private readonly formulasUrl = `${this.baseUrl}/formulas`;

    listarProductos(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.catalogosUrl}/productos`
        );
    }

    listarTurnos(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.catalogosUrl}/turnos`
        );
    }

    listarSkusPorProducto(idProducto: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.catalogosUrl}/skus?idProducto=${idProducto}`
        );
    }

    obtenerFormulaVigente(idProducto: number): Observable<any> {
        return this.http.get<any>(
            `${this.formulasUrl}/producto/${idProducto}/vigente`
        );
    }

    simularProgramacion(
        request: SimularProgramacionRequest
    ): Observable<SimularProgramacionResponse> {
        return this.http.post<SimularProgramacionResponse>(
            `${this.programacionesUrl}/simular`,
            request
        );
    }

    crearProgramacion(body: any): Observable<any> {
        return this.http.post(
            this.programacionesUrl,
            body
        );
    }

    agregarSku(body: any): Observable<any> {
        return this.http.post(
            `${this.baseUrl}/programacion-skus`,
            body
        );
    }
}
