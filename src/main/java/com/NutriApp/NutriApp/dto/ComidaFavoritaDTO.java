package com.NutriApp.NutriApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComidaFavoritaDTO {
    private long id;
    private String nombrePaquete;
    private String nombreComida;
    private long comidaId;
    private double cantidad;
}
