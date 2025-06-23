package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRespository extends JpaRepository<SolicitudAltaAlimento, Integer> {

    boolean existsByNombreComida (String nombreComida);
    List<SolicitudAltaAlimento> findAllByUsuarioUsername (String username);
    List<SolicitudAltaAlimento> findAllByNombreComida (String nombreComida);
    void deleteByUsuarioUsernameAndNombreComidaIgnoreCase(String username, String nombreComida);
    boolean existsByUsuarioUsernameAndNombreComidaIgnoreCase(String username, String nombreComida);//me elimino los usuarios, personas authorities y demas

}
