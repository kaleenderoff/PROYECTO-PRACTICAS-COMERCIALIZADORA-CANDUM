import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Usuario {
  idUsuario: number;
  cc: string;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  rol: string;
  activo: boolean;
}

export interface CrearUsuarioRequest {
  cc: string;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  password: string;
  rol: string;
}

export interface ActualizarUsuarioRequest {
  cc: string;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  rol: string;
  activo: boolean;
}

export interface ResetPasswordRequest {
  nuevaPassword: string;
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private readonly apiUrl = 'http://localhost:8082/api/usuarios';

  constructor(private http: HttpClient) { }

  obtenerPorId(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  listarTodos(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/todos`);
  }

  listarPorRol(rol: string): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(
      `${this.apiUrl}/rol/${rol}`
    );
  }

  crear(request: CrearUsuarioRequest): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, request);
  }

  actualizar(id: number, request: ActualizarUsuarioRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, request);
  }

  resetPassword(id: number, request: ResetPasswordRequest): Observable<void> {
    return this.http.patch<void>(
      `${this.apiUrl}/${id}/reset-password`,
      request
    );
  }

  activar(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.apiUrl}/${id}/activar`,
      {}
    );
  }

  desactivar(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.apiUrl}/${id}/desactivar`,
      {}
    );
  }
}