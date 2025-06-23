package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.ActividadFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadFisicaRepository extends JpaRepository <ActividadFisica, Long> {



}
