import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient} from '@angular/common/http';
import { inject } from '@angular/core';
import { environment } from '../../../environments/environment';

interface Message {
  role: 'user' | 'bot';
  content: string;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrl: './chat.css',
})
export class Chat {
  private http = inject(HttpClient);
  
  messages: Message[] = [{ role: 'bot', content: '¡Hola! Soy tu NutriBot. ¿En qué puedo ayudarte hoy?' }];
  newMessage: string = '';
  isLoading: boolean = false;

  sendMessage() {
    if (!this.newMessage.trim()) return;

    // 1. Guardar mensaje localmente
    const userPrompt = this.newMessage;
    this.messages.push({ role: 'user', content: userPrompt });
    this.newMessage = '';
    this.isLoading = true;

    // 2. Preparar el objeto que coincide con tu ChatRequest del backend
    const body = { message: userPrompt };

    // 3. CAMBIO CRÍTICO: Usar POST y enviar el cuerpo
    // La URL debe ser la que definiste en tu @RequestMapping("/ai") y @PostMapping("/ask")
    this.http.post<{ reply: string }>('${environment.apiUrl}/ai/ask', body)
      .subscribe({
        next: (response) => {
          this.messages.push({ role: 'bot', content: response.reply });
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error:', err);
          this.messages.push({ role: 'bot', content: 'Lo siento, tuve un error técnico. ¿Podrías intentar de nuevo?' });
          this.isLoading = false;
        }
      });
  }
}
