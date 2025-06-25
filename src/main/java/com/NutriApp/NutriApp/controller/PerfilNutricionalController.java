package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.PerfilNutricional;
import com.NutriApp.NutriApp.service.PerfilNutricionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Validated
@RestController
@RequestMapping("/api/perfil-nutricional")
@RequiredArgsConstructor
public class PerfilNutricionalController {

    private final PerfilNutricionalService perfilNutricionalService;

    // Obtener el perfil nutricional actual del usuario autenticado
    @GetMapping ("/obtener")
    public ResponseEntity<PerfilNutricional> obtenerPerfil() {
        PerfilNutricional perfil = perfilNutricionalService.obtenerPerfilNutricional();
        return ResponseEntity.ok(perfil);
    }
}