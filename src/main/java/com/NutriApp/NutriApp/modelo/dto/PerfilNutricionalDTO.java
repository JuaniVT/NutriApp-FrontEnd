package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.ObjetivoCaloricoTipo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilNutricionalDTO {

    private Double peso;  // en kg

    private Double altura;  // en cm

    private NivelActividadFisica nivelActividadFisica;

    private int edad;

    private ObjetivoCaloricoTipo objetivoCaloricoTipo;
}
