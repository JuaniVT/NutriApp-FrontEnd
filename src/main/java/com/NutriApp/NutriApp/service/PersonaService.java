package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

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
