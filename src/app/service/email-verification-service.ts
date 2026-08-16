import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class EmailVerificationService {

   private apiUrl = 'https://nutriapp-backend-fko0.onrender.com/verificacion'; // Ajusta si tu ruta cambia

  constructor(private http: HttpClient) {}

  enviarCodigo(email: string) {
    return this.http.post(`${this.apiUrl}/email/enviar-codigo`, { email });
  }

  verificarCodigo(email: string, codigo: string) {
    return this.http.post(`${this.apiUrl}/email/verificar-codigo`, { email, codigo });
  }

}
