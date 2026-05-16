import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

interface LoginRequest {
  cc: string;
  password: string;
}

interface LoginResponse {
  token: string;
  cc: string;
  rol: string;
  primerNombre: string;
  primerApellido: string;
  idUsuario: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly tokenKey = 'token';
  private readonly userKey = 'usuario';

  constructor(private http: HttpClient) { }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.token);
        localStorage.setItem(this.userKey, JSON.stringify({
          cc: response.cc,
          rol: response.rol,
          primerNombre: response.primerNombre,
          primerApellido: response.primerApellido,
          idUsuario: response.idUsuario
        }));
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUsuario(): any {
    const data = localStorage.getItem(this.userKey);
    return data ? JSON.parse(data) : null;
  }

  getRol(): string {
    return this.getUsuario()?.rol || '';
  }

  getIdUsuario(): number {
    return this.getUsuario()?.idUsuario || 0;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.getRol() === 'ADMIN';
  }

  isDuenoEmpresa(): boolean {
    return this.getRol() === 'DUENO_EMPRESA';
  }

  isJefePlanta(): boolean {
    return this.getRol() === 'JEFE_PLANTA';
  }

  isJefeProduccion(): boolean {
    return this.getRol() === 'JEFE_PRODUCCION';
  }

  isJefeLinea(): boolean {
    return this.getRol() === 'JEFE_LINEA';
  }

  isAuxiliarCalidad(): boolean {
    return this.getRol() === 'AUXILIAR_CALIDAD';
  }

  isGerencia(): boolean {
    const rol = this.getRol();
    return rol === 'ADMIN' || rol === 'DUENO_EMPRESA' || rol === 'JEFE_PLANTA';
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }
}
