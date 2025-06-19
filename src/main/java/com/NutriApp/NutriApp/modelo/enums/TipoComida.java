package com.NutriApp.NutriApp.modelo.enums;

import lombok.Getter;

import lombok.ToString;

@Getter
public enum TipoComida {
    DESAYUNO(1),
    ALMUERZO(2),
    CENA(3),
    SNACK(4);

    private final int value;

    TipoComida(int value) {
        this.value = value;
    }

    public static TipoComida fromValue(int value) {
        for (TipoComida tipoComida : values()) {
            if (tipoComida.getValue() == value) {
                return tipoComida;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Tipo de comida: " + value);
    }
}
