import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DashboardService, ProduccionVsEmpaque, DashboardGerencial, DashboardProduccionSku } from '../../core/services/dashboard';
import { RecepcionLecheService, SaldoTanqueLeche } from '../../core/services/recepcion-leche';
import { AuthService } from '../../core/services/auth';

import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule, BaseChartDirective],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {

  view: string = 'general';

  // Datos Operativos (Astrid / Diana)
  datos: ProduccionVsEmpaque[] = [];
  tanques: SaldoTanqueLeche[] = [];
  skus: DashboardProduccionSku[] = [];
  
  // Datos Gerenciales (Rafael)
  gerencial: DashboardGerencial | null = null;

  // Gráficas
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: true, position: 'bottom' },
    },
    scales: {
      y: { 
        beginAtZero: true, 
        grid: { display: true, color: '#f1f5f9' },
        title: { display: true, text: 'PRODUCCIÓN TOTAL (KG)', font: { weight: 'bold' } }
      },
      y1: { 
        position: 'right', 
        beginAtZero: true, 
        max: 60,
        grid: { display: false },
        title: { display: true, text: '% RENDIMIENTO', font: { weight: 'bold' } }
      }
    }
  };

  public barChartData: ChartData<'bar' | 'line'> = {
    labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
    datasets: [
      { data: [], label: 'PT Dulce Leche (Kg)', type: 'bar', backgroundColor: '#3b82f6', borderRadius: 8 },
      { data: [], label: 'PT Condensada (Kg)', type: 'bar', backgroundColor: '#10b981', borderRadius: 8 },
      { data: [], label: 'Rend. DL (%)', type: 'line', borderColor: '#f59e0b', yAxisID: 'y1', tension: 0.4, fill: false, pointBackgroundColor: '#f59e0b' },
      { data: [], label: 'Rend. LC (%)', type: 'line', borderColor: '#ef4444', yAxisID: 'y1', tension: 0.4, fill: false, pointBackgroundColor: '#ef4444' }
    ]
  };

  public donutChartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: true, position: 'right' },
    },
    cutout: '70%'
  };

  public donutChartData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [{ data: [], backgroundColor: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'] }]
  };

  public weeklyChartData: ChartData<'bar' | 'line'> = {
    labels: [],
    datasets: [
      { data: [], label: 'PT Dulce dL (Kg)', type: 'bar', backgroundColor: '#5bc0de', borderRadius: 0 },
      { data: [], label: 'PT L. Cond. (Kg)', type: 'bar', backgroundColor: '#6b8e23', borderRadius: 0 },
      { data: [], label: 'Rend. 1 Dulce (%)', type: 'line', borderColor: '#2f5597', yAxisID: 'y1', tension: 0, fill: false, pointBackgroundColor: '#2f5597', pointRadius: 4 },
      { data: [], label: 'Rend. 2 Dulce (%)', type: 'line', borderColor: '#000000', yAxisID: 'y1', tension: 0, fill: false, pointBackgroundColor: '#000000', pointRadius: 4 },
      { data: [], label: 'Rend. L. Cond. (%)', type: 'line', borderColor: '#7030a0', yAxisID: 'y1', tension: 0, fill: false, pointBackgroundColor: '#7030a0', pointRadius: 4 }
    ]
  };

  totalProducido = 0;
  totalEmpacado = 0;
  totalDiferencia = 0;
  totalLecheDisponible = 0;

  cargando = false;
  error = '';

  mesActual = new Date().getMonth() + 1;
  anioActual = new Date().getFullYear();

  constructor(
    private dashboardService: DashboardService,
    private recepcionService: RecepcionLecheService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.cargarProduccionVsEmpaque();
    this.cargarSaldosTanques();
    
    if (this.authService.isGerencia() || this.authService.isJefeProduccion()) {
      this.cargarDatosGerenciales();
      this.cargarProduccionSku();
      this.cargarGraficas();
    }
  }

  cargarProduccionVsEmpaque(): void {
    this.dashboardService.obtenerProduccionVsEmpaque().subscribe({
      next: (response: ProduccionVsEmpaque[]) => {
        this.datos = response;
        this.totalProducido = response.reduce((acc, item) => acc + Number(item.totalProducido), 0);
        this.totalEmpacado = response.reduce((acc, item) => acc + Number(item.totalEmpaquetado), 0);
        this.totalDiferencia = response.reduce((acc, item) => acc + Number(item.diferencia), 0);
      },
      error: () => this.error = 'Error en dashboard operativo'
    });
  }

  cargarSaldosTanques(): void {
    this.recepcionService.listarSaldosTanques().subscribe({
      next: (data) => {
        this.tanques = data.filter(t => t.activo);
        this.totalLecheDisponible = this.tanques.reduce((acc, t) => acc + t.saldoLitros, 0);
      }
    });
  }

  cargarDatosGerenciales(): void {
    this.dashboardService.obtenerGerencial(this.mesActual, this.anioActual).subscribe({
      next: (data) => {
        this.gerencial = data;
        
        // Populate Weekly Chart (Mirroring Excel)
        if (data.tablaSemanal && data.tablaSemanal.length > 0) {
          const labels = data.tablaSemanal.map(s => `Sem. ${s.numeroSemana}`);
          const d_dl = data.tablaSemanal.map(s => s.ptDulceLeche || 0);
          const d_lc = data.tablaSemanal.map(s => s.ptLecheCondensada || 0);
          const r1_dl = data.tablaSemanal.map(s => s.rend1DulceLeche || 0);
          const r2_dl = data.tablaSemanal.map(s => s.rend2DulceLeche || 0);
          const r_lc = data.tablaSemanal.map(s => s.rendLecheCondensada || 0);

          this.weeklyChartData = {
            labels: labels,
            datasets: [
              { ...this.weeklyChartData.datasets[0], data: d_dl },
              { ...this.weeklyChartData.datasets[1], data: d_lc },
              { ...this.weeklyChartData.datasets[2], data: r1_dl },
              { ...this.weeklyChartData.datasets[3], data: r2_dl },
              { ...this.weeklyChartData.datasets[4], data: r_lc }
            ]
          };
        } else {
          this.weeklyChartData = { ...this.weeklyChartData, labels: [], datasets: this.weeklyChartData.datasets.map(d => ({ ...d, data: [] })) };
        }
      }
    });
  }

  cargarProduccionSku(): void {
    this.dashboardService.obtenerProduccionSku(this.mesActual, this.anioActual).subscribe({
      next: (data) => {
        this.skus = data;
        this.cargando = false;
        
        // Populate Donut Chart
        const allSkus = data.flatMap(cat => cat.items);
        this.totalProducido = allSkus.reduce((acc, s) => acc + s.kilosMes, 0);
        
        const topSkus = allSkus.sort((a, b) => b.kilosMes - a.kilosMes).slice(0, 6);
        
        this.donutChartData = {
          labels: topSkus.map(s => s.sku.split(' ').slice(0, 3).join(' ')),
          datasets: [{
            ...this.donutChartData.datasets[0],
            data: topSkus.map(s => s.kilosMes)
          }]
        };

        // Calculate Category Totals
        this.skus = data.map(cat => ({
          ...cat,
          totalKilos: cat.items.reduce((acc, item) => acc + item.kilosMes, 0),
          totalUnidades: cat.items.reduce((acc, item) => acc + item.unidadesMes, 0)
        }));
      },
      error: () => this.cargando = false
    });
  }

  cargarGraficas(): void {
    this.dashboardService.obtenerRendimientoAnual(this.anioActual).subscribe({
      next: (data) => {
        const d_dl = data.map(d => d.ptDulceLeche);
        const d_lc = data.map(d => d.ptLecheCondensada);
        const r_dl = data.map(d => d.rendimientoDL);
        const r_lc = data.map(d => d.rendimientoLC);

        this.barChartData = {
          ...this.barChartData,
          datasets: [
            { ...this.barChartData.datasets[0], data: d_dl },
            { ...this.barChartData.datasets[1], data: d_lc },
            { ...this.barChartData.datasets[2], data: r_dl },
            { ...this.barChartData.datasets[3], data: r_lc }
          ]
        };
      }
    });
  }

  // Helpers para la UI
  getPorcentajeGrasa(leche: number): string {
    return ((leche * 0.04) / 1).toFixed(2); // Ejemplo
  }
}
