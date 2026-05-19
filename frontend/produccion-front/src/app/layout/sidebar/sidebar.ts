import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  
})
export class Sidebar {

  constructor(public authService: AuthService) { }

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
