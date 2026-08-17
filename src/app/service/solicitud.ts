import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Solicitud } from '../models/solicitud';
import { identifierName } from '@angular/compiler';
import { environment } from '../../environments/environment.development';
@Injectable({
  providedIn: 'root',
})
export class SolicitudService {
  private readonly http = inject(HttpClient);
  private readonly baseURL = `${environment.apiUrl}/api/solicitud`;

  
  //USERS
  insert(solicitud: Solicitud){
    return this.http.post<Solicitud>(this.baseURL + "/insertar", solicitud);
  }
  
  getMine(){
    return this.http.get<Solicitud[]>(this.baseURL + "/listar/misSolicitudes");
  }
  
  deleteMine(foodName: string){
    return this.http.delete(this.baseURL + "/eliminar", 
      {params: {nombreComidaSolicitudEliminar: foodName}, responseType: "text"});
  }

  modifydMine(foodNameModify: string, solicitudModified: Solicitud){
    return this.http.put<Solicitud>(

      //url base
      this.baseURL + "/modificar/miSolicitud", 
      //body que necesita mi back
      solicitudModified,
      //parametros que necesita el metodo del back
      {params: {nombreComidaModificar: foodNameModify}}

    );
  }
  
  
  //ADMINS
  getAll(){
    return this.http.get<Solicitud[]>(this.baseURL + "/listar/todas");
  }

  filterByDate(){

  }

  filterByUsername(){

  }

  filterByFoodName(){

  }

  modifyAndAccept(solicitudFoodName: string, newSolicitud: Solicitud){
    return this.http.put(
      this.baseURL + "/modificarAndAceptar",
      //recibe un body
      newSolicitud,
      //opciones
      {params: {nombreComidaSolicitudModificar: solicitudFoodName}, responseType: "text"}
    )

  }

  accept (idSolicitud: string | number){
    return this.http.post(
      //url
      this.baseURL + "/aceptar", 
      //le decimos que no recibe body y lo ponemos en null
      null,
      //opciones
      {params: {idSolicitud: idSolicitud}, responseType: "text"}

    );

  }

  decline (isSolciudDecline: number | string){
    return this.http.delete(
      
      //url
      this.baseURL + "/rechazar",
      //opciones
      {params: {idSolicitud: isSolciudDecline}, responseType: "text"}

    );
  }

}
