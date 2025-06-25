package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.ComidaFavoritaDTO;
import com.NutriApp.NutriApp.modelo.dto.ComidaIngeridaDTO;
import com.NutriApp.NutriApp.modelo.dto.ModificarCantidadComidaFavoritaDTO;
import com.NutriApp.NutriApp.modelo.ComidaFavorita;
import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.NutriApp.NutriApp.service.ComidaFavoritaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@Validated
@RestController
@RequestMapping("/comidas-favoritas")
@RequiredArgsConstructor
public class ComidaFavoritaController {

    private final ComidaFavoritaService comidaFavoritaService;

    // Agregar comida favorita o actualizar cantidad si ya existe
    @PostMapping("/agregar")
    public ResponseEntity<String> agregarComidaFavorita(@Valid @RequestBody ComidaFavoritaDTO request) {
        try {
            comidaFavoritaService.agregarComidaFavorita(request
            );
            return ResponseEntity.ok("Comida favorita agregada/modificada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // Modificar solo la cantidad
    @PutMapping("/modificar-cantidad")
    public ResponseEntity<String> modificarCantidad(@Valid @RequestBody ModificarCantidadComidaFavoritaDTO dto) {
        try {
            comidaFavoritaService.modificarCantidadComidaFavorita(dto);
            return ResponseEntity.ok("Cantidad modificada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // Eliminar comida favorita por paquete y comidaId
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarComidaFavorita(@RequestParam String nombrePaquete,
                                                         @RequestParam long comidaId) {
        try {
            comidaFavoritaService.eliminarComidaFavorita(nombrePaquete, comidaId);
            return ResponseEntity.ok("Comida favorita eliminada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarPorPaquete(@RequestParam String nombrePaquete) {
        List<ComidaFavorita> favoritas = comidaFavoritaService.listarComidasFavoritasPorPaquete(nombrePaquete);

        if (favoritas.isEmpty()) {
            return ResponseEntity.ok("El paquete '" + nombrePaquete + "' no tiene comidas favoritas.");
        }

        return ResponseEntity.ok(favoritas);
    }

    // Agregar paquete completo a comidas ingeridas (día)
    @PostMapping("/agregar-paquete-a-dia")
    public ResponseEntity<String> agregarPaqueteADia(@RequestParam String nombrePaquete, @RequestParam TipoComida tipo, @RequestParam LocalDate dia) {
        try {
            comidaFavoritaService.agregarComidaFavoritaaIngerida(
                    nombrePaquete,
                    tipo,
                    dia
            );
            return ResponseEntity.ok("Paquete agregado al día correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


}
