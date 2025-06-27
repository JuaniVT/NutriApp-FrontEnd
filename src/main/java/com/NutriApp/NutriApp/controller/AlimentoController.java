package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.AlimentoBusquedaDTO;
import com.NutriApp.NutriApp.modelo.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.service.FoodDataService;
import com.NutriApp.NutriApp.service.NutricionService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Validated
@RestController
@RequestMapping("/api/alimentos")
@Tag(name = "Alimentos Api", description = "Operaciones con los alimentos de la api")
public class AlimentoController {

    private final FoodDataService foodDataService;
    private final NutricionService nutricionService;

    public AlimentoController(FoodDataService foodDataService, NutricionService nutricionService) {
        this.foodDataService = foodDataService;
        this.nutricionService = nutricionService;
    }

    // Endpoint 1: buscar alimentos por nombre (devuelve lista con fdcId y descripción)
    @Operation(summary = "Buscar alimentos en la api.", description = "Devuleve una lista con los alimentos obtenidos de la api.")
    @GetMapping("/buscar")
    public List<AlimentoBusquedaDTO> buscarAlimentos(@RequestParam String nombre) throws Exception {
        return foodDataService.buscarAlimentosPorNombre(nombre);
    }

    // Endpoint 2: obtener detalles nutricionales (DTO) por ID
    @Operation(summary = "Obtener el detalle del alimento de la api.", description = "Devuleve los macronutrientes de una alimento de la api.")
    @GetMapping("/detalle/{fdcId}")
    public MacronutrienteDTO obtenerDetalle(@PathVariable Long fdcId) throws Exception {
        JsonNode detalle = foodDataService.obtenerDetallePorId(fdcId);
        return nutricionService.extraerMacronutrientes(detalle);
    }
}
