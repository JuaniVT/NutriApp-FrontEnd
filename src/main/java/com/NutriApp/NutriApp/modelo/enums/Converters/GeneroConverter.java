package com.NutriApp.NutriApp.modelo.enums.Converters;
import com.NutriApp.NutriApp.modelo.enums.Genero;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GeneroConverter implements AttributeConverter<Genero, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Genero genero) {
        return genero != null ? genero.getValue() : null;
    }

    @Override
    public Genero convertToEntityAttribute(Integer dbData) {
        return dbData != null ? Genero.fromValue(dbData) : null;
    }
}


