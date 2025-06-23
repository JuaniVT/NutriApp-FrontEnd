package com.NutriApp.NutriApp.dto;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarComidaIngeridaDTO {
    private long id;
    private String nombre;
    private double gramos;
    private TipoComida tipoComida;
    private TipoComida tipoComidaNuevo;
    private LocalDate fecha;
}
