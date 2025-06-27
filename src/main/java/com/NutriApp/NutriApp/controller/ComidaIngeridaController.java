package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.ComidaIngeridaDTO;
import com.NutriApp.NutriApp.modelo.dto.ModificarComidaIngeridaDTO;
import com.NutriApp.NutriApp.exceptions.DiaInvalidoException;
import com.NutriApp.NutriApp.modelo.ComidaIngerida;
import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.NutriApp.NutriApp.service.ComidaIngeridaService;
import com.NutriApp.NutriApp.service.DiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Validated
@RestController
@RequestMapping("/comidas")  // prefijo de la ruta
@RequiredArgsConstructor
public class ComidaIngeridaController {

    private final ComidaIngeridaService comidaIngeridaService;
    private final DiaService diaService;

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarComidaIngerida(@Valid @RequestBody ComidaIngeridaDTO comidaIngeridaDTO) throws Exception {
        comidaIngeridaService.agregarComidaIngerida(comidaIngeridaDTO.getId(), comidaIngeridaDTO.getNombreComida(), comidaIngeridaDTO.getGramos(), comidaIngeridaDTO.getTipoComida(), comidaIngeridaDTO.getFecha());
        return ResponseEntity.ok("Se cargo correctamnete el alimento " + comidaIngeridaDTO.getNombreComida());
    }

    @PutMapping("/modificar")
    public ResponseEntity<String> modificarComidaIngerida(@RequestBody ModificarComidaIngeridaDTO modificarComidaIngeridaDTO) throws Exception {
        comidaIngeridaService.modificarComida(modificarComidaIngeridaDTO);
        return ResponseEntity.ok("Se modifico correctamente la comida");
    }

    @GetMapping("/calorias-dia")
    public ResponseEntity<Double> verCaloriasConsumidasDeunDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        double totalCalorias = comidaIngeridaService.verCaloriasConsumidasDeunDia(fecha);
        return ResponseEntity.ok(totalCalorias);
    }
    
    

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarComida(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, long comidaId, TipoComida tipoComida) {
        comidaIngeridaService.eliminarComidaIngerida(fecha, comidaId, tipoComida);
        return ResponseEntity.ok("La comida se eliminado correctamente.");
    }
}

