package com.NutriApp.NutriApp.Repositorio;

import com.NutriApp.NutriApp.Modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepositorio extends JpaRepository <Persona, Integer> {
    boolean existsByDni(String dni);
}
