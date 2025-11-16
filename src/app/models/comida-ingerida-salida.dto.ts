export interface ComidaIngeridaSalidaDTO {
  id: string;
  nombreComida: string;
  calorias: number;
  proteinas: number;
  grasas: number;
  carbohidratos: number;
  cantidad: number;
  tipoComida: string;

  mostrar?: boolean;  
  swipeOffset?: number; 
}

