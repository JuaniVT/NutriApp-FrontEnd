package com.NutriApp.NutriApp.modelo.enums.Converters;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NivelActividadFisicaConverter implements AttributeConverter <NivelActividadFisica, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NivelActividadFisica nivelActividadFisica) {
        return nivelActividadFisica != null ? nivelActividadFisica.getValue() : null;
    }

    @Override
    public NivelActividadFisica convertToEntityAttribute(Integer dbData) {
        return dbData != null ? NivelActividadFisica.fromValue(dbData) : null;
    }
}
