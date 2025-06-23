package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.service.ActividadFisicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actividad")
@RequiredArgsConstructor
public class ActividadFisicaController {


    private final ActividadFisicaService actividadFisicaService;

}
