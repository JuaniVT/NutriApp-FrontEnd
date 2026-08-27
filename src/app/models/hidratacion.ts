// hidratacion.model.ts
export interface HidratacionEntradaDTO {
  cantidadMl: number;
}

export interface HidratacionSalidaDTO {
  id: number;
  cantidadMl: number;
  fecha: string;
}