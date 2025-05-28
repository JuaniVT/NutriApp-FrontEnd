package com.NutriApp.NutriApp.Servicio;

import com.NutriApp.NutriApp.Exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.Modelo.Persona;
import com.NutriApp.NutriApp.Repositorio.PersonaRepositorio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service

public class PersonaServicio {

    private final PersonaRepositorio personaRepository;

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
