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
import { MedicionesCalidadLactea } from './pages/mediciones-calidad-lactea/mediciones-calidad-lactea';

const ADMIN = 'ADMIN';
const DUENO_EMPRESA = 'DUENO_EMPRESA';
const JEFE_PLANTA = 'JEFE_PLANTA';
const JEFE_PRODUCCION = 'JEFE_PRODUCCION';
const JEFE_LINEA = 'JEFE_LINEA';
const AUXILIAR_CALIDAD = 'AUXILIAR_CALIDAD';

const ROLES_OPERACION_LECTURA = [ADMIN, JEFE_LINEA, JEFE_PRODUCCION, JEFE_PLANTA, DUENO_EMPRESA];
const ROLES_RECEPCION_LECHE_LECTURA = [ADMIN, JEFE_LINEA, JEFE_PRODUCCION, JEFE_PLANTA, DUENO_EMPRESA, AUXILIAR_CALIDAD];
const ROLES_OPERACION_ESCRITURA = [ADMIN, JEFE_LINEA];
const ROLES_PROGRAMACION_ESCRITURA = [ADMIN, JEFE_PRODUCCION];
const ROLES_CATALOGOS_TECNICOS = [ADMIN, JEFE_PRODUCCION, JEFE_PLANTA, DUENO_EMPRESA];
const ROLES_REPORTES = [ADMIN, JEFE_PRODUCCION, JEFE_PLANTA, DUENO_EMPRESA];
const ROLES_CALIDAD_LECTURA = [ADMIN, AUXILIAR_CALIDAD, JEFE_PRODUCCION, JEFE_PLANTA, DUENO_EMPRESA];
const ROLES_USUARIOS_ADMIN = [ADMIN];

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
                canActivate: [authGuard],
                data: { roles: ROLES_REPORTES }
            },

            {
                path: 'recepcion-leche',
                component: RecepcionLeche,
                canActivate: [authGuard],
                data: { roles: ROLES_RECEPCION_LECHE_LECTURA }
            },

            {
                path: 'recepcion-leche/nueva',
                component: RecepcionLecheForm,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_ESCRITURA }
            },

            {
                path: 'usuarios',
                component: Usuarios,
                canActivate: [authGuard],
                data: { roles: ROLES_USUARIOS_ADMIN }
            },

            {
                path: 'usuarios/nuevo',
                component: UsuarioForm,
                canActivate: [authGuard],
                data: { roles: ROLES_USUARIOS_ADMIN }
            },

            {
                path: 'usuarios/:id/editar',
                component: UsuarioForm,
                canActivate: [authGuard],
                data: { roles: ROLES_USUARIOS_ADMIN }
            },

            {
                path: 'usuarios/:id/reset-password',
                component: UsuarioResetPassword,
                canActivate: [authGuard],
                data: { roles: ROLES_USUARIOS_ADMIN }
            },

            {
                path: 'descremado',
                component: Descremado,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_LECTURA }
            },

            {
                path: 'descremado/nuevo',
                component: DescremadoForm,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_ESCRITURA }
            },

            {
                path: 'insumos',
                component: Insumos,
                canActivate: [authGuard],
                data: { roles: ROLES_CATALOGOS_TECNICOS }
            },

            {
                path: 'formulas',
                loadComponent: () =>
                    import('./pages/formulas/formulas').then(m => m.Formulas),
                canActivate: [authGuard],
                data: { roles: ROLES_CATALOGOS_TECNICOS }
            },

            {
                path: 'skus',
                loadComponent: () =>
                    import('./pages/skus/skus').then(m => m.Skus),
                canActivate: [authGuard],
                data: { roles: ROLES_CATALOGOS_TECNICOS }
            },

            {
                path: 'programacion-produccion/nueva',
                component: ProgramacionProduccionForm,
                canActivate: [authGuard],
                data: { roles: ROLES_PROGRAMACION_ESCRITURA }
            },

            {
                path: 'ordenes-produccion',
                component: OrdenesProduccion,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_LECTURA }
            },

            {
                path: 'ejecucion-produccion',
                component: EjecucionProduccion,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_LECTURA }
            },



            {
                path: 'ordenes-produccion/:id',
                component: OrdenProduccionDetalle,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_LECTURA }
            },

            {
                path: 'ordenes-produccion/:id/ejecutar',
                component: OrdenEjecucion,
                canActivate: [authGuard],
                data: { roles: ROLES_OPERACION_ESCRITURA }
            },

            {
                path: 'mediciones-calidad-lactea',
                component: MedicionesCalidadLactea,
                canActivate: [authGuard],
                data: { roles: ROLES_CALIDAD_LECTURA }
            },

        ]
    },

    {
        path: '**',
        redirectTo: 'login'
    }

];
