import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Person } from '../models/person';
import { NutritionalProfile } from '../models/nutritional-profile';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  
  private readonly http = inject(HttpClient);
  private readonly baseURL_Person = "http://localhost:8080/api/persona";
  private readonly baseURL_NutritionalProfile = "http://localhost:8080/api/perfil-nutricional";

  //Person Profile
  getPersonProfile (){
    return this.http.get<Person>(this.baseURL_Person + "/mostrarMisDatos");
  }

  updatePersonProfile (person: Person){
    return this.http.put(this.baseURL_Person + "/actualizar-persona", person, {responseType: "text"});
  }



  //Nutritional Profile
  getNutritionalProfile (){
    return this.http.get<NutritionalProfile>(this.baseURL_NutritionalProfile + "/obtener")
  }

  updateNutritionalProfile (nutritionalProfile: NutritionalProfile){
    return this.http.put(this.baseURL_NutritionalProfile + "/actualizar", nutritionalProfile, {responseType: "text"});
  }
}
