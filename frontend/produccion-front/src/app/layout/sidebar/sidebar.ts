import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {

  @Input() mobileOpen = false;
  @Output() closeMobile = new EventEmitter<void>();

  administracionAbierto = true;
  controlAbierto = true;
  operacionesAbierto = true;
  produccionAbierto = true;

  constructor(public authService: AuthService, private router: Router) { }

  cerrarMenuMovil(): void {
    this.closeMobile.emit();
  }

  toggleAdministracion(): void {
    this.administracionAbierto = !this.administracionAbierto;
  }

  toggleControl(): void {
    this.controlAbierto = !this.controlAbierto;
  }

  toggleOperaciones(): void {
    this.operacionesAbierto = !this.operacionesAbierto;
  }

  toggleProduccion(): void {
    this.produccionAbierto = !this.produccionAbierto;
  }

  cerrarSesion(): void {
    this.authService.logout();
    this.closeMobile.emit();
    this.router.navigate(['/login']);
  }

  numeroModuloProduccion(modulo: 'programacion' | 'ordenes' | 'ejecucion' | 'calidad' | 'reportes'): number {
    const modulos = [
      { clave: 'programacion', visible: this.authService.canManageProgramacion() },
      { clave: 'ordenes', visible: this.authService.canReadOperaciones() },
      { clave: 'ejecucion', visible: this.authService.canReadOperaciones() },
      { clave: 'calidad', visible: this.authService.canReadCalidad() },
      { clave: 'reportes', visible: this.authService.canViewReportes() }
    ];

    return modulos
      .filter(item => item.visible)
      .findIndex(item => item.clave === modulo) + 1;
  }

}