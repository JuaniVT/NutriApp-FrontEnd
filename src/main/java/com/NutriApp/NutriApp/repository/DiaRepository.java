package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaRepository extends JpaRepository<Dia, Long> {

    Optional<Dia> findByFechaAndUsuario(LocalDate fecha, Usuario usuario);

    List<Dia> findByUsuario(Usuario usuario);
}