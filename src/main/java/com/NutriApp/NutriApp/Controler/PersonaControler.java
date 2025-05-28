package com.NutriApp.NutriApp.Controler;

import com.NutriApp.NutriApp.Exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.Modelo.Persona;
import com.NutriApp.NutriApp.Servicio.PersonaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persona")
public class PersonaControler {
    private final PersonaServicio personaServicio;

    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodasPersonas() {
        List<Persona> personas = personaServicio.obtenerTodas();
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(personas);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPersonaXid(@PathVariable int id) throws PersonaInvalidaException {
        return ResponseEntity.ok(personaServicio.obtenerPorId(id));
    }

    public ResponseEntity<> guardarPersona (Persona persona) throws PersonaInvalidaException
        return ResponseEntity.ok()
}
