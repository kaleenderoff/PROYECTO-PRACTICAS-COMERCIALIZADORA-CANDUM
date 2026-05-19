import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

export const authGuard: CanActivateFn = (route) => {

  const router = inject(Router);
  const authService = inject(AuthService);

  const token = localStorage.getItem('token');

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  const roles = route.data?.['roles'] as string[] | undefined;

  if (roles?.length && !authService.hasAnyRole(roles)) {
    const fallback = authService.isJefeLinea()
      ? '/recepcion-leche'
      : authService.isAuxiliarCalidad()
        ? '/mediciones-calidad-lactea'
        : '/dashboard';

    router.navigate([fallback]);
    return false;
  }

  return true;
};
