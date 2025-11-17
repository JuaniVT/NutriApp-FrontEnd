export type NivelActividadFisica = "SEDENTARIO" | "LIGERA" | "MODERADA" | "INTENSA" | "MUY_INTENSA";
export type ObjetivoCaloricoTipo = "MANTENIMIENTO" | "DEFICIT_LIGERO" | "DEFICIT_MODERADO" | "SUPERAVIT_LIGERO" | "SUPERAVIT_MODERADO";

export interface NutritionalProfile {
    id?: string | number,
    peso: number,
    altura: number,
    nivelActividadFisica: NivelActividadFisica,
    objetivoCaloricoTipo: ObjetivoCaloricoTipo,
    objetivoDiario: number,
    edad: number,
    geb: number,
}
