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

    if (!this.cc || !this.password) {
      this.error = 'Debe ingresar cédula y contraseña.';
      return;
    }

    this.cargando = true;

    this.authService.login({
      cc: this.cc,
      password: this.password
    }).subscribe({
      next: (response) => {
        this.cargando = false;
        
        // Redirección inteligente según el rol (normalización para evitar fallos por espacios o mayúsculas)
        const rol = (response.rol || '').trim().toUpperCase();
        
        console.log('Login exitoso. Rol detectado:', rol);

        if (rol.includes('JEFE_PRODUCCION')) {
          this.router.navigate(['/programacion-produccion/nueva']);
        } else if (rol.includes('JEFE_LINEA')) {
          this.router.navigate(['/ordenes-produccion']);
        } else {
          // ADMIN, DUENO_EMPRESA, JEFE_PLANTA van al Dashboard
          this.router.navigate(['/dashboard']);
        }
      },
      error: (err) => {
        this.cargando = false;
        console.error('Error en login:', err);
        const msg = err.error?.message || 'Credenciales incorrectas o usuario inactivo.';
        this.error = msg;
      }
    });
  }
}
