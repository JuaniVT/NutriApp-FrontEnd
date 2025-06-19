package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.ComidaIngerida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComidaIngeridaRepository extends JpaRepository <ComidaIngerida, Long> {
}
