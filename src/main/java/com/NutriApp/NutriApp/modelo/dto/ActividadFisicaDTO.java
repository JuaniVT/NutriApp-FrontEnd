package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.TipoActividadFisica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisicaDTO {

    @NotNull(message = "El tipo de actividad es obligatorio.")
    private TipoActividadFisica tipoActividad;

    @NotNull(message = "La intensidad de la actividad es obligatoria.")
    private NivelActividadFisica intensidad;

    @Positive(message = "La duración debe ser mayor a cero.")
    private double duracionMin;

}
