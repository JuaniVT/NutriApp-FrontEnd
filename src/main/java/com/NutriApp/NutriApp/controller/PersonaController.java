package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.service.PersonaService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
public class PersonaController {


    private final PersonaService personaService;

    @GetMapping("/listar")
    public ResponseEntity<List<Persona>> obtenerTodasPersonas() {
        List<Persona> personas = personaService.obtenerTodas();
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(personas);
        }
    }

    @GetMapping("/Obtener/{id}")
    public ResponseEntity<Persona> obtenerPersonaXid(@PathVariable int id) throws PersonaInvalidaException {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<Persona> guardarPersona(@RequestBody Persona persona) throws PersonaInvalidaException {
        Persona personaGuardada = personaService.guardar(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaGuardada);
    }

}
