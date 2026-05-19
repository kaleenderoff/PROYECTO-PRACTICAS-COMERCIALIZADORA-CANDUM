import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';

import {
  OrdenProduccionResponse,
  OrdenProduccionService
} from '../../core/services/orden-produccion';
import { RecepcionLecheService, SaldoTanqueLeche } from '../../core/services/recepcion-leche';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-orden-produccion-detalle',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './orden-produccion-detalle.html',
})
export class OrdenProduccionDetalle implements OnInit {

  orden: OrdenProduccionResponse | null = null;
  cargando = false;
  error = '';
  tanques: SaldoTanqueLeche[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ordenService: OrdenProduccionService,
    private recepcionService: RecepcionLecheService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.cargarOrden(id);
      this.cargarTanques();
    }
  }

  cargarOrden(id: number): void {
    this.cargando = true;
    this.error = '';

    this.ordenService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.orden = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo cargar la orden de producción.';
        this.cargando = false;
      }
    });
  }

  iniciarEjecucion(): void {
    if (!this.authService.canWriteOperaciones()) return;

    if (!this.orden) return;
    
    // Para simplificar, usamos el ID del usuario actual si estuviera disponible, 
    // pero como el servicio pide un ID de jefe ejecutor, y el backend ya maneja 
    // seguridad, aquí pasamos un ID genérico o el del usuario si lo tuviéramos.
    // Usaremos el idUsuario del AuthService si fuera necesario.
    const idJefe = this.authService.getIdUsuario();
    if (!idJefe) {
      this.error = 'No se pudo identificar el usuario autenticado.';
      return;
    }

    this.cargando = true;
    this.ordenService.iniciar(this.orden.id, idJefe).subscribe({
      next: (updated) => {
        this.orden = updated;
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'No se pudo iniciar la orden.';
        this.cargando = false;
      }
    });
  }

  finalizarOrden(): void {
    if (!this.authService.canWriteOperaciones()) return;

    if (!this.orden) return;

    if (!this.orden.idTanqueLeche) {
      this.error = 'Seleccione el tanque de leche descremada antes de finalizar la orden.';
      return;
    }
    
    this.cargando = true;
    this.ordenService.finalizar(this.orden.id).subscribe({
      next: (updated) => {
        this.orden = updated;
        this.cargando = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'No se pudo finalizar la orden.';
        this.cargando = false;
      }
    });
  }

  registrarBatch(): void {
    if (!this.authService.canWriteOperaciones()) return;

    if (!this.orden) return;
    this.router.navigate([`/ordenes-produccion/${this.orden.id}/ejecutar`]);
  }

  obtenerClaseEstado(estado: string): string {
    switch (estado) {
      case 'PROGRAMADA': return 'bg-slate-100 text-slate-600 border-slate-200';
      case 'EN_EJECUCION': return 'bg-blue-50 text-blue-700 border-blue-100';
      case 'FINALIZADA': return 'bg-emerald-50 text-emerald-700 border-emerald-100';
      case 'CERRADA': return 'bg-slate-900 text-white border-slate-800';
      case 'CANCELADA': return 'bg-red-50 text-red-700 border-red-100';
      default: return 'bg-slate-50 text-slate-400';
    }
  }

  cargarTanques(): void {
    this.recepcionService.listarSaldosTanques().subscribe({
      next: (data) => this.tanques = data.filter(t => t.activo),
      error: (err) => console.error('Error cargando tanques', err)
    });
  }

  cambiarTanque(event: any): void {
    if (!this.authService.canWriteOperaciones()) return;

    const idTanque = Number(event.target.value);
    if (!this.orden || !idTanque) return;

    this.ordenService.actualizarTanqueLeche(this.orden.id, idTanque).subscribe({
      next: (data) => {
        this.orden = data;
      },
      error: (err) => {
        console.error(err);
        this.error = 'No se pudo actualizar el tanque de origen.';
      }
    });
  }
}
