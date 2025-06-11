package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonaService {

    // No es necesario usar @Autowired para inyectar la instancia de PersonaRepository,
    // ya que Spring detecta que esta clase (PersonaService) tiene un único constructor
    // que recibe como parámetro un bean (PersonaService) (aca lo especificamos con @RequierdArgsContructor).
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

    // Obtener datos de mi perfil
    public Persona obtenerMiPerfil() throws PersonaInvalidaException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal(); // si tu clase Usuario implementa UserDetails
        Persona persona = usuario.getPersona();
        return persona;
    }

    public boolean existsByDni(String dni) {
        if (personaRepository.existsByDni(dni)) {
            return true;
        }
        return false;
    }

    // Crear nueva persona
    public void guardar(Persona persona) throws PersonaInvalidaException {
        if (personaRepository.existsByDni(persona.getDni())) {
            throw new PersonaInvalidaException("La persona a ingresar ya se encuentra registrada");
        }
        personaRepository.save(persona);
    }
}
