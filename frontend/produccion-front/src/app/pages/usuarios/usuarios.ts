import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Usuario, UsuarioService } from '../../core/services/usuario';

import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-usuarios',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './usuarios.html',
  
})
export class Usuarios implements OnInit {

  usuarios: Usuario[] = [];
  cargando = false;
  error = '';
  filtro = '';

  // Paginación
  paginaActual = 1;
  itemsPorPagina = 10;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.cargando = true;
    this.error = '';

    this.usuarioService.listarTodos().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.paginaActual = 1;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los usuarios.';
        this.cargando = false;
      }
    });
  }

  activar(usuario: Usuario): void {
    this.usuarioService.activar(usuario.idUsuario).subscribe({
      next: () => this.cargarUsuarios()
    });
  }

  desactivar(usuario: Usuario): void {
    this.usuarioService.desactivar(usuario.idUsuario).subscribe({
      next: () => this.cargarUsuarios()
    });
  }

  get usuariosFiltrados(): Usuario[] {
    if (!this.filtro.trim()) return this.usuarios;
    const f = this.filtro.toLowerCase();
    return this.usuarios.filter(u => 
      `${u.primerNombre} ${u.primerApellido}`.toLowerCase().includes(f) ||
      u.cc.toLowerCase().includes(f)
    );
  }

  get usuariosPaginados(): Usuario[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    return this.usuariosFiltrados.slice(inicio, inicio + this.itemsPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.usuariosFiltrados.length / this.itemsPorPagina);
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  cambiarPagina(p: number): void {
    if (p >= 1 && p <= this.totalPaginas) {
      this.paginaActual = p;
    }
  }
}
