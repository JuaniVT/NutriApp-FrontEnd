package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.ActividadFisicaDTO;
import com.NutriApp.NutriApp.modelo.dto.ActividadFisicaResponseDTO;
import com.NutriApp.NutriApp.exceptions.ActividadFisicaInvalidaException;
import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.TipoActividadFisica;
import com.NutriApp.NutriApp.service.ActividadFisicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/actividadesFisicas")
@RequiredArgsConstructor
public class ActividadFisicaController {


private final ActividadFisicaService actividadFisicaService;

@GetMapping("/tipos")
public ResponseEntity<List<String>> obtenerTiposActividadesDisponibles() {
    return ResponseEntity.ok(actividadFisicaService.obtenerTiposDisponibles());
}


@PostMapping("/agregar")
public ResponseEntity<String> agregarActividad(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO) throws ActividadFisicaInvalidaException {

    actividadFisicaService.agregarActividadFsicaRealizada(actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin());
    return ResponseEntity.ok("ActividadFisicaRealizada agregada con exito");

}

@PostMapping("/agregar/diaEspecifico")
public ResponseEntity<String> agregarActividad(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO, @RequestParam LocalDate fecha) throws ActividadFisicaInvalidaException {

    actividadFisicaService.agregarActividadFsicaRealizadaEnUnDiaEspecifico(actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin(), fecha);
    return ResponseEntity.ok("ActividadFisicaRealizada agregada con exito");

}

@GetMapping("/listarActividades/sistema")
public ResponseEntity<List<ActividadFisicaResponseDTO>> listarActividadesFisicas()throws ActividadFisicaInvalidaException  {
    List<ActividadFisicaResponseDTO> actividadesFisicas = actividadFisicaService.obtenerTodas();

    return ResponseEntity.ok(actividadesFisicas);


}

@GetMapping("/listarActividadesRealizadas")
public ResponseEntity<List<ActividadFisica>> listarActividadesFisicasRealizadas(@RequestParam LocalDate fecha) throws ActividadFisicaInvalidaException {

    List<ActividadFisica> actividadesRealizadas = actividadFisicaService.obtenerTodasPorDia(fecha);

    return ResponseEntity.ok(actividadesRealizadas);


}

@DeleteMapping("/eliminar")
public ResponseEntity<String> elminarActividadFisica(@RequestParam LocalDate fecha,@RequestParam long id)  throws ActividadFisicaInvalidaException {

    actividadFisicaService.elminarActividadFisica(fecha, id);
    return ResponseEntity.ok("Actividad eliminada con exito");

}

@GetMapping("/filtrar")
public ResponseEntity<List<ActividadFisica>> filtrarActividadFisicasDelSistema(@RequestParam TipoActividadFisica tipoActividad) throws ActividadFisicaInvalidaException  {

    List<ActividadFisica> actividades = actividadFisicaService.filtrarActividadFisicasDelSistema(tipoActividad);

    return ResponseEntity.ok(actividades);

}

@GetMapping("/filtrar/actividadesRealizadas")
public ResponseEntity<List<ActividadFisica>> filtrarActividadesFisicasRealizadas(@RequestParam LocalDate fecha, @RequestParam TipoActividadFisica tipoActividad) throws ActividadFisicaInvalidaException  {

    List<ActividadFisica> actividades = actividadFisicaService.filtrarActividadesFisicasRealizadas(fecha,tipoActividad);

    return ResponseEntity.ok(actividades);
}

@PutMapping("/modificar")
public ResponseEntity<String> modificarActividadFisica(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO,@RequestParam LocalDate fecha, @RequestParam long id) throws ActividadFisicaInvalidaException {

    actividadFisicaService.modificarActividadFisica(fecha,id, actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin());

    return ResponseEntity.ok("Actividad fisica modificada con exito");

}

}
