import { inject, Injectable, signal } from '@angular/core';
import { LogroService } from './logro';
import { LogroHistorial } from '../models/logro-historial';

@Injectable({
  providedIn: 'root',
})
export class NotificacionLogro {
  private readonly logroService = inject(LogroService);
  readonly ultimoLogroGanado = signal<LogroHistorial | undefined>(undefined);
  readonly mostrarNotificacionLogro = signal<boolean>(false);

  //variable para manejar el timeout de una notificacion y que no se pisen entre dos logros
  private timeoutNotificacion?: ReturnType<typeof setTimeout>; 

  obtenerUltimoLogroYmostrarNotificacion(){
    this.logroService.getLastWoned().subscribe({
      next: (logro) =>{

        //si no hay nada es porque no hay logros cargados todavia y no hacemos nada
        if(!logro){
          return;
        }

        //si ya se mostro el logro no hacemos nada
        if(logro.id == this.logroService.getLast_ID_Notified()){
          return
        }

        //si habia una notificacion anterior, lo cerramos para que no se pisen entre dos notificaciones
        if(this.timeoutNotificacion){
          clearTimeout(this.timeoutNotificacion);
        }

        //si hay un logro se lo seteamos
        this.ultimoLogroGanado.set(logro)
        
        //mostramos la ventana 
        this.mostrarNotificacionLogro.set(true);

        if(logro.id != undefined){
          //guardamos el id del ultimo notificado
          this.logroService.saveLast_ID_Notified(logro.id.toString());
        }

        //programamos el cierre automatico de la notificacion
        this.timeoutNotificacion = setTimeout(() => {
          this.mostrarNotificacionLogro.set(false);
        }, 10000);
      },
      error: (e) => {alert(e.message)}
    })
  }

  //metodo para cerrar la notificacion manualmente
  cerrarNotificacion(){
    if(this.timeoutNotificacion){
      clearTimeout(this.timeoutNotificacion)
    }

    this.mostrarNotificacionLogro.set(false)
  }

}
