package com.NutriApp.NutriApp.modelo.enums.Converters;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoComidaConverter implements AttributeConverter<TipoComida, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoComida tipoComida) {
        return tipoComida != null ? tipoComida.getValue() : null;
    }

    @Override
    public TipoComida convertToEntityAttribute(Integer dbData) {
        return dbData != null ? TipoComida.fromValue(dbData) : null;
    }
}
