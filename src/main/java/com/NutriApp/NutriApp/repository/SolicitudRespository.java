package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import org.hibernate.validator.internal.engine.resolver.JPATraversableResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRespository extends JpaRepository<SolicitudAltaAlimento, Integer> {

    boolean existsByNombreComida (String nombreComida);

    List<SolicitudAltaAlimento> findAllByUsuarioUsername (String username);
}
