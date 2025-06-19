package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Dia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaRepository extends JpaRepository <Dia, Long> {
}
