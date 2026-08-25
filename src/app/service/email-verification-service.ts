import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EmailVerificationService {

   private apiUrl = `${environment.apiUrl}/verificacion`; // Ajusta si tu ruta cambia

  constructor(private http: HttpClient) {}

  enviarCodigo(email: string) {
    return this.http.post(`${this.apiUrl}/email/enviar-codigo`, { email });
  }

  verificarCodigo(email: string, codigo: string) {
    return this.http.post(`${this.apiUrl}/email/verificar-codigo`, { email, codigo });
  }

}
