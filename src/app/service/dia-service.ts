import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComidaIngeridaSalidaDTO } from '../models/comida-ingerida-salida.dto';
import { modificarComidaIngeridaDTO } from '../models/modificar-comida-ingerida-dto';
import { AlimentoBusquedaDTO } from '../models/alimentoBusquedadto';
import { ComidaIngeridaDTO } from '../models/comidaingeridadto';
import { HidratacionSalidaDTO } from '../models/hidratacion';


export interface ActividadFisicaSalidaDTO {
  tipoActividad: string;
  intensidad: string;
  duracionMin: number;
  caloriasGastadas: number;
 
}

export interface DiaDTO {
  fecha: string;
  caloriasRestantes: number;
  comidasIngeridas: ComidaIngeridaSalidaDTO[];
  actividadesFisicasRealizadas: ActividadFisicaSalidaDTO[];
  hidratacion: HidratacionSalidaDTO;
}

@Injectable({
  providedIn: 'root'
})
export class DiaService {

  private apiUrl = 'https://nutriapp-backend-fko0.onrender.com'; 
  private readonly http = inject(HttpClient); 


  
  verDiaCompleto(fecha: string): Observable<DiaDTO> {
    return this.http.get<DiaDTO>(`${this.apiUrl}/dias/ver/completo?fecha=${fecha}`);
  }

  modificarComida(dto: modificarComidaIngeridaDTO) {
  return this.http.put<{ mensaje: string }>(`${this.apiUrl}/comidas/modificar`, dto);

  
}

// Buscar alimentos
  buscarAlimentos(nombre: string): Observable<AlimentoBusquedaDTO[]> {
    return this.http.get<AlimentoBusquedaDTO[]>(`${this.apiUrl}/api/alimentos/global/buscar`, {
      params: { nombreComida: nombre }
    });
  }

  // Agregar comida ingerida
  agregarComidaIngerida(comida: ComidaIngeridaDTO): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${this.apiUrl}/comidas/agregar`, comida);
  }

eliminarComida(id: string, fecha: string, tipo: string): Observable<{ mensaje: string }> {
    return this.http.delete<{ mensaje: string }>(`${this.apiUrl}/comidas/eliminar`, {
      params: {
        fecha: fecha,
        comidaId: id,
        tipoComida: tipo // uso del map para enviar el número correcto
      }
    });
  }

}