import { HttpClient, HttpHandler } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly http = inject (HttpClient);
  private readonly baseURL = "https://nutriapp-backend-fko0.onrender.com/usuario";

  deleteAccount (){
    return this.http.delete(this.baseURL + "/eliminarCuenta", {responseType: "text"});
  }
}
