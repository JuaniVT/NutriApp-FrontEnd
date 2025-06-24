package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.modelo.Dia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActividadFisicaRepository extends JpaRepository <ActividadFisica, Long> {

    List<ActividadFisica> findActividadFisicasByDia(Dia dia);

}
