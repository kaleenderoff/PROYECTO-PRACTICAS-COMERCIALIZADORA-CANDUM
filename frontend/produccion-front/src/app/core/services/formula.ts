import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class FormulaService {

    private http = inject(HttpClient);

    private readonly baseUrl = 'http://localhost:8082/api';
    private readonly catalogosUrl = `${this.baseUrl}/catalogos`;
    private readonly formulasUrl = `${this.baseUrl}/formulas`;

    listarInsumos(): Observable<any[]> {
        return this.http.get<any[]>(`${this.catalogosUrl}/insumos`);
    }

    crearInsumo(body: any): Observable<any> {
        return this.http.post(`${this.catalogosUrl}/insumos`, body);
    }

    actualizarInsumo(idInsumo: number, body: any): Observable<any> {
        return this.http.put(`${this.catalogosUrl}/insumos/${idInsumo}`, body);
    }

    listarFormulasPorProducto(idProducto: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.formulasUrl}/producto/${idProducto}`
        );
    }

    crearFormula(body: any): Observable<any> {
        return this.http.post(this.formulasUrl, body);
    }

    listarVersiones(idFormula: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.formulasUrl}/${idFormula}/versiones`
        );
    }

    crearVersion(idFormula: number, body: any): Observable<any> {
        return this.http.post(
            `${this.formulasUrl}/${idFormula}/versiones`,
            body
        );
    }

    agregarDetalle(idFormulaVersion: number, body: any): Observable<any> {
        return this.http.post(
            `${this.formulasUrl}/versiones/${idFormulaVersion}/detalles`,
            body
        );
    }

    actualizarDetalle(idDetalle: number, body: any): Observable<any> {
        return this.http.put(
            `${this.formulasUrl}/detalles/${idDetalle}`,
            body
        );
    }

    eliminarDetalle(idDetalle: number): Observable<void> {
        return this.http.delete<void>(
            `${this.formulasUrl}/detalles/${idDetalle}`
        );
    }

    marcarVersionVigente(idFormulaVersion: number): Observable<any> {
        return this.http.patch(
            `${this.formulasUrl}/versiones/${idFormulaVersion}/vigente`,
            {}
        );
    }

    obtenerFormulaVigente(idProducto: number): Observable<any> {
        return this.http.get<any>(
            `${this.formulasUrl}/producto/${idProducto}/vigente`
        );
    }
}