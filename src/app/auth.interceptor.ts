import { inject, Injectable } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './service/auth';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';


//interceptor que ejecuta codigo antes de que una peticion HTTP salga al servidor y despues de recibir la resíesta (como una cadena de filtros)
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  const router = inject(Router)

  let modifiedReq = req

  //agregamos token a las peticiones HTTP si hay un token almacenado en el navegador
  if (token) {
    modifiedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }


  //retornamos la peticion con el token agregado y manejamos si llegua a haber error de token expirado
  return next(modifiedReq).pipe(
    catchError((error) => {
      if (error.status === 401) {         //si recibe un error de 401 (token expirado)
        authService.clearToken();          //clerea el token en la memoria del navegador
        alert("Sesion Expirada");          //mensja de alerta
        router.navigateByUrl("/login");    //y redirecciona al login
      }

      return throwError(() => error);
    })
  );
}
