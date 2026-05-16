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

import { Descremado } from './pages/descremado/descremado';
import { DescremadoForm } from './pages/descremado-form/descremado-form';

import { EjecucionProduccion } from './pages/ejecucion-produccion/ejecucion-produccion';
import { EjecucionProduccionForm } from './pages/ejecucion-produccion-form/ejecucion-produccion-form';

import { ProgramacionProduccionForm } from './pages/programacion-produccion-form/programacion-produccion-form';

import { Insumos } from './pages/insumos/insumos';

import { OrdenesProduccion } from './pages/ordenes-produccion/ordenes-produccion';

import { OrdenProduccionDetalle } from './pages/orden-produccion-detalle/orden-produccion-detalle';
import { OrdenEjecucion } from './pages/orden-ejecucion/orden-ejecucion';

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
            },

            {
                path: 'descremado',
                component: Descremado,
                canActivate: [authGuard]
            },

            {
                path: 'descremado/nuevo',
                component: DescremadoForm,
                canActivate: [authGuard]
            },

            {
                path: 'insumos',
                component: Insumos,
                canActivate: [authGuard]
            },

            {
                path: 'formulas',
                loadComponent: () =>
                    import('./pages/formulas/formulas').then(m => m.Formulas),
                canActivate: [authGuard]
            },

            {
                path: 'skus',
                loadComponent: () =>
                    import('./pages/skus/skus').then(m => m.Skus),
                canActivate: [authGuard]
            },

            {
                path: 'programacion-produccion/nueva',
                component: ProgramacionProduccionForm,
                canActivate: [authGuard]
            },

            {
                path: 'ordenes-produccion',
                component: OrdenesProduccion,
                canActivate: [authGuard]
            },

            {
                path: 'ejecucion-produccion',
                component: EjecucionProduccion,
                canActivate: [authGuard]
            },



            {
                path: 'ordenes-produccion/:id',
                component: OrdenProduccionDetalle,
                canActivate: [authGuard]
            },

            {
                path: 'ordenes-produccion/:id/ejecutar',
                component: OrdenEjecucion,
                canActivate: [authGuard]
            },

        ]
    },

    {
        path: '**',
        redirectTo: 'login'
    }

];
