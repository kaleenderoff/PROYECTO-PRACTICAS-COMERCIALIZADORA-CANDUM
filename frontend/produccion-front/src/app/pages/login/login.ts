import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
})
export class Login {

  cc = '';
  password = '';
  error = '';
  cargando = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  iniciarSesion(): void {
    this.error = '';

    if (!this.cc.trim()) {
      this.error = 'Ingrese su número de cédula.';
      return;
    }

    if (!this.password) {
      this.error = 'Ingrese su contraseña.';
      return;
    }

    this.cargando = true;

    this.authService.login({
      cc: this.cc.trim(),
      password: this.password
    }).subscribe({
      next: (response) => {
        this.cargando = false;

        const rol = (response.rol || '').trim().toUpperCase();

        if (rol.includes('JEFE_PRODUCCION')) {
          this.router.navigate(['/programacion-produccion/nueva']);
        } else if (rol.includes('JEFE_LINEA')) {
          this.router.navigate(['/ordenes-produccion']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      },
      error: (err) => {
        this.cargando = false;
        this.error = this.resolverMensajeError(err);
      }
    });
  }

  private resolverMensajeError(err: any): string {
    const status = err?.status;
    const mensaje = err?.error?.message;

    // 404 → usuario no existe
    if (status === 404) {
      return 'No existe un usuario registrado con esa cédula.';
    }

    // 401 → contraseña incorrecta
    if (status === 401) {
      return 'Contraseña incorrecta. Verifique sus credenciales.';
    }

    // 403 → usuario inactivo
    if (status === 403) {
      return 'Su cuenta está inactiva. Contacte al administrador.';
    }

    // 0 → sin conexión
    if (status === 0) {
      return 'No se pudo conectar con el servidor. Intente más tarde.';
    }

    // Mensaje específico del backend como fallback
    if (mensaje) {
      return mensaje;
    }

    return 'Ocurrió un error al iniciar sesión. Intente nuevamente.';
  }
}
