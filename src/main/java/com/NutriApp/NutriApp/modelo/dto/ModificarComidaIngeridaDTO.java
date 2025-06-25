package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarComidaIngeridaDTO {

    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Positive(message = "Los gramos deben ser un valor positivo")
    private double gramos;

    @NotNull(message = "El tipo de comida actual es obligatorio")
    private TipoComida tipoComida;

    @NotNull(message = "El nuevo tipo de comida es obligatorio")
    private TipoComida tipoComidaNuevo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}
