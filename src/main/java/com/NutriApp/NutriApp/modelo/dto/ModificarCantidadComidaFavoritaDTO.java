package com.NutriApp.NutriApp.modelo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarCantidadComidaFavoritaDTO {

    @NotBlank(message = "El nombre del paquete es obligatorio")
    private String nombrePaquete;

    @Min(value = 1, message = "El id de comida debe ser mayor o igual a 1")
    private long idComida;

    @Positive(message = "La nueva cantidad debe ser un valor positivo")
    private double nuevaCantidad;
}
