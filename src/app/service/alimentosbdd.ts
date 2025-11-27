import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AlimentosBDD } from '../pages/alimentos-bdd/alimentos-bdd';
import { ComidaBDD } from '../models/comida-bdd';

@Injectable({
  providedIn: 'root',
})
export class AlimentosbddService {
  private readonly http = inject(HttpClient);
  private readonly baseURL = "http://localhost:8080/api/alimentos-usuario";

  getAll (){
    return this.http.get<ComidaBDD[]>(this.baseURL + "/listarTodos");
  }

  getLast10 (){
    return this.http.get<ComidaBDD[]>(this.baseURL + "/");
  }

  filterByFoodName (foodNameFilter: string){
    return this.http.get<ComidaBDD[]>(this.baseURL + "/filtrar", {params: {nombreComida: foodNameFilter}});
  }

}
