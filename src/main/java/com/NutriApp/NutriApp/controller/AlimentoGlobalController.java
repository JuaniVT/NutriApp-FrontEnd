package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.AlimentoBusquedaDTO;
import com.NutriApp.NutriApp.service.AlimentoGlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


//controladora para unificar los alimentos de nuestra bdd con la api
@RestController
@RequestMapping("/api/alimentos/global")
public class AlimentoGlobalController {

    @Autowired
    private AlimentoGlobalService alimentoGlobalService;

    //cualquiera logeado
    @GetMapping("/buscar")
    public ResponseEntity<List<AlimentoBusquedaDTO>> buscarPorNombre (@RequestParam String nombreComida) throws Exception{
        return ResponseEntity.ok(alimentoGlobalService.filtarAlimentosCombinadosPorNombreComida(nombreComida));
    }
}
