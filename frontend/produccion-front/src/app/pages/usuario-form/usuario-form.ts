import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import {
  ActualizarUsuarioRequest,
  CrearUsuarioRequest,
  Usuario,
  UsuarioService
} from '../../core/services/usuario';

@Component({
  selector: 'app-usuario-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './usuario-form.html',
  styleUrl: './usuario-form.scss',
})
export class UsuarioForm implements OnInit {

  editando = false;
  usuarioId: number | null = null;

  cargando = false;
  error = '';

  roles = [
    'ADMIN',
    'DUENO_EMPRESA',
    'JEFE_PLANTA',
    'JEFE_PRODUCCION',
    'JEFE_LINEA',
    'AUXILIAR_CALIDAD'
  ];

  form;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router,
    private route: ActivatedRoute
  ) {

    this.form = this.fb.group({

      cc: ['', Validators.required],

      primerNombre: ['', Validators.required],

      segundoNombre: [''],

      primerApellido: ['', Validators.required],

      segundoApellido: [''],

      password: [''],

      rol: ['', Validators.required],

      activo: [true]

    });

  }

  ngOnInit(): void {

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {

      this.editando = true;
      this.usuarioId = Number(id);

      this.cargarUsuario(this.usuarioId);

    }

  }

  cargarUsuario(id: number): void {
    this.cargando = true;

    this.usuarioService.obtenerPorId(id).subscribe({
      next: (usuario) => {
        this.form.patchValue({
          cc: usuario.cc,
          primerNombre: usuario.primerNombre,
          segundoNombre: usuario.segundoNombre || '',
          primerApellido: usuario.primerApellido,
          segundoApellido: usuario.segundoApellido || '',
          rol: usuario.rol,
          activo: usuario.activo
        });

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

    this.cargando = true;
    this.error = '';

    if (this.editando) {

      this.actualizar();

    } else {

      this.crear();

    }

  }

  crear(): void {

    const request: CrearUsuarioRequest = {

      cc: this.form.value.cc!,
      primerNombre: this.form.value.primerNombre!,
      segundoNombre: this.form.value.segundoNombre || '',
      primerApellido: this.form.value.primerApellido!,
      segundoApellido: this.form.value.segundoApellido || '',
      password: this.form.value.password!,
      rol: this.form.value.rol!

    };

    this.usuarioService.crear(request).subscribe({

      next: () => {

        this.router.navigate(['/usuarios']);

      },

      error: () => {

        this.cargando = false;
        this.error = 'No se pudo crear el usuario.';

      }

    });

  }

  actualizar(): void {

    if (!this.usuarioId) {
      return;
    }

    const request: ActualizarUsuarioRequest = {

      cc: this.form.value.cc!,
      primerNombre: this.form.value.primerNombre!,
      segundoNombre: this.form.value.segundoNombre || '',
      primerApellido: this.form.value.primerApellido!,
      segundoApellido: this.form.value.segundoApellido || '',
      rol: this.form.value.rol!,
      activo: this.form.value.activo!

    };

    this.usuarioService.actualizar(
      this.usuarioId,
      request
    ).subscribe({

      next: () => {

        this.router.navigate(['/usuarios']);

      },

      error: () => {

        this.cargando = false;
        this.error = 'No se pudo actualizar el usuario.';

      }

    });

  }

}