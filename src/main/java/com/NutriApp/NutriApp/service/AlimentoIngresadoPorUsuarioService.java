package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.repository.AlimentoIngresadoPorUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlimentoIngresadoPorUsuarioService {

    @Autowired
    private AlimentoIngresadoPorUsuarioRepository alimentoRepository;

    public boolean existsByNombre (String nombre){
        return alimentoRepository.existsByNombreComidaIgnoreCase(nombre);
    }


}
