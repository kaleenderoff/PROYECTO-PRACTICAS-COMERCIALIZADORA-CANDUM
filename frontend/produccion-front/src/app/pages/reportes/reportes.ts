import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ReporteLacteoService,
  ResumenConsumoInsumos,
  ResumenRecepcionDescremado
} from '../../core/services/reporte-lacteo';

type ReporteView = 'consumo' | 'recepcion' | 'produccion';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.html'
})
export class Reportes implements OnInit {

  view: ReporteView = 'consumo';

  fechaConsumo: string = this.hoy();
  fechaRecepcion: string = this.hoy();

  resumenConsumo: ResumenConsumoInsumos | null = null;
  resumenRecepcion: ResumenRecepcionDescremado | null = null;

  cargandoConsumo = false;
  cargandoRecepcion = false;
  descargando = false;
  errorConsumo = '';
  errorRecepcion = '';
  errorProduccion = '';

  constructor(private reporteService: ReporteLacteoService) {}

  ngOnInit(): void {
    this.cargarConsumo();
    this.cargarRecepcion();
  }

  setView(v: ReporteView): void {
    this.view = v;
  }

  hoy(): string {
    return new Date().toISOString().slice(0, 10);
  }

  // ─── CONSUMO INSUMOS ─────────────────────────────────────────────────────────

  cargarConsumo(): void {
    this.cargandoConsumo = true;
    this.errorConsumo = '';
    this.reporteService.obtenerConsumoInsumos(this.fechaConsumo).subscribe({
      next: (data) => {
        this.resumenConsumo = data;
        this.cargandoConsumo = false;
      },
      error: () => {
        this.resumenConsumo = null;
        this.errorConsumo = 'No hay datos para la fecha seleccionada.';
        this.cargandoConsumo = false;
      }
    });
  }

  descargarConsumoExcel(): void {
    this.descargando = true;
    this.reporteService.descargarExcelConsumoInsumos(this.fechaConsumo).subscribe({
      next: (blob) => {
        ReporteLacteoService.triggerDownload(blob, `consumo-insumos-${this.fechaConsumo}.xlsx`);
        this.descargando = false;
      },
      error: () => {
        this.descargando = false;
        alert('Error al generar el Excel. Verifique que haya datos para la fecha.');
      }
    });
  }

  // ─── RECEPCIÓN / DESCREMADO ──────────────────────────────────────────────────

  cargarRecepcion(): void {
    this.cargandoRecepcion = true;
    this.errorRecepcion = '';
    this.reporteService.obtenerRecepcionDescremado(this.fechaRecepcion).subscribe({
      next: (data) => {
        this.resumenRecepcion = data;
        this.cargandoRecepcion = false;
      },
      error: () => {
        this.resumenRecepcion = null;
        this.errorRecepcion = 'No hay datos para la fecha seleccionada.';
        this.cargandoRecepcion = false;
      }
    });
  }

  descargarRecepcionExcel(): void {
    this.descargando = true;
    this.reporteService.descargarExcelRecepcionDescremado(this.fechaRecepcion).subscribe({
      next: (blob) => {
        ReporteLacteoService.triggerDownload(blob, `recepcion-descremado-${this.fechaRecepcion}.xlsx`);
        this.descargando = false;
      },
      error: () => {
        this.descargando = false;
        alert('Error al generar el Excel. Verifique que haya datos para la fecha.');
      }
    });
  }

  // ─── PRODUCCIÓN VS EMPAQUE ───────────────────────────────────────────────────

  descargarProduccionExcel(): void {
    this.descargando = true;
    this.errorProduccion = '';
    this.reporteService.descargarExcelProduccionVsEmpaque().subscribe({
      next: (blob) => {
        const hoy = new Date().toISOString().slice(0, 10);
        ReporteLacteoService.triggerDownload(blob, `produccion-vs-empaque-${hoy}.xlsx`);
        this.descargando = false;
      },
      error: () => {
        this.descargando = false;
        this.errorProduccion = 'Error al generar el Excel.';
      }
    });
  }
}
