export interface ComidaIngeridaDTO {
  id: number;              // Long en Java → number en TS
  nombreComida: string;    // String en Java → string en TS
  gramos: number;          // Double en Java → number en TS
  tipoComida: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK'; // Enum en Java → union de strings en TS
  fecha: string;           // LocalDate en Java → string (YYYY-MM-DD) en TS
}