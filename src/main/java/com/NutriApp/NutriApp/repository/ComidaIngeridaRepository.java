package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.ComidaIngerida;
import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ComidaIngeridaRepository extends JpaRepository<ComidaIngerida, Long> {

    @Query("""
            SELECT ci FROM ComidaIngerida ci
            JOIN ci.dia d
            JOIN d.usuario u
            WHERE u.persona.id = :userId
              AND d.fecha = :fecha
              AND ci.idComidaApi = :comidaId
              AND ci.tipoComida = :tipoComida
            """)
    Optional<ComidaIngerida> findByUserIdAndFechaAndComidaIdAndTipoComida(
            @Param("userId") int userId,
            @Param("fecha") LocalDate fecha,
            @Param("comidaId") Long comidaId,
            @Param("tipoComida") TipoComida tipoComida);
}