import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Person } from '../models/person';
import { NutritionalProfile } from '../models/nutritional-profile';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  
  private readonly http = inject(HttpClient);
  private readonly baseURL_Person = "https://nutriapp-backend-fko0.onrender.com/api/persona";
  private readonly baseURL_NutritionalProfile = "https://nutriapp-backend-fko0.onrender.com/api/perfil-nutricional";

  //Person Profile
  getPersonProfile (){
    return this.http.get<Person>(this.baseURL_Person + "/mostrarMisDatos");
  }

  updatePersonProfile (person: Person){
    return this.http.put<Person>(this.baseURL_Person + "/actualizar-persona", person);
  }



  //Nutritional Profile
  getNutritionalProfile (){
    return this.http.get<NutritionalProfile>(this.baseURL_NutritionalProfile + "/obtener")
  }

  updateNutritionalProfile (nutritionalProfile: NutritionalProfile){
    return this.http.put<NutritionalProfile>(this.baseURL_NutritionalProfile + "/actualizar", nutritionalProfile);
  }
}
