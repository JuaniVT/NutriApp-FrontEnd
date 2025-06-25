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
public class ComidaFavoritaDTO {

    @NotBlank(message = "El nombre del paquete no puede estar vacío")
    private String nombrePaquete;

    @NotBlank(message = "El nombre de la comida no puede estar vacío")
    private String nombreComida;

    @Min(value = 1, message = "El comidaId debe ser mayor o igual a 1")
    private long comidaId;

    @Positive(message = "La cantidad debe ser un número positivo")
    private double cantidad;
}
