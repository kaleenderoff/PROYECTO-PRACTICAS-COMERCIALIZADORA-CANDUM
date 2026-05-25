import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

import { AuditoriaResponse, AuditoriaService } from '../../core/services/auditoria';

@Component({
  selector: 'app-auditoria',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './auditoria.html',
})
export class Auditoria implements OnInit {

  registros: AuditoriaResponse[] = [];
  cargando = false;
  error = '';

  constructor(private auditoriaService: AuditoriaService) { }

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';

    this.auditoriaService.listarUltimos(150).subscribe({
      next: (data) => {
        this.registros = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cargar el historial de auditoria.';
        this.cargando = false;
      }
    });
  }

  claseAccion(accion: string): string {
    switch (accion) {
      case 'CREAR':
        return 'bg-emerald-50 text-emerald-700 border-emerald-100';
      case 'ACTUALIZAR':
      case 'CAMBIAR_ESTADO':
        return 'bg-blue-50 text-blue-700 border-blue-100';
      case 'ELIMINAR':
        return 'bg-red-50 text-red-700 border-red-100';
      default:
        return 'bg-slate-50 text-slate-600 border-slate-100';
    }
  }
}
