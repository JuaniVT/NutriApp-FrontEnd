// chat.service.ts
export interface ChatRequest {
  message: string;
  userId: number;
}

export interface ChatResponse {
  reply: string;
  isFunctionCall: boolean; // Para saber si debe mostrar un componente de "Registro"
}