import { AlimentoInPaquete } from "./alimentoInPaquete";

export interface Paquete {
  nombrePaquete: string;
  alimentos: AlimentoInPaquete[];
}