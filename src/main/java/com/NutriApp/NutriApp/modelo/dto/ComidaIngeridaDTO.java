package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComidaIngeridaDTO {
    private String nombreComida;
    private Double calorias;
    private Double proteinas;
    private Double grasas;
    private Double carbohidratos;
    private Double cantidad;
    private TipoComida tipoComida;
    private LocalDate fecha;  // la fecha del día
}
