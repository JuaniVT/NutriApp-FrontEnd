package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.dto.AlimentoBusquedaDTO;
import com.NutriApp.NutriApp.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.service.FoodDataService;
import com.NutriApp.NutriApp.service.NutricionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final FoodDataService foodDataService;
    private final NutricionService nutricionService;

    public AlimentoController(FoodDataService foodDataService, NutricionService nutricionService) {
        this.foodDataService = foodDataService;
        this.nutricionService = nutricionService;
    }

    // Endpoint 1: buscar alimentos por nombre (devuelve lista con fdcId y descripción)
    @GetMapping("/buscar")
    public List<AlimentoBusquedaDTO> buscarAlimentos(@RequestParam String nombre) throws Exception {
        return foodDataService.buscarAlimentosPorNombre(nombre);
    }

    // Endpoint 2: obtener detalles nutricionales (DTO) por ID
    @GetMapping("/detalle/{fdcId}")
    public MacronutrienteDTO obtenerDetalle(@PathVariable Long fdcId) throws Exception {
        JsonNode detalle = foodDataService.obtenerDetallePorId(fdcId);
        return nutricionService.extraerMacronutrientes(detalle);
    }
}
