package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.service.PersonaService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
public class PersonaController {


    // No es necesario usar @Autowired para inyectar la instancia de PersonaService,
    // ya que Spring detecta que esta clase (PersonaController) tiene un único constructor
    // que recibe como parámetro un bean (PersonaService) (aca se lo especificamos ocn el @RequiredArgsConstructor).
    // Spring automáticamente realiza la inyección de dependencias usando ese constructor.
    // Si existieran múltiples constructores, Spring no sabría cuál usar y se necesitaría
    // especificar la inyección de otra manera (por ejemplo, con @Autowired).
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

    //La exception que se lanza su no se encunetra la persona, en este metodo se vuelva a lanzar delegando la responsabilidad
    // a la clase GlobalExceptionHandler (buena practica)
    @GetMapping("/obtener")
    public ResponseEntity<Persona> obtenerPersonaXid(@RequestParam int id) throws PersonaInvalidaException {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPersona(@RequestBody @Validated Persona persona) throws PersonaInvalidaException {
        personaService.guardar(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body("Persona guardada correctamente");
    }

}
