import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ProgramacionProduccionService } from '../../core/services/programacion-produccion';
import { NotificationService } from '../../core/services/notification';

import {
  SimularSkuRequest
} from '../../core/models/programacion/simular-programacion.request';

import {
  Usuario,
  UsuarioService
} from '../../core/services/usuario';

@Component({
  selector: 'app-programacion-produccion-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './programacion-produccion-form.html',
})
export class ProgramacionProduccionForm implements OnInit {

  private programacionService = inject(ProgramacionProduccionService);
  private usuarioService = inject(UsuarioService);
  private notification = inject(NotificationService);

  productos: any[] = [];
  turnos: any[] = [];
  skusDisponibles: any[] = [];
  formulaVigente: any | null = null;
  jefesLinea: Usuario[] = [];

  idProducto: number | null = null;
  idJefeLineaEjecutor: number | null = null;
  idTurno: number | null = null;

  fechaProduccion = new Date().toISOString().substring(0, 10);
  observaciones = '';

  skus: SimularSkuRequest[] = [
    {
      idSku: 0,
      unidades: 0
    }
  ];

  cargandoFormula = false;
  guardando = false;

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarTurnos();
    this.cargarJefesLinea();
  }

  cargarProductos(): void {
    this.programacionService.listarProductos().subscribe({
      next: productos => this.productos = productos,
      error: error => console.error('Error cargando productos', error)
    });
  }

  cargarTurnos(): void {
    this.programacionService.listarTurnos().subscribe({
      next: turnos => this.turnos = turnos,
      error: error => console.error('Error cargando turnos', error)
    });
  }

  cargarJefesLinea(): void {
    this.usuarioService.listarPorRol('JEFE_LINEA').subscribe({
      next: usuarios => this.jefesLinea = usuarios,
      error: error => console.error('Error cargando jefes de línea', error)
    });
  }

  onProductoChange(): void {
    this.skusDisponibles = [];
    this.formulaVigente = null;
    this.skus = [{ idSku: 0, unidades: 0 }];

    if (!this.idProducto) return;

    this.cargarSkusPorProducto(this.idProducto);
    this.cargarFormulaVigente(this.idProducto);
  }

  cargarSkusPorProducto(idProducto: number): void {
    this.programacionService.listarSkusPorProducto(idProducto).subscribe({
      next: skus => this.skusDisponibles = skus,
      error: error => console.error('Error cargando SKUs', error)
    });
  }

  cargarFormulaVigente(idProducto: number): void {
    this.cargandoFormula = true;

    this.programacionService.obtenerFormulaVigente(idProducto).subscribe({
      next: formula => {
        this.formulaVigente = formula;
        this.cargandoFormula = false;
      },
      error: error => {
        console.error('Error cargando fórmula vigente', error);
        this.cargandoFormula = false;
      }
    });
  }

  obtenerProductoSeleccionado(): any | undefined {
    return this.productos.find(
      producto => Number(producto.id) === Number(this.idProducto)
    );
  }

  obtenerNombreLinea(): string {
    const producto = this.obtenerProductoSeleccionado();
    return producto?.nombreLinea || producto?.lineaNombre || producto?.linea || '-';
  }

  agregarFila(): void {
    this.skus.push({ idSku: 0, unidades: 0 });
  }

  eliminarFila(index: number): void {
    this.skus.splice(index, 1);

    if (this.skus.length === 0) {
      this.agregarFila();
    }
  }

  obtenerSku(idSku: number): any | undefined {
    return this.skusDisponibles.find(
      sku => Number(sku.id) === Number(idSku)
    );
  }

  obtenerKgBatchFormula(): number {
    return Number(this.formulaVigente?.kgBatchTotal || 0);
  }

  obtenerRendimientoFormula(): number {
    return Number(this.formulaVigente?.rendimientoTeoricoPct || 0);
  }

  calcularKgProductoTerminado(fila: SimularSkuRequest): number {
    const sku = this.obtenerSku(fila.idSku);

    if (!sku || !fila.unidades) return 0;

    return Number(((Number(fila.unidades) * Number(sku.pesoNetoGr)) / 1000).toFixed(3));
  }

  calcularKgEntradaRequeridos(fila: SimularSkuRequest): number {
    const kgPt = this.calcularKgProductoTerminado(fila);
    const rendimiento = this.obtenerRendimientoFormula();

    if (kgPt <= 0 || rendimiento <= 0) return 0;

    return Number((kgPt / (rendimiento / 100)).toFixed(3));
  }

  calcularBatchesTeoricos(fila: SimularSkuRequest): number {
    const kgEntrada = this.calcularKgEntradaRequeridos(fila);
    const kgBatchFormula = this.obtenerKgBatchFormula();

    if (kgEntrada <= 0 || kgBatchFormula <= 0) return 0;

    return Number((kgEntrada / kgBatchFormula).toFixed(3));
  }

  calcularBatchesOperativos(fila: SimularSkuRequest): number {
    return Math.ceil(this.calcularBatchesTeoricos(fila));
  }

  calcularTotalKgPt(): number {
    return Number(
      this.skus
        .reduce((total, fila) => total + this.calcularKgProductoTerminado(fila), 0)
        .toFixed(3)
    );
  }

  calcularTotalKgEntrada(): number {
    return Number(
      this.skus
        .reduce((total, fila) => total + this.calcularKgEntradaRequeridos(fila), 0)
        .toFixed(3)
    );
  }

  calcularTotalBatchesTeoricos(): number {
    return Number(
      this.skus
        .reduce((total, fila) => total + this.calcularBatchesTeoricos(fila), 0)
        .toFixed(3)
    );
  }

  calcularTotalBatchesOperativos(): number {
    return this.skus
      .reduce((total, fila) => total + this.calcularBatchesOperativos(fila), 0);
  }

  tieneSkusValidos(): boolean {
    return this.skus.some(
      fila => Number(fila.idSku) > 0 && Number(fila.unidades) > 0
    );
  }

  crearProgramacion(): void {
    if (
      !this.idProducto ||
      !this.formulaVigente ||
      !this.tieneSkusValidos() ||
      !this.idJefeLineaEjecutor ||
      !this.idTurno ||
      !this.fechaProduccion
    ) {
      this.notification.warning('Seleccione fecha, producto, turno, jefe de línea y al menos un SKU.');
      return;
    }

    const productoSeleccionado = this.obtenerProductoSeleccionado();

    if (!productoSeleccionado) {
      this.notification.error('No se encontró el producto seleccionado.');
      return;
    }

    const skusValidos = this.skus
      .filter(fila => Number(fila.idSku) > 0 && Number(fila.unidades) > 0)
      .map(fila => ({
        idSku: Number(fila.idSku),
        unidadesObjetivo: Number(fila.unidades)
      }));

    const body = {
      fechaProduccion: this.fechaProduccion,
      idLinea: Number(productoSeleccionado.idLinea),
      idProducto: Number(this.idProducto),
      idTurno: Number(this.idTurno),
      idJefeLineaEjecutor: Number(this.idJefeLineaEjecutor),
      numBachesPlan: this.calcularTotalBatchesOperativos(),
      kgBachePlan: this.obtenerKgBatchFormula(),
      idFormulaVersion: Number(this.formulaVigente.idFormulaVersion ?? this.formulaVigente.id),
      observaciones: this.observaciones?.trim() || 'Programación generada desde pantalla tipo Excel',
      skus: skusValidos
    };

    this.guardando = true;

    this.programacionService.crearProgramacion(body).subscribe({
      next: () => {
        this.guardando = false;
        this.notification.success('Programación creada correctamente.');

        this.idProducto = null;
        this.idJefeLineaEjecutor = null;
        this.idTurno = null;
        this.fechaProduccion = new Date().toISOString().substring(0, 10);
        this.observaciones = '';
        this.formulaVigente = null;
        this.skusDisponibles = [];
        this.skus = [{ idSku: 0, unidades: 0 }];

        window.scrollTo({ top: 0, behavior: 'smooth' });
      },
      error: error => {
        this.guardando = false;
        const msg = error?.error?.message || 'Error creando programación.';
        this.notification.error(msg);
      }
    });
  }
}
