package com.NutriApp.NutriApp.modelo;


import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "ActividadFisica")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public abstract class ActividadFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NivelActividadFisica intensidad;

    private double duracionMin;

    private double caloriasGastadas;

    @ManyToOne
    @JoinColumn(name = "dia_id")
    @JsonIgnore
    private Dia dia;

    public abstract double calcularCalorias(PerfilNutricional perfilNutricional);



}


