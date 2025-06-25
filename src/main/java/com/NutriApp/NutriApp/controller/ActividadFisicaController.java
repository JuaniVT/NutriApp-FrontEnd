package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.ActividadFisicaDTO;
import com.NutriApp.NutriApp.modelo.dto.ActividadFisicaResponseDTO;
import com.NutriApp.NutriApp.exceptions.ActividadFisicaInvalidaException;
import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.service.ActividadFisicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@Validated
@RestController
@RequestMapping("/api/actividadesFisicas")
@RequiredArgsConstructor
public class ActividadFisicaController {


    private final ActividadFisicaService actividadFisicaService;

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> obtenerTiposActividadesDisponibles() {
        return ResponseEntity.ok(actividadFisicaService.obtenerTiposDisponibles());
    }


    @PostMapping("/agregar")
    public ResponseEntity<String> agregarActividad(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO) {

        actividadFisicaService.agregarActividadFsicaRealizada(actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin());
        return ResponseEntity.ok("ActividadFisicaRealizada agregada con exito");

    }

    @PostMapping("/agregar/diaEspecifico")
    public ResponseEntity<String> agregarActividad(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO, @RequestParam LocalDate fecha) {

        actividadFisicaService.agregarActividadFsicaRealizadaEnUnDiaEspecifico(actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin(), fecha);
        return ResponseEntity.ok("ActividadFisicaRealizada agregada con exito");

    }

    @GetMapping("/listarActividades/sistema")
    public ResponseEntity<List<ActividadFisicaResponseDTO>> listarActividadesFisicas() {
        List<ActividadFisicaResponseDTO> actividadesFisicas = actividadFisicaService.obtenerTodas();
        if (actividadesFisicas.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(actividadesFisicas);
        }

    }

    @GetMapping("/listarActividadesRealizadas")
    public ResponseEntity<List<ActividadFisica>> listarActividadesFisicasRealizadas(@RequestParam LocalDate fecha) throws ActividadFisicaInvalidaException {

        List<ActividadFisica> actividadesRealizadas = actividadFisicaService.obtenerTodasPorDia(fecha);
        if (actividadesRealizadas.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(actividadesRealizadas);
        }

    }


}
