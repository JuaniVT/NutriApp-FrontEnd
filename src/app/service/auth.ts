import { HttpClient, HttpHandler } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Authority } from '../models/authority';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseURL = `${environment.apiUrl}/auth`;
  protected readonly http = inject(HttpClient);


  //signal que se usa para obtener el rol en base al token y poder hacer comprobaciones en tiempo real 
  //ya que cuando se guarda el token y se elmina (logeo y deslogeo) se modifica el estado de esta señal
  readonly roleSignal = signal<string | null>(null);  
  
  //necesario para que cuando se recargue la pagina, si hay algun token guardado se setee el rol
  //ya que el estado de la signal muere cuando se resetea el componente
  constructor() {
    const token = this.getToken();
    if (token) {
      this.roleSignal.set(this.getRoleFromToken(token));
    }
  }


  //AUTH METHODS
  login(username: string, password: string): Observable<any> {
    const body = { username, password }; // 👈 Debe coincidir con LoginRequest
    return this.http.post(`${this.baseURL}/login`, body);
  }
  
  register(data: any): Observable<any> {
    return this.http.post(`${this.baseURL}/registro`, data);
  }
  
  getRoleRequest (){
    return this.http.get<Authority>(`${environment.apiUrl}/api/rol/obtener`);
  }
  
  
  //LOCAL STORAGE
  saveToken(token: string): void {
    localStorage.setItem('token', token); //guardamos el token

    this.roleSignal.set(this.getRoleFromToken(token)); //seteamos el role del token a la signal
  }
  
  getToken(): string | null {
    return localStorage.getItem('token');
  }
  
  clearToken(): void {
    localStorage.removeItem('token');   //se elminar el token 

    this.roleSignal.set(null)   //reseteamos el role cuando se deslogea
  }
  
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  //metodo para decodificar el token en un json con todo lo que trae (asi podemos acceder a los roles que vienen con el token)
  private decodeToken(token: string): any {
    const payload = token.split(".")[1];
    return JSON.parse(atob(payload));
  }

  //metodo para obtener el rol al decodifcar el token
  private getRoleFromToken (token: string){
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.roles || decoded.roles.length === 0) {
      return null;
    }
    
    return decoded.roles[0].authority;
  }


   checkUsername(username: string): Observable<boolean> {
    return this.http.get<any>(`${this.baseURL}/check-username?username=${username}`)
      .pipe(map(resp => resp.disponible));
  }

  checkEmail(email: string): Observable<boolean> {
    return this.http.get<any>(`${this.baseURL}/check-email?email=${email}`)
      .pipe(map(resp => resp.disponible));
  }

  checkDni(dni: string): Observable<boolean> {
    return this.http.get<any>(`${this.baseURL}/check-dni?dni=${dni}`)
      .pipe(map(resp => resp.disponible));
  }

  checkTelefono(telefono: string): Observable<boolean> {
    return this.http.get<any>(`${this.baseURL}/check-telefono?telefono=${telefono}`)
      .pipe(map(resp => resp.disponible));
  }

  googleLogin(token: string): Observable<any> {
  return this.http.post<any>(`${this.baseURL}/google`, {
    token: token
  });
}

}

