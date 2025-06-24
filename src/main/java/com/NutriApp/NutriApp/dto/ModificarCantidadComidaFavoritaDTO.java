package com.NutriApp.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarCantidadComidaFavoritaDTO {
    private String nombrePaquete;
    private long idComida;
    private double nuevaCantidad;
}
