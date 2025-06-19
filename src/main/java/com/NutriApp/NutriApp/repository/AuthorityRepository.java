package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Authority;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository <Authority, Integer> {

    Optional<Authority> findByUsuarioUsername (String username);

    @Transactional
    @Modifying      //hace falta poner estas dos anotaciones cuadno hacemos querys que no sean solo de lectura
    @Query(value = "UPDATE authorities SET authority = 'ROLE_ADMIN' WHERE username = ?1", nativeQuery = true)
    void cambiarRol_A_ADMIN (String username);

    @Transactional
    @Modifying      //hace falta poner estas dos anotaciones cuadno hacemos querys que no sean solo de lectura
    @Query(value = "UPDATE authorities SET authority = 'ROLE_CLIENT' WHERE username = ?1", nativeQuery = true)
    void cambiarRol_A_CLIENTE (String username);

}
