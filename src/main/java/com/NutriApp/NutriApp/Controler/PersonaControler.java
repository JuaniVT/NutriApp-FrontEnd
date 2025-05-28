package com.NutriApp.NutriApp.Controler;

import com.NutriApp.NutriApp.Exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.Modelo.Persona;
import com.NutriApp.NutriApp.Servicio.PersonaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persona")
public class PersonaControler {
    private final PersonaServicio personaServicio;

    @GetMapping("/listar")
    public ResponseEntity<List<Persona>> obtenerTodasPersonas() {
        List<Persona> personas = personaServicio.obtenerTodas();
        if (personas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(personas);
        }
    }

    @GetMapping("/Obtener/{id}")
    public ResponseEntity<Persona> obtenerPersonaXid(@PathVariable int id) throws PersonaInvalidaException {
        return ResponseEntity.ok(personaServicio.obtenerPorId(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<Persona> guardarPersona(@RequestBody Persona persona) throws PersonaInvalidaException {
        Persona personaGuardada = personaServicio.guardar(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaGuardada);
    }

}
