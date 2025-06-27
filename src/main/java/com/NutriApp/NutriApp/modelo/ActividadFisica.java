package com.NutriApp.NutriApp.modelo;


import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.TipoActividadFisica;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    private TipoActividadFisica tipoActividad;

    private NivelActividadFisica intensidad;

    private double duracionMin;

    private double caloriasGastadas;


    @ManyToOne
    @JoinColumn(name = "dia_id")
    @JsonIgnore
    private Dia dia;

    public double calcularCaloriasGastadas(PerfilNutricional perfil) {
        return tipoActividad.calcularCalorias(perfil.getPeso(), duracionMin);
    }


}


