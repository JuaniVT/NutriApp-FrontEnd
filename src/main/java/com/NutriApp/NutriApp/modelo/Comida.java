package com.NutriApp.NutriApp.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comida {

    private String nombreComida;
    private Long idComida;
    private Double calorias;
    private Double proteinas;
    private Double grasas;
    private Double carbohidratos;
    private Double gramosPorPorcion;

}

