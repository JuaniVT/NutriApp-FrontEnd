package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.PerfilNutricional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilNutricionalRepository extends JpaRepository <PerfilNutricional, Integer> {

}
