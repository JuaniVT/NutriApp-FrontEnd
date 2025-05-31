package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
@RequiredArgsConstructor
public class PersonaService {

    // No es necesario usar @Autowired para inyectar la instancia de PersonaRepository,
    // ya que Spring detecta que esta clase (PersonaService) tiene un único constructor
    // que recibe como parámetro un bean (PersonaService).
    // Spring automáticamente realiza la inyección de dependencias usando ese constructor.
    // Si existieran múltiples constructores, Spring no sabría cuál usar y se necesitaría
    // especificar la inyección de otra manera (por ejemplo, con @Autowired).
    private final PersonaRepository personaRepository;



    public List<Persona> obtenerTodas() {
        return personaRepository.findAll();
    }

    // Obtener persona por ID
    public Persona obtenerPorId(int id) throws PersonaInvalidaException {
        return personaRepository.findById(id)
                .orElseThrow(() -> new PersonaInvalidaException("Persona no encontrada con ID: " + id));
    }

    // Crear nueva persona
    public Persona guardar(Persona persona) throws PersonaInvalidaException {
        if (personaRepository.existsByDni(persona.getDni())) {
            throw new PersonaInvalidaException("La persona a ingresar ya se encuentra registrada");
        }
        return personaRepository.save(persona);
    }
}
