import { CanActivateFn, Router } from '@angular/router';
import { inject, computed } from '@angular/core';
import { AuthService } from '../service/auth';


export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles: string[] | undefined = route.data['roles'];

  // Si no está logueado
  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  // Si no se definieron roles en la ruta → cualquier usuario logueado puede entrar
  if (!expectedRoles || expectedRoles.length === 0) {
    return true;
  }

  // Si se definieron roles → verificar que el rol actual esté permitido
  const currentRole = authService.roleSignal();
  if (expectedRoles.includes(currentRole!)) {
    return true;
  } else {
    router.navigate(['/unauthorized']);
    return false;
  }
};