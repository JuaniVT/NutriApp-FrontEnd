package com.NutriApp.NutriApp.service;


import com.NutriApp.NutriApp.dto.MacronutrienteDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class NutricionService {


    private final FoodDataService foodDataService;

    public MacronutrienteDTO extraerMacronutrientes(JsonNode detalle) throws Exception {
        MacronutrienteDTO dto = new MacronutrienteDTO();
        dto.setNombreComida(detalle.path("description").asText());
        dto.setId_comida(detalle.path("fdcId").asLong());

        // Extraer macronutrientes
        for (JsonNode nutriente : detalle.path("foodNutrients")) {
            String nombre = nutriente.path("nutrient").path("name").asText();
            double cantidad = nutriente.path("amount").asDouble();

            switch (nombre) {
                case "Energy":
                    String unidad = nutriente.path("nutrient").path("unitName").asText();
                    if (unidad.equals("kcal")) dto.setCalorias(cantidad);
                    break;
                case "Protein":
                    dto.setProteinas(cantidad);
                    break;
                case "Total lipid (fat)":
                    dto.setGrasas(cantidad);
                    break;
                case "Carbohydrate, by difference":
                    dto.setCarbohidratos(cantidad);
                    break;
            }
        }

        // Extraer gramos por porción (primer foodPortion disponible)
        JsonNode porciones = detalle.path("foodPortions");
        if (porciones.isArray() && porciones.size() > 0) {
            JsonNode primeraPorcion = porciones.get(0); // podés ajustar si querés otro criterio
            dto.setGramosPorPorcion(primeraPorcion.path("gramWeight").asDouble());
        }

        return dto;
    }

    // metodo que combina la extraccion de elementos de la api
    public Optional<MacronutrienteDTO> obtenerMacronutrientesPorId(Long fdcId) {
        try {
            Optional <JsonNode> optionalDetalle = foodDataService.obtenerDetallePorIdOptional(fdcId);
            if (optionalDetalle.isEmpty()) {
                return Optional.empty();
            }

            MacronutrienteDTO dto = extraerMacronutrientes(optionalDetalle.get());

            if (dto.getCalorias() == null || dto.getProteinas() == null ||
                    dto.getGrasas() == null || dto.getCarbohidratos() == null ||
                    dto.getGramosPorPorcion() == null) {

                return Optional.empty();
            }

            return Optional.of(dto);

        } catch (Exception e) {
            // Podés loguear si querés: log.error("Error al obtener macronutrientes por ID", e);
            return Optional.empty();
        }
    }

}