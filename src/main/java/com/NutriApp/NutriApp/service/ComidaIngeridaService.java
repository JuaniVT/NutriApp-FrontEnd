package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.dto.ComidaIngeridaDTO;
import com.NutriApp.NutriApp.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.modelo.ComidaIngerida;
import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.NutriApp.NutriApp.repository.ComidaIngeridaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class ComidaIngeridaService {

    private final ComidaIngeridaRepository  comidaIngeridaRepository;
    private final FoodDataService foodDataService;
    private final NutricionService nutricionService;
    private final DiaService diaService;

    @Transactional
    public void agregarComidaIngerida(long id_comida, double gramos, TipoComida tipo, LocalDate fecha) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();

        // Buscás el Día o lo creás si no existe
        Dia dia = diaService.obtenerODiaOCrear(fecha, user);

        // Armás la comida
        ComidaIngerida comida = convertir_comidaid(id_comida, gramos);
        comida.setTipoComida(tipo);
        comida.setDia(dia);
        guardar(comida);
    }

    public ComidaIngerida convertir_comidaid (long id, double gramos) throws Exception
    {
        JsonNode jsonNode = foodDataService.obtenerDetallePorId(id);
        MacronutrienteDTO macronutrienteDTO = nutricionService.extraerMacronutrientes(jsonNode);

        if (macronutrienteDTO.getGramosPorPorcion() == null || macronutrienteDTO.getGramosPorPorcion() == 0) {
            throw new IllegalArgumentException("El valor de gramosPorPorcion no puede ser nulo ni cero.");
        }
        ComidaIngerida comidaIngerida;

        // armamos el DTO
        comidaIngerida = new ComidaIngerida();
        comidaIngerida.setNombreComida(macronutrienteDTO.getNombreComida());
        comidaIngerida.setCalorias((gramos * macronutrienteDTO.getCalorias()) / macronutrienteDTO.getGramosPorPorcion());
        comidaIngerida.setProteinas((gramos * macronutrienteDTO.getProteinas()) / macronutrienteDTO.getGramosPorPorcion());
        comidaIngerida.setGrasas((gramos * macronutrienteDTO.getGrasas()) / macronutrienteDTO.getGramosPorPorcion());
        comidaIngerida.setCarbohidratos((gramos * macronutrienteDTO.getCarbohidratos()) / macronutrienteDTO.getGramosPorPorcion());
        comidaIngerida.setCantidad(gramos);
        return comidaIngerida;
    }

    // Crear nuevo dia
    public void guardar(com.NutriApp.NutriApp.modelo.ComidaIngerida comidaIngerida) {

        comidaIngeridaRepository.save(comidaIngerida);
    }

}
