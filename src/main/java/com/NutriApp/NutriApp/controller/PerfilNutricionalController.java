package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.PerfilNutricional;
import com.NutriApp.NutriApp.modelo.dto.PerfilNutricionalDTO;
import com.NutriApp.NutriApp.service.PerfilNutricionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil-nutricional")
@RequiredArgsConstructor
public class PerfilNutricionalController {

    private final PerfilNutricionalService perfilNutricionalService;

    // Obtener el perfil nutricional actual del usuario autenticado
    @GetMapping("/obtener")
    public ResponseEntity<PerfilNutricional> obtenerPerfil() {
        PerfilNutricional perfil = perfilNutricionalService.obtenerPerfilNutricional();
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarPerfil(@Valid @RequestBody PerfilNutricionalDTO perfilDTO) {
        perfilNutricionalService.actualizarPerfilNutricional(perfilDTO);
        return ResponseEntity.ok("Perfil nutricional actualizado correctamente.");
    }
}