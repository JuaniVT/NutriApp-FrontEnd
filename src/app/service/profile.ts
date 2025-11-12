import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Person } from '../models/person';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  
  private readonly http = inject(HttpClient);
  private readonly baseURL_Person = "http://localhost:8080/api/persona";
  private readonly baseURL_NutritionalProfile = "http://localhost:8080/perfil-nutricional";

  //Person Profile
  getPersonProfile (){
    return this.http.get<Person>(this.baseURL_Person + "/mostrarMisDatos");
  }



  //Nutritional Profile
}
