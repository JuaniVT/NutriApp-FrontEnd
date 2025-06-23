package com.NutriApp.NutriApp.modelo;


import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "ActividadFisica")
@AllArgsConstructor
@NoArgsConstructor
public class ActividadFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoActividad;

    private NivelActividadFisica intensidad;

    private double duracionMin;

    private LocalDate fecha;

    private double caloriasGastadas;




}


