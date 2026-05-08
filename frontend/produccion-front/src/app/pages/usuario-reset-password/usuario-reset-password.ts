import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import {
  Usuario,
  UsuarioService
} from '../../core/services/usuario';

@Component({
  selector: 'app-usuario-reset-password',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './usuario-reset-password.html',
  styleUrl: './usuario-reset-password.scss',
})
export class UsuarioResetPassword implements OnInit {

  usuarioId: number | null = null;
  usuario: Usuario | null = null;

  cargando = false;
  error = '';

  form;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      nuevaPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmarPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.router.navigate(['/usuarios']);
      return;
    }

    this.usuarioId = Number(id);
    this.cargarUsuario(this.usuarioId);
  }

  cargarUsuario(id: number): void {
    this.cargando = true;

    this.usuarioService.obtenerPorId(id).subscribe({
      next: (usuario) => {
        this.usuario = usuario;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar la información del usuario.';
        this.cargando = false;
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (!this.usuarioId) {
      return;
    }

    const nuevaPassword = this.form.value.nuevaPassword || '';
    const confirmarPassword = this.form.value.confirmarPassword || '';

    if (nuevaPassword !== confirmarPassword) {
      this.error = 'Las contraseñas no coinciden.';
      return;
    }

    this.cargando = true;
    this.error = '';

    this.usuarioService.resetPassword(this.usuarioId, {
      nuevaPassword
    }).subscribe({
      next: () => {
        this.router.navigate(['/usuarios']);
      },
      error: () => {
        this.error = 'No se pudo actualizar la contraseña.';
        this.cargando = false;
      }
    });
  }
}