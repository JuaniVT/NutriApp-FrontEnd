package com.NutriApp.NutriApp.modelo.ActividadesFisicas;

import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.modelo.PerfilNutricional;
import jakarta.persistence.Entity;

@Entity
public class Gym extends ActividadFisica {


    private static final double MET = 6.0;

    @Override
    public double calcularCalorias(PerfilNutricional perfilNutricional) {
        double peso = perfilNutricional.getPeso();
        return (MET * peso / 200) * getDuracionMin();
    }

}
