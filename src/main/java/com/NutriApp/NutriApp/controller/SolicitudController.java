package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.SolicitudInvalidaException;
import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import com.NutriApp.NutriApp.service.SolicitudService;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/solicitud")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PostMapping("/insertar")
    public ResponseEntity<String> insertar (@RequestBody SolicitudAltaAlimento solicitudAltaAlimento) throws SolicitudInvalidaException {
        solicitudService.insertar(solicitudAltaAlimento);

        return ResponseEntity.ok("Solicitud enviada con exito");
    }

    @GetMapping("/listar/todas")
    public ResponseEntity<List<SolicitudAltaAlimento>> listarTodas (){
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    @GetMapping("/filtrar/fecha")
    public ResponseEntity<List<SolicitudAltaAlimento>> filtrarDedeUnaFecha (@RequestParam LocalDate fechaFiltrar){
        return ResponseEntity.ok(solicitudService.filtrarSolicitudesPorFecha(fechaFiltrar));
    }

    @GetMapping ("/filtrar/username")
    public ResponseEntity<List<SolicitudAltaAlimento>> filtrarPorUsername (@RequestParam String username){
        return ResponseEntity.ok(solicitudService.filtrarSolicitudesPorUsername(username));
    }

    @GetMapping("/filtrar/nombreComida")
    public ResponseEntity<List<SolicitudAltaAlimento>> filtrarPorNombreComida (@RequestParam String nombreComida){
        return ResponseEntity.ok(solicitudService.filtrarPorNombrecomida(nombreComida));
    }

    @GetMapping("/listar/misSolicitudes")
    public ResponseEntity<List<SolicitudAltaAlimento>> listarMisSolicitudes (){
        return ResponseEntity.ok(solicitudService.listarMisSolicitudes());
    }




}
