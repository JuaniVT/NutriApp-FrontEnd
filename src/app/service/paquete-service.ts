import { Injectable } from '@angular/core';
import { ComidaFavoritaDTO } from '../models/comidaFavoritaDTO';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class PaqueteService {
  private baseUrl = `${environment.apiUrl}/comidas-favoritas`;

  constructor(private http: HttpClient) {}

  addComidaFavorita(comida: ComidaFavoritaDTO) {
  return this.http.post(`${this.baseUrl}/agregar`, comida, { responseType: 'text' });
}

  getComidasFavoritasPorUsuario(): Observable<ComidaFavoritaDTO[]> {
    return this.http.get<ComidaFavoritaDTO[]>(`${this.baseUrl}/usuario`);
  }

   // En tu service.ts
eliminarAlimento(nombrePaquete: string, comidaId: number) {
  return this.http.delete(`${this.baseUrl}/eliminar`, {
    params: { nombrePaquete, comidaId },
    responseType: 'text' // <-- clave
  });
}

 agregarPaqueteADia(nombrePaquete: string, tipo: string, fecha: string): Observable<any> {
  return this.http.post(`${this.baseUrl}/agregar-paquete-a-dia`, null, {
    params: { nombrePaquete, tipo, dia: fecha },
    responseType: 'json'  // asegura que Angular lo interprete como JSON
  });
}

}
