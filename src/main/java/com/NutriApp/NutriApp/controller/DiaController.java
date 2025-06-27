package com.NutriApp.NutriApp.controller;


import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.service.DiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("dias")
@RequiredArgsConstructor
public class DiaController {


    private final DiaService diaService;

    @GetMapping("/ver/completo")
    public ResponseEntity<Dia> verDiaCompleto(@RequestParam LocalDate fecha){

        return ResponseEntity.ok(diaService.verDiaCompleto(fecha));

    }

    @GetMapping("ver/todos")
    public ResponseEntity<List<Dia>> verHistorialDias(){

        return ResponseEntity.ok(diaService.verHistorialDias());

    }


}
