import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LogroHistorial } from '../models/logro-historial';

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
  
}
