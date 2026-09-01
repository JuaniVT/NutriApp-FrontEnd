import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LogroHistorial } from '../models/logro-historial';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LogroService {
  
  private readonly http = inject(HttpClient);
  private readonly baseURL = `${environment.apiUrl}/api/logro`;
  

  getAll(){
    return this.http.get<LogroHistorial[]>(this.baseURL + "/listar/historial");
  }

  //obtiene las veces que gano ese logro
  getTimesWoned(tipoLogroComprobar: string){
    return this.http.get<Number>(
      this.baseURL + "/obtener/veces/ganado",
      {params: {tipoLogro: tipoLogroComprobar}});
  }

  //obtiene el ultimo logro ganado
  getLastWoned(): Observable<LogroHistorial | null>{
    return this.http.get<LogroHistorial>(this.baseURL + "/obtener/ultimo/ganado")
  }
  
  //guardar el id del ultimo logro que se notifico
  saveLast_ID_Notified (id: string){
    const logrosNotificados = this.getLogrosNotificados();

    // Evitamos guardar dos veces el mismo ID
    if (!logrosNotificados.includes(id)) {
      logrosNotificados.push(id);

      localStorage.setItem(
        "logrosNotificados",
        JSON.stringify(logrosNotificados)
      );
    }
  }

  // Obtener la lista de IDs de logros que ya fueron notificados
  private getLogrosNotificados(){
    const datos = localStorage.getItem("logrosNotificados");

    // Si todavía no existe la lista, devolvemos una lista vacía
    return datos ? JSON.parse(datos) : [];
  }

  // Verificar si un logro ya fue notificado
  yaFueNotificado(id: string){
    return this.getLogrosNotificados().includes(id);
  }
}
