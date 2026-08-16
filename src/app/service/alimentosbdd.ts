import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AlimentosBDD } from '../pages/alimentos-nuestra-bdd/alimentos-bdd/alimentos-bdd';
import { ComidaBDD } from '../models/comida-bdd';

@Injectable({
  providedIn: 'root',
})
export class AlimentosbddService {
  private readonly http = inject(HttpClient);
  private readonly baseURL = "https://nutriapp-backend-fko0.onrender.com/api/alimentos-usuario";

  getAll (){
    return this.http.get<ComidaBDD[]>(this.baseURL + "/listarTodos");
  }

  getLast10 (){
    return this.http.get<ComidaBDD[]>(this.baseURL + "/listarUltimos10");
  }

  filterByFoodName (foodNameFilter: string){
    return this.http.get<ComidaBDD[]>(this.baseURL + "/filtrar", {params: {nombreComida: foodNameFilter}});
  }

  insert (food: ComidaBDD){
    return this.http.post<ComidaBDD>(this.baseURL + "/insert", food);
  }

  delete (idFood: string | number){
    return this.http.delete(this.baseURL + "/eliminar", {params: {idAlimento: idFood}, responseType: "text"});
  }

  modify (idFood: number | string, food: ComidaBDD){
    return this.http.put<ComidaBDD>(this.baseURL + "/editar", food, {params: {idAlimento: idFood}});
  }
 

}
