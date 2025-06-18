package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.NutriApp.NutriApp.service.ComidaIngeridaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/comidas")  // prefijo de la ruta
@RequiredArgsConstructor
public class ComidaIngeridaController {

    private final ComidaIngeridaService comidaIngeridaService;

    @PostMapping("/agregar")
    public void agregarComidaIngerida(@RequestParam Long idComida,
                                      @RequestParam Double gramos,
                                      @RequestParam TipoComida tipoComida,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) throws Exception {
        comidaIngeridaService.agregarComidaIngerida(idComida, gramos, tipoComida, fecha);
    }
}
