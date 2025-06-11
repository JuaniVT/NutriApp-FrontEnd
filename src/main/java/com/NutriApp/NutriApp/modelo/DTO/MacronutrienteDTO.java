package com.NutriApp.NutriApp.modelo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties (ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacronutrienteDTO {

    private String nombreComida;

    private Long id_comida;

    private Double calorias;

    private Double proteinas;

    private Double grasas;

    private Double carbohidratos;

    private Double gramosPorPorcion;


}