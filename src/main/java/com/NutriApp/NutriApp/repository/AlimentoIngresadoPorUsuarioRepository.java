package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.AlimentoIngresadoPorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentoIngresadoPorUsuarioRepository extends JpaRepository<AlimentoIngresadoPorUsuario, Integer> {

    boolean existsByNombreComidaIgnoreCase(String nombreComida);
    boolean existsById (long id);
}
