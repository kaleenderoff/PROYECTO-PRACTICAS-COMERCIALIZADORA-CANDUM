import { Routes } from '@angular/router';

import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';

import { MainLayout } from './layout/main-layout/main-layout';

import { authGuard } from './core/guards/auth-guard';

import { RecepcionLeche } from './pages/recepcion-leche/recepcion-leche';

import { RecepcionLecheForm } from './pages/recepcion-leche-form/recepcion-leche-form';

import { Usuarios } from './pages/usuarios/usuarios';

import { UsuarioForm } from './pages/usuario-form/usuario-form';

import { UsuarioResetPassword } from './pages/usuario-reset-password/usuario-reset-password';

export const routes: Routes = [

    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },

    {
        path: 'login',
        component: Login
    },

    {
        path: '',
        component: MainLayout,
        children: [

            {
                path: 'dashboard',
                component: Dashboard,
                canActivate: [authGuard]
            },

            {
                path: 'recepcion-leche',
                component: RecepcionLeche,
                canActivate: [authGuard]
            },

            {
                path: 'recepcion-leche/nueva',
                component: RecepcionLecheForm,
                canActivate: [authGuard]
            },

            {
                path: 'usuarios',
                component: Usuarios,
                canActivate: [authGuard]
            },

            {
                path: 'usuarios/nuevo',
                component: UsuarioForm,
                canActivate: [authGuard]
            },
            {
                path: 'usuarios/:id/editar',
                component: UsuarioForm,
                canActivate: [authGuard]
            },

            {
                path: 'usuarios/:id/reset-password',
                component: UsuarioResetPassword,
                canActivate: [authGuard]
            }

        ]
    },

    {
        path: '**',
        redirectTo: 'login'
    }

];