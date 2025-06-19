package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.dto.ComidaIngeridaDTO;
import com.NutriApp.NutriApp.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.dto.ModificarComidaIngeridaDTO;
import com.NutriApp.NutriApp.exceptions.DiaInvalidoException;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComidaIngeridaService {

    private final ComidaIngeridaRepository comidaIngeridaRepository;
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
        ComidaIngerida comida= new ComidaIngerida();
        comida = convertir_comidaid(comida, id_comida, gramos);
        comida.setTipoComida(tipo);
        comida.setDia(dia);
        guardar(comida);
    }

    public ComidaIngerida convertir_comidaid(ComidaIngerida comidaIngerida, long id, double gramos) throws Exception {
        JsonNode jsonNode = foodDataService.obtenerDetallePorId(id);
        MacronutrienteDTO macronutrienteDTO = nutricionService.extraerMacronutrientes(jsonNode);

        if (macronutrienteDTO.getGramosPorPorcion() == null || macronutrienteDTO.getGramosPorPorcion() == 0) {
            throw new IllegalArgumentException("El valor de gramosPorPorcion no puede ser nulo ni cero.");
        }
        comidaIngerida.setNombreComida(macronutrienteDTO.getNombreComida());
        comidaIngerida.setIdComidaApi(id);
        return settearComidaIngerida(comidaIngerida, gramos, macronutrienteDTO.getCalorias(), macronutrienteDTO.getProteinas(), macronutrienteDTO.getGrasas(), macronutrienteDTO.getCarbohidratos(), macronutrienteDTO.getGramosPorPorcion());
    }

    // Crear nuevo dia
    public void guardar(com.NutriApp.NutriApp.modelo.ComidaIngerida comidaIngerida) {

        comidaIngeridaRepository.save(comidaIngerida);
    }

    //modificar algun alimento dentro de la base de datos
    public boolean modificarComida(ModificarComidaIngeridaDTO dto) throws DiaInvalidoException, Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();
        Optional<Dia> dia = diaService.obtenerDiaPorFecha(dto.getFecha(), user);
        if (dia.isEmpty()) {
            throw new DiaInvalidoException("No hay un registro realizado en ese dia");
        }
        List<ComidaIngerida> comidasIngeridas = dia.get().getComidasIngeridas();
        ComidaIngerida insertar;
        for (int i = 0; i < comidasIngeridas.size(); i++) {
            if (comidasIngeridas.get(i).getId() == dto.getId()) {
                insertar = comidasIngeridas.get(i);
                insertar = convertir_comidaid(insertar, insertar.getIdComidaApi(), dto.getGramos());
                insertar.setTipoComida(dto.getTipoComida());
                guardar(insertar);
                return true;
            }
        }
        return false;
    }

    // metodo que settea ciertos valores de la comida para modularizar codigo
    public ComidaIngerida settearComidaIngerida(ComidaIngerida comidaIngerida, double gramos, double calorias, double proteinas, double grasas, double carbohidratos, double gramosPorPorcion) {
        // armamos el DTO
        comidaIngerida.setCalorias((gramos * calorias) / gramosPorPorcion);
        comidaIngerida.setProteinas((gramos * proteinas) / gramosPorPorcion);
        comidaIngerida.setGrasas((gramos * grasas) / gramosPorPorcion);
        comidaIngerida.setCarbohidratos((gramos * carbohidratos) / gramosPorPorcion);
        comidaIngerida.setCantidad(gramos);
        return comidaIngerida;
    }

}
