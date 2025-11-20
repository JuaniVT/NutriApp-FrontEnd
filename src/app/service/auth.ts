import { HttpClient, HttpHandler } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Authority } from '../models/authority';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseURL = 'http://localhost:8080/auth';
  protected readonly http = inject(HttpClient);


  //AUTH METHODS
  login(username: string, password: string): Observable<any> {
    const body = { username, password }; // 👈 Debe coincidir con LoginRequest
    return this.http.post(`${this.baseURL}/login`, body);
  }
  
  register(data: any): Observable<any> {
  return this.http.post(`${this.baseURL}/registro`, data);
  }

  getRole (){
    return this.http.get<Authority>("http://localhost:8080/api/rol/obtener");
  }


  //LOCAL STORAGE
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
}

