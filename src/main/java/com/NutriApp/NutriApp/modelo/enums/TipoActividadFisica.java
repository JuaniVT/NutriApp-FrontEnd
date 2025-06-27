package com.NutriApp.NutriApp.modelo.enums;

public enum TipoActividadFisica {

    CORRER(11.0),
    GYM(6.0),
    FUTBOL(7.0),
    YOGA(2.5),
    BOXEO(8.0),
    NADAR(10.0),
    BAILAR(7.3),
    CAMINAR(3.8),
    CROSSFIT(9.0),
    ESCALADA(7.3),
    VOLEY(4.0),
    RUGBY(8.5),
    TENIS(7.3),
    PATINAJE(7.0),
    PILATES(3.0),
    BASQUET(8.0),
    HANDBALL(7.0),
    SURF(3.0),
    BICICLETA(8.0);

    private final double met;

    TipoActividadFisica(double met) {
        this.met = met;
    }

    public double calcularCalorias(double pesoKg, double duracionMinutos) {
        return (met * pesoKg / 200) * duracionMinutos;
    }

    public double getMet() {
        return met;
    }

    

}
