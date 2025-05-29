package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository <Persona, Integer> {
    boolean existsByDni(String dni);
}
