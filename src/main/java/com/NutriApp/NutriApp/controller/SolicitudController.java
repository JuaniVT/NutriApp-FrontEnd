package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.SolicitudInvalidaException;
import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import com.NutriApp.NutriApp.service.SolicitudService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/listarTodas")
    public ResponseEntity<List<SolicitudAltaAlimento>> listarTodas (){
        return ResponseEntity.ok(solicitudService.listarTodas());
    }
}
