import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { NotificacionLogro } from '../../service/notificacion-logro';

@Component({
  selector: 'app-notificacion-logro-ventana',
  imports: [],
  templateUrl: './notificacion-logro-ventana.html',
  styleUrl: './notificacion-logro-ventana.css',
})
export class NotificacionLogroVentana {
  protected readonly notificacionLogroService = inject(NotificacionLogro);
  private readonly router = inject(Router);

  verLogros(){
    this.notificacionLogroService.cerrarNotificacion();
    this.router.navigateByUrl("/perfil")
  }
}
