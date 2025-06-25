package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.ObjetivoCaloricoTipo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilNutricionalDTO {

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser un número positivo")
    @Max(value = 500, message = "El peso no puede superar los 500 kg")  // peso razonable máximo
    private Double peso;  // en kg

    @NotNull(message = "La altura es obligatoria")
    @Positive(message = "La altura debe ser un número positivo")
    @Max(value = 300, message = "La altura no puede superar los 300 cm")  // altura razonable máxima
    @Min(value = 50, message = "La altura debe estar expresada en centímetros, no en metros (ej: 174 en vez de 1.74)")
    private Double altura;  // en cm

    @NotNull(message = "El nivel de actividad física es obligatorio")
    private NivelActividadFisica nivelActividadFisica;

    @Min(value = 1, message = "La edad debe ser al menos 1 año")
    @Max(value = 130, message = "La edad no puede ser mayor a 130 años")
    private int edad;

    @NotNull(message = "El objetivo calórico es obligatorio")
    private ObjetivoCaloricoTipo objetivoCaloricoTipo;
}