import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CatalogoService {

    private http = inject(HttpClient);
    private readonly baseUrl = `${environment.apiUrl}/catalogos`;

    // Productos
    listarProductos(activos: boolean = true): Observable<any[]> {
        return this.http.get<any[]>(`${this.baseUrl}/productos?activos=${activos}`);
    }

    // Marcas
    listarMarcas(activos: boolean = true): Observable<any[]> {
        return this.http.get<any[]>(`${this.baseUrl}/marcas?activos=${activos}`);
    }

    // SKUs
    listarSkus(activos: boolean = true): Observable<any[]> {
        return this.http.get<any[]>(`${this.baseUrl}/skus?activos=${activos}`);
    }

    obtenerSku(codigo: string): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/skus/${codigo}`);
    }

    crearSku(body: any): Observable<any> {
        return this.http.post(`${this.baseUrl}/skus`, body);
    }

    actualizarSku(id: number, body: any): Observable<any> {
        return this.http.put(`${this.baseUrl}/skus/${id}`, body);
    }

    listarMarmitas(activos: boolean = true): Observable<any[]> {
        return this.http.get<any[]>(`${this.baseUrl}/marmitas?activos=${activos}`);
    }
}
