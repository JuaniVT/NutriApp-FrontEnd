package com.NutriApp.NutriApp.modelo.dto;
import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisicaResponseDTO {
    private long id;
    private String tipoActividad;
    private NivelActividadFisica intensidad;
    private double duracionMin;
    private double caloriasGastadas;
    private LocalDate fecha;
    private String username;
}
