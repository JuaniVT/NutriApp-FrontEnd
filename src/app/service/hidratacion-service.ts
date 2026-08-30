import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { HidratacionEntradaDTO, HidratacionSalidaDTO } from '../models/hidratacion';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class HidratacionService {

  private apiUrl = `${environment.apiUrl}/api/hidratacion`;
  private readonly http = inject(HttpClient); 

  verHidratacionDia(fecha: string): Observable<HidratacionSalidaDTO> {
    return this.http.get<HidratacionSalidaDTO>(`${this.apiUrl}/total?fecha=${fecha}`);
  }
  
  registrar(data: HidratacionEntradaDTO, fecha?: string): Observable<HidratacionSalidaDTO> {
  let params = new HttpParams();
  
  if (fecha) {
    // Asegúrate de que 'fecha' sea formato 'YYYY-MM-DD'
    params = params.set('fecha', fecha);
  }

  return this.http.post<HidratacionSalidaDTO>(this.apiUrl, data, { params });
}

obtenerTotalPorFecha(fecha: string): Observable<number> {
    // El backend espera 'fecha' como RequestParam
    const params = new HttpParams().set('fecha', fecha);
    return this.http.get<number>(`${this.apiUrl}/total`, { params });
  }

}
