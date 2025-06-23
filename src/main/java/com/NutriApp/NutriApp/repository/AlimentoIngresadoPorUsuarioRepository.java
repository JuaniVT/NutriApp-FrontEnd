package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.modelo.AlimentoIngresadoPorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlimentoIngresadoPorUsuarioRepository extends JpaRepository<AlimentoIngresadoPorUsuario, Integer> {

    boolean existsByNombreComida (String nombreComida);

    @Query("""
       SELECT new com.NutriApp.NutriApp.dto.MacronutrienteDTO(
           a.nombreComida,
           a.id_comida,
           a.calorias,
           a.proteinas,
           a.grasas,
           a.carbohidratos,
           a.gramosPorPorcion
       )
       FROM alimentoIngresadoPorUsuario a
       WHERE a.nombreComida = :nombreComida AND a.id = :id
       """)
    Optional<MacronutrienteDTO> findMacronutrientesByNombreComidaAndId(@Param("nombreComida") String nombreComida, @Param("id") Long id);
}
