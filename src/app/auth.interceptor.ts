import { inject, Injectable } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from './service/auth';
import { DialogService } from './service/dialog';

let alreadyHanded401 = false;

//interceptor que ejecuta codigo antes de que una peticion HTTP salga al servidor y despues de recibir la respuesta (como una cadena de filtros)
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  const router = inject(Router)
  const dialog = inject(DialogService)

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
    catchError(async (error) => {
      if (error.status === 401 && !alreadyHanded401) {

        //seteamos que ya estamos manejando el 401 para que si llegua un segundo 401 se ejecute solo una vez
        alreadyHanded401 = true;

        authService.clearToken();                  //si recibe un error de 401 (token expirado)
        await dialog.info("Sesion Expirada");       //clerea el token en la memoria del navegador
        router.navigateByUrl("/login");             //mensaje de alerta y redirecciona al login
      }

      throw error;
    })
  );
}
