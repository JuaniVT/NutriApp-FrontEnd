package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.SolicitudInvalidaException;
import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import com.NutriApp.NutriApp.modelo.ValidacionBasica;
import com.NutriApp.NutriApp.service.SolicitudService;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.ValidationMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/solicitud")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    //cualquiera registrado
    @PostMapping("/insertar")
    public ResponseEntity<String> insertar (@RequestBody @Validated SolicitudAltaAlimento solicitudAltaAlimento) throws SolicitudInvalidaException {
        solicitudService.insertar(solicitudAltaAlimento);

        return ResponseEntity.ok("Solicitud enviada con exito");
    }

    //solo admins
    @GetMapping("/listar/todas")
    public ResponseEntity<List<SolicitudAltaAlimento>> listarTodas (){
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    //solo admins
    @GetMapping("/filtrar/fecha")
    public ResponseEntity<List<SolicitudAltaAlimento>> filtrarDedeUnaFecha (@RequestParam LocalDate fechaFiltrar){
        return ResponseEntity.ok(solicitudService.filtrarSolicitudesPorFecha(fechaFiltrar));
    }

    //solo admins
    @GetMapping ("/filtrar/username")
    public ResponseEntity<List<SolicitudAltaAlimento>> filtrarPorUsername (@RequestParam String username){
        return ResponseEntity.ok(solicitudService.filtrarSolicitudesPorUsername(username));
    }

    //solo admins
    @GetMapping("/filtrar/nombreComida")
    public ResponseEntity<List<SolicitudAltaAlimento>> filtrarPorNombreComida (@RequestParam String nombreComida){
        return ResponseEntity.ok(solicitudService.filtrarPorNombrecomida(nombreComida));
    }

    //cualquiera que este registrado
    @GetMapping("/listar/misSolicitudes")
    public ResponseEntity<List<SolicitudAltaAlimento>> listarMisSolicitudes (){
        return ResponseEntity.ok(solicitudService.listarMisSolicitudes());
    }

    //cualquiera que este registrado
    @DeleteMapping ("/eliminar")
    public ResponseEntity<String> eliminarMiSolicitud (@RequestParam String nombreComidaSolicitudEliminar){
        return ResponseEntity.ok(solicitudService.elimiarMiSolicitud(nombreComidaSolicitudEliminar));
    }

    //cualquiera que este registrado
    @PutMapping("/modificar/miSolicitud")
    public ResponseEntity<String> modificarMiSolicitud (String nombreComidaModificar, @RequestBody @Validated(ValidacionBasica.class) SolicitudAltaAlimento solicitudNueva){
        return ResponseEntity.ok(solicitudService.modificarMiSolicitud(nombreComidaModificar, solicitudNueva));
    }

    @PostMapping ("/aceptar")
    public ResponseEntity<String> acepatarSolicitud (@RequestParam long idSolicitud){
        return ResponseEntity.ok(solicitudService.aceptarSolicitud(idSolicitud));
    }

}
