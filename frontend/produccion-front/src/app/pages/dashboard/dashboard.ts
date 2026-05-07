import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DashboardService, ProduccionVsEmpaque } from '../../core/services/dashboard';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {

  datos: ProduccionVsEmpaque[] = [];

  totalProducido = 0;
  totalEmpacado = 0;
  totalDiferencia = 0;

  cargando = false;
  error = '';

  constructor(private dashboardService: DashboardService) { }

  ngOnInit(): void {
    this.cargarProduccionVsEmpaque();
  }

  cargarProduccionVsEmpaque(): void {
    this.cargando = true;
    this.error = '';

    this.dashboardService.obtenerProduccionVsEmpaque().subscribe({
      next: (response) => {
        this.datos = response;

        this.totalProducido = response.reduce((acc, item) => acc + Number(item.totalProducido), 0);
        this.totalEmpacado = response.reduce((acc, item) => acc + Number(item.totalEmpaquetado), 0);
        this.totalDiferencia = response.reduce((acc, item) => acc + Number(item.diferencia), 0);

        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el dashboard.';
        this.cargando = false;
      }
    });
  }
}