import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LogroHistorial } from '../models/logro-historial';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LogroService {
  
  private readonly http = inject(HttpClient);
  private readonly baseURL = "http://localhost:8080/api/logro";
  

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
    localStorage.setItem("lastNotified_id", id);
  }

  //obener el id del ultimo logro notificado
  getLast_ID_Notified(){
    return localStorage.getItem("lastNotified_id");
  }
}
