export type EstadoDiaTipo = 'PENDIENTE' | 'CUMPLIDO' | 'EXCEDIDO';

export interface EstadoDiaDTO {
  fecha: string;
  estadoDia: EstadoDiaTipo;
  caloriasConsumidas: number | null;        //propiedades obligatorias
  objetivoCalorico: number | null;


  caloriasRestantes?: number | null;
  porcentajeCumplimiento?: number | null;
  descripcion?: string | null;
  resumen?: string | null;                  // propiedades opcionales
  detalle?: string | null;        
  contexto?: string | null;
  comidasRegistradas?: number | null;
  [key: string]: unknown;
}