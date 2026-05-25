import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RecepcionLecheService, Proveedor } from '../../core/services/recepcion-leche';
import { NotificationService } from '../../core/services/notification';
import { AuthService } from '../../core/services/auth';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-proveedores',
  imports: [CommonModule, FormsModule],
  templateUrl: './proveedores.html',
})
export class Proveedores implements OnInit {

  proveedores: Proveedor[] = [];
  filtro = '';
  cargando = false;
  guardando = false;
  error = '';

  modalAbierto = false;
  proveedorEditando: Proveedor | null = null;
  nombre = '';
  activo = true;

  constructor(
    private service: RecepcionLecheService,
    private notification: NotificationService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarProveedores();
  }

  cargarProveedores(): void {
    this.cargando = true;
    this.error = '';

    this.service.listarTodosProveedores().subscribe({
      next: (data) => {
        this.proveedores = data.slice().sort((a, b) => a.nombre.localeCompare(b.nombre));
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.error = err.error?.message || 'No se pudieron cargar los proveedores.';
        this.notification.error(this.error);
        this.cargando = false;
      }
    });
  }

  get proveedoresFiltrados(): Proveedor[] {
    const termino = this.filtro.toLowerCase().trim();
    if (!termino) return this.proveedores;
    return this.proveedores.filter(proveedor => proveedor.nombre.toLowerCase().includes(termino));
  }

  abrirNuevo(): void {
    this.proveedorEditando = null;
    this.nombre = '';
    this.activo = true;
    this.modalAbierto = true;
  }

  abrirEditar(proveedor: Proveedor): void {
    this.proveedorEditando = proveedor;
    this.nombre = proveedor.nombre;
    this.activo = proveedor.activo;
    this.modalAbierto = true;
  }

  cerrarModal(): void {
    this.modalAbierto = false;
    this.proveedorEditando = null;
    this.nombre = '';
    this.activo = true;
  }

  guardar(): void {
    const nombreLimpio = this.nombre.trim();
    if (!nombreLimpio) {
      this.notification.warning('El nombre del proveedor es obligatorio.');
      return;
    }

    this.guardando = true;
    const request = { nombre: nombreLimpio, activo: this.activo };
    const operacion = this.proveedorEditando
      ? this.service.actualizarProveedor(this.proveedorEditando.id, request)
      : this.service.crearProveedor(request);

    operacion.subscribe({
      next: () => {
        this.guardando = false;
        this.notification.success(
          this.proveedorEditando ? 'Proveedor actualizado correctamente.' : 'Proveedor creado correctamente.'
        );
        this.cerrarModal();
        this.cargarProveedores();
      },
      error: (err) => {
        console.error(err);
        this.guardando = false;
        this.notification.error(err.error?.message || 'No se pudo guardar el proveedor.');
      }
    });
  }

  toggleEstado(proveedor: Proveedor): void {
    if (!this.authService.canManageCatalogosTecnicos()) return;
    const payload = { ...proveedor, activo: !proveedor.activo };
    this.service.actualizarProveedor(proveedor.id!, payload).subscribe({
      next: () => {
        this.notification.success(`Proveedor ${payload.activo ? 'activado' : 'inactivado'} correctamente.`);
        this.cargarProveedores();
      },
      error: (err) => {
        this.notification.error(err.error?.message || 'Error al cambiar el estado.');
      }
    });
  }

  eliminarProveedor(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción eliminará permanentemente este proveedor.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#059669', // emerald-600
      cancelButtonColor: '#ef4444', // red-500
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.service.eliminarProveedor(id).subscribe({
          next: () => {
            this.notification.success('Proveedor eliminado correctamente.');
            this.cargarProveedores();
          },
          error: (err) => {
            this.notification.error(err.error?.message || 'Error al eliminar el proveedor.');
          }
        });
      }
    });
  }

  colorProveedor(proveedor: string): string {
    const colores = [
      'bg-blue-100 text-blue-700',
      'bg-emerald-100 text-emerald-700',
      'bg-amber-100 text-amber-700',
      'bg-violet-100 text-violet-700',
      'bg-rose-100 text-rose-700',
      'bg-cyan-100 text-cyan-700'
    ];
    const total = proveedor.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    return colores[total % colores.length];
  }
}
