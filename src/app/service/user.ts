import { HttpClient, HttpHandler } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly http = inject (HttpClient);
  private readonly baseURL = `${environment.apiUrl}/usuario`;

  deleteAccount (){
    return this.http.delete(this.baseURL + "/eliminarCuenta", {responseType: "text"});
  }
}
