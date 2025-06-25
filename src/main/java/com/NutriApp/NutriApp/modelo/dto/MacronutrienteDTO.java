package com.NutriApp.NutriApp.modelo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties (ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacronutrienteDTO {

    @NotBlank(message = "El nombre de la comida es obligatorio")
    private String nombreComida;

    @NotNull(message = "El id de comida es obligatorio")
    @Min(value = 1, message = "El id de comida debe ser mayor o igual a 1")
    private Long id_comida;

    @NotNull(message = "Las calorías son obligatorias")
    @PositiveOrZero(message = "Las calorías no pueden ser negativas")
    private Double calorias;

    @NotNull(message = "Las proteínas son obligatorias")
    @PositiveOrZero(message = "Las proteínas no pueden ser negativas")
    private Double proteinas;

    @NotNull(message = "Las grasas son obligatorias")
    @PositiveOrZero(message = "Las grasas no pueden ser negativas")
    private Double grasas;

    @NotNull(message = "Los carbohidratos son obligatorios")
    @PositiveOrZero(message = "Los carbohidratos no pueden ser negativos")
    private Double carbohidratos;

    @NotNull(message = "Los gramos por porción son obligatorios")
    @PositiveOrZero(message = "Los gramos por porción no pueden ser negativos")
    private Double gramosPorPorcion;
}