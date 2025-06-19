package com.NutriApp.NutriApp.modelo.enums;

// Nuevo Enum para los Objetivos Calóricos
public enum ObjetivoCaloricoTipo {
    MANTENIMIENTO(0, 1), // No hay ajuste, se mantiene el GET
    DEFICIT_LIGERO(-0.15, 2), // Reducir un 15% del GET
    DEFICIT_MODERADO(-0.25, 3), // Reducir un 25% del GET
    SUPERAVIT_LIGERO(0.10, 4), // Aumentar un 10% del GET
    SUPERAVIT_MODERADO(0.20, 5); // Aumentar un 20% del GET

    private final double ajustePorcentaje;// Para multiplicar el GET
    private final int value;

    ObjetivoCaloricoTipo(double ajustePorcentaje, int value) {
        this.ajustePorcentaje = ajustePorcentaje;
        this.value = value;
    }

    public double getAjustePorcentaje() {
        return ajustePorcentaje;
    }

    public int getValue() {
        return value;
    }

    public static ObjetivoCaloricoTipo fromValue(int value) {
        for (ObjetivoCaloricoTipo objetivoCaloricoTipo : values()) {
            if (objetivoCaloricoTipo.getValue() == value) {
                return objetivoCaloricoTipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para el objetivo calorico: " + value);
    }
}
