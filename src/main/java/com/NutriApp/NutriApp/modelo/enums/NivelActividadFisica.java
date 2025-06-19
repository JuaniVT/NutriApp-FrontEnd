package com.NutriApp.NutriApp.modelo.enums;

public enum NivelActividadFisica {
    SEDENTARIO(1.2, 1),
    LIGERA(1.375, 2),
    MODERADA(1.55, 3),
    INTENSA(1.725, 4),
    MUY_INTENSA(1.9, 5);

    private final double factor;
    private final int value;

    NivelActividadFisica(double factor, int value) {
        this.factor = factor;
        this.value = value;
    }

    public double getFactor() {
        return factor;
    }

    public int getValue() {
        return value;
    }

    public static NivelActividadFisica fromValue(int value) {
        for (NivelActividadFisica nivelActividadFisica : values()) {
            if (nivelActividadFisica.getValue() == value) {
                return nivelActividadFisica;
            }
        }
        throw new IllegalArgumentException("Valor inválido para nivel de actividad fisica: " + value);
    }
}
