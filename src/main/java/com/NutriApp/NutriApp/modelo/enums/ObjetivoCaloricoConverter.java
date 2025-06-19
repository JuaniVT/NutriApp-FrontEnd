package com.NutriApp.NutriApp.modelo.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ObjetivoCaloricoConverter implements AttributeConverter<ObjetivoCaloricoTipo, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ObjetivoCaloricoTipo objetivoCaloricoTipo) {
        return objetivoCaloricoTipo != null ? objetivoCaloricoTipo.getValue() : null;
    }

    @Override
    public ObjetivoCaloricoTipo convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ObjetivoCaloricoTipo.fromValue(dbData) : null;
    }
}
