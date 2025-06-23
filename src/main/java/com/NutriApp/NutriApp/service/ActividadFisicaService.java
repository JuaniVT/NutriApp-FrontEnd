package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.ActividadFisicaInvalidaException;
import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.repository.ActividadFisicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActividadFisicaService {

    private final ActividadFisicaRepository actividadFisicaRepository;


    public void guardar (ActividadFisica actividadFisica){


        actividadFisicaRepository.save(actividadFisica);

    }



}
