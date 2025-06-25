package com.NutriApp.NutriApp.modelo.dto;
import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisicaResponseDTO {

    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private long id;

    @NotBlank(message = "El tipo de actividad es obligatorio")
    private String tipoActividad;

    @NotNull(message = "La intensidad es obligatoria")
    private NivelActividadFisica intensidad;

    @PositiveOrZero(message = "La duración en minutos no puede ser negativa")
    private double duracionMin;

    @PositiveOrZero(message = "Las calorías gastadas no pueden ser negativas")
    private double caloriasGastadas;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
}
