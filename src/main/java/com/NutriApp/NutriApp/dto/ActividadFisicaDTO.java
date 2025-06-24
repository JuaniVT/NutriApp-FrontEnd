package com.NutriApp.NutriApp.dto;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
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

    @NotBlank(message = "El tipo de actividad es obligatorio.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "El tipo de actividad debe contener solo letras.")
    private String tipoActividad;

    @NotNull(message = "La intensidad de la actividad es obligatoria.")
    private NivelActividadFisica intensidad;

    @Positive(message = "La duración debe ser mayor a cero.")
    private double duracionMin;

}
