package com.NutriApp.NutriApp.service;


import com.NutriApp.NutriApp.modelo.dto.MacronutrienteDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class NutricionService {


    private final FoodDataService foodDataService;

    public MacronutrienteDTO extraerMacronutrientes(JsonNode detalle) throws Exception {
        // Se crea un nuevo objeto DTO que se usará para devolver los datos nutricionales procesados
        MacronutrienteDTO dto = new MacronutrienteDTO();

        // Se obtiene y guarda el nombre o descripción del alimento desde el nodo JSON
        dto.setNombreComida(detalle.path("description").asText());

        // Se obtiene y guarda el ID único del alimento (fdcId) desde el nodo JSON
        dto.setId_comida(detalle.path("fdcId").asLong());

        // Recorremos todos los nutrientes del alimento
        for (JsonNode nutriente : detalle.path("foodNutrients")) {
            // Obtenemos el nombre del nutriente (ej: "Energy", "Protein", etc.)
            String nombre = nutriente.path("nutrient").path("name").asText();

            // Obtenemos la cantidad del nutriente en la porción especificada
            double cantidad = nutriente.path("amount").asDouble();

            // Usamos un switch para guardar solo los nutrientes que nos interesan (macronutrientes)
            switch (nombre) {
                case "Energy":  // Energía o calorías
                    String unidad = nutriente.path("nutrient").path("unitName").asText();
                    // Solo usamos las calorías expresadas en "kcal"
                    if (unidad.equals("kcal")) dto.setCalorias(cantidad);
                    break;

                case "Protein":  // Proteínas
                    dto.setProteinas(cantidad);
                    break;

                case "Total lipid (fat)":  // Grasas totales
                    dto.setGrasas(cantidad);
                    break;

                case "Carbohydrate, by difference":  // Carbohidratos netos
                    dto.setCarbohidratos(cantidad);
                    break;
            }
        }

        // Ahora buscamos el peso en gramos de una porción estándar del alimento
        JsonNode porciones = detalle.path("foodPortions");

        // Si hay una lista de porciones disponibles
        if (porciones.isArray() && porciones.size() > 0) {
            // Tomamos la primera porción de la lista
            JsonNode primeraPorcion = porciones.get(0);

            // Obtenemos el peso en gramos de esa porción y lo guardamos en el DTO
            dto.setGramosPorPorcion(primeraPorcion.path("gramWeight").asDouble());
        }else{
            // Si el alimento no tiene información de porciones, usamos 100g por convención,
            // ya que la mayoría de los valores nutricionales están expresados por cada 100g
            dto.setGramosPorPorcion(100.0);

        }

        // Devolvemos el objeto con todos los datos extraídos
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