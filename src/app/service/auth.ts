import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseURL = 'http://localhost:8080/auth';
  protected readonly http = inject(HttpClient);

  login(username: string, password: string): Observable<any> {
    const body = { username, password }; // 👈 Debe coincidir con LoginRequest
    return this.http.post(`${this.baseURL}/login`, body);
  }
  
  register(data: any): Observable<any> {
  return this.http.post(`${this.baseURL}/registro`, data);
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

   getToken(): string | null {
    return localStorage.getItem('token');
  }

  clearToken(): void {
    localStorage.removeItem('token');
  }

   isLoggedIn(): boolean {
    return !!this.getToken();
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

