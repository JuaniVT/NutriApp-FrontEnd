import { inject, Injectable } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './service/auth';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  return next(req);
};
