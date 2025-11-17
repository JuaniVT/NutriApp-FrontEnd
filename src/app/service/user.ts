import { HttpClient, HttpHandler } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly http = inject (HttpClient);
  private readonly baseURL = "http://localhost:8080/usuario";

  deleteAccount (){
    return this.http.delete(this.baseURL + "/eliminarCuenta", {responseType: "text"});
  }
}
