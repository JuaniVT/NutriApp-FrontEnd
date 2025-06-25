package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.AlimentoIngresadoPorUsuario;
import com.NutriApp.NutriApp.service.AlimentoIngresadoPorUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos-usuario")
public class AlimentoIngresadoPorElUsuarioController {

    @Autowired
    private AlimentoIngresadoPorUsuarioService alimentoIngresadoPorUsuarioService;

    //solo admins
    @GetMapping("/listarTodos")
    public ResponseEntity<List<AlimentoIngresadoPorUsuario>> listarTodos (){
        return ResponseEntity.ok(alimentoIngresadoPorUsuarioService.listarTodos());
    }

    //solo admins
    @GetMapping("/filtrar")
    public ResponseEntity<List<AlimentoIngresadoPorUsuario>> filtrarPorUsername (@RequestParam String nombreComida){
        return ResponseEntity.ok(alimentoIngresadoPorUsuarioService.filtrarAlimentosPorNombreComida(nombreComida));
    }



}
