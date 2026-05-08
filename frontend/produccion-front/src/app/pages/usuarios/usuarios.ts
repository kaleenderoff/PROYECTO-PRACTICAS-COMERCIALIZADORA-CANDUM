import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Usuario, UsuarioService } from '../../core/services/usuario';

@Component({
  selector: 'app-usuarios',
  imports: [CommonModule, RouterLink],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.scss',
})
export class Usuarios implements OnInit {

  usuarios: Usuario[] = [];
  cargando = false;
  error = '';

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
}