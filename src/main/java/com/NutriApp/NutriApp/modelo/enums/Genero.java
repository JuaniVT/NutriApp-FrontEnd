package com.NutriApp.NutriApp.modelo.enums;


public enum Genero {
    MASCULINO(1),
    FEMENINO(2);

    private final int value;

    Genero(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Genero fromValue(int value) {
        for (Genero genero : values()) {
            if (genero.getValue() == value) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Genero: " + value);
    }
}

