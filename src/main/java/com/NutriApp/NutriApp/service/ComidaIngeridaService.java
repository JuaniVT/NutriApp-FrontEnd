package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.dto.ComidaIngeridaDTO;
import com.NutriApp.NutriApp.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.dto.ModificarComidaIngeridaDTO;
import com.NutriApp.NutriApp.exceptions.ComidaIngeridaException;
import com.NutriApp.NutriApp.exceptions.DiaInvalidoException;
import com.NutriApp.NutriApp.modelo.Comida;
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
import org.springframework.web.bind.annotation.RequestParam;

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
    public void agregarComidaIngerida(long id_comida, double gramos, TipoComida tipo, LocalDate fecha ) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();

        // Buscás el Día o lo creás si no existe
        Dia dia = diaService.obtenerODiaOCrear(fecha, user);

        // Armás la comida
        ComidaIngerida comida = new ComidaIngerida();
        comida = convertir_comidaid(comida, id_comida, gramos);
        comida.setTipoComida(tipo);
        comida.setDia(dia);
        guardar(comida);
    }

    public ComidaIngerida convertir_comidaid(ComidaIngerida comidaIngerida, long id, double gramos) throws Exception {
        JsonNode jsonNode = foodDataService.obtenerDetallePorId(id);
        MacronutrienteDTO macronutrienteDTO = nutricionService.extraerMacronutrientes(jsonNode);

        if (macronutrienteDTO.getGramosPorPorcion() == null || macronutrienteDTO.getGramosPorPorcion() == 0) {
            throw new ComidaIngeridaException("El valor de gramosPorPorcion no puede ser nulo ni cero.");
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
    public boolean modificarComida(ModificarComidaIngeridaDTO dto) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();
        Optional<Dia> dia = diaService.obtenerDiaPorFecha(dto.getFecha(), user);

        if (dia.isEmpty()) {
            throw new DiaInvalidoException("No hay un registro realizado en ese dia");
        }
        List<ComidaIngerida> comidasIngeridas = dia.get().getComidasIngeridas();
        ComidaIngerida modificar;
        for (int i = 0; i < comidasIngeridas.size(); i++) {
            if (comidasIngeridas.get(i).getId() == dto.getId()) {
                modificar = comidasIngeridas.get(i);
                modificar = convertir_comidaid(modificar, modificar.getIdComidaApi(), dto.getGramos());
                modificar.setTipoComida(dto.getTipoComida());
                guardar(modificar);
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

    public double verCaloriasConsumidasDeunDia(@RequestParam LocalDate fecha) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();

        // Buscar el día correspondiente
        Optional <Dia> diaEncontrado = diaService.obtenerDiaPorFecha(fecha, user);
        if (diaEncontrado.isEmpty())
        {
            throw new DiaInvalidoException("No se encontro el dia registrado con fecha: " + fecha);
        }

        // Extraer la lista de comidas ingeridas
        List<ComidaIngerida> comidas = diaEncontrado.get().getComidasIngeridas();

        // Sumar las calorías de las comidas ingeridas
        double totalCalorias = comidas.stream()
                .mapToDouble(ComidaIngerida::getCalorias)
                .sum();

        return totalCalorias;
    }

    public ComidaIngerida buscarComidaIngerida(LocalDate fecha, long comidaId, TipoComida tipoComida) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();

        return comidaIngeridaRepository.findByUserIdAndFechaAndComidaIdAndTipoComida(user.getPersona().getId(), fecha, comidaId, tipoComida)
                .orElseThrow(() -> new ComidaIngeridaException("No se encontró una comida para el día con la fecha: " + fecha + " en el/la " + tipoComida));
    }

    public void eliminarComidaIngerida (LocalDate fecha, long comidaId, TipoComida tipoComida)
    {
        comidaIngeridaRepository.delete(buscarComidaIngerida(fecha, comidaId, tipoComida));
    }
}
