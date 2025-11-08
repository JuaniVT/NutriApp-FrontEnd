package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.ActividadFisicaDTO;
import com.NutriApp.NutriApp.modelo.dto.ActividadFisicaResponseDTO;
import com.NutriApp.NutriApp.exceptions.ActividadFisicaInvalidaException;
import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.TipoActividadFisica;
import com.NutriApp.NutriApp.service.ActividadFisicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Actividades Fisicas", description = "Operaciones con las actividades fisicas del sistema y las realizadas por el usuario")
public class ActividadFisicaController {


    private final ActividadFisicaService actividadFisicaService;

    @Operation(summary = "Obtener listado de tipos de actividad.", description = "Devuelve una lista de los tipos de actividad cargados en el sistema.")
    @GetMapping("/tipos")
    public ResponseEntity<List<String>> obtenerTiposActividadesDisponibles() {
        return ResponseEntity.ok(actividadFisicaService.obtenerTiposDisponibles());
    }


    @Operation(summary = "Agregar una actividad fisica realizada.", description = "Agrega una actividad fisica realizada al dia actual.")
    @PostMapping("/agregar")
    public ResponseEntity<String> agregarActividad(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO) throws ActividadFisicaInvalidaException {

        actividadFisicaService.agregarActividadFsicaRealizada(actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin());
        return ResponseEntity.ok("ActividadFisicaRealizada agregada con exito");

    }

    @Operation(summary = "Agregar una actividad fisica realizada en un dia.", description = "Agrega una actividad fisica realizada en un dia en especifico.")
    @PostMapping("/agregar/diaEspecifico")
    public ResponseEntity<String> agregarActividad(@RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO, @RequestParam LocalDate fecha) throws ActividadFisicaInvalidaException {

        actividadFisicaService.agregarActividadFsicaRealizadaEnUnDiaEspecifico(actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin(), fecha);
        return ResponseEntity.ok("ActividadFisicaRealizada agregada con exito");

    }

    @Operation(summary = "Listar todas las actividades realizadas del sistema.", description = "Devuelve una lista de las actividades realizadas de todos los usuarios.")
    @GetMapping("/listarActividades/sistema")
    public ResponseEntity<List<ActividadFisicaResponseDTO>> listarActividadesFisicas()throws ActividadFisicaInvalidaException  {
        List<ActividadFisicaResponseDTO> actividadesFisicas = actividadFisicaService.obtenerTodas();

        return ResponseEntity.ok(actividadesFisicas);


    }

    @Operation(summary = "Listar las actividades fisicas realizadas en una fecha.", description = "Devuelve una lista de actividades fisicas realizadas por el usuario logeado en una fecha en especifico.")
    @GetMapping("/listarActividadesRealizadas")
    public ResponseEntity<List<ActividadFisica>> listarActividadesFisicasRealizadas(@RequestParam LocalDate fecha) throws ActividadFisicaInvalidaException {

        List<ActividadFisica> actividadesRealizadas = actividadFisicaService.obtenerTodasPorDia(fecha);

        return ResponseEntity.ok(actividadesRealizadas);


    }

    @Operation(summary = "Eliminar una actividad fisica realizada por nosotros.", description = "Elimina una actividad fisica realizada por el usuario logeado en un dia especifo.")
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> elminarActividadFisica(@Parameter(description = "Fecha de la actividad a eliminar") @RequestParam LocalDate fecha,
                                                         @Parameter(description = "Id de la actividad realizada a eliminar") @RequestParam long id)  throws ActividadFisicaInvalidaException {

        actividadFisicaService.elminarActividadFisica(fecha, id);
        return ResponseEntity.ok("Actividad eliminada con exito");

    }

    @Operation(summary = "Filtrar actividades fisicas realizadas de todo el sistema.", description = "Devuelve una lista filtrando por tipo de actividad fisica realizada de todos los usuarios.")
    @GetMapping("/filtrar")
    public ResponseEntity<List<ActividadFisica>> filtrarActividadFisicasDelSistema(@RequestParam TipoActividadFisica tipoActividad) throws ActividadFisicaInvalidaException  {

        List<ActividadFisica> actividades = actividadFisicaService.filtrarActividadFisicasDelSistema(tipoActividad);

        return ResponseEntity.ok(actividades);

    }


    @Operation(summary = "Filtrar actividades fisicas realizadas del usuario.", description = "Devuelve una lista filtrando por tipo de actividad fisica realizada del usuario logeado.")
    @GetMapping("/filtrar/actividadesRealizadas")
    public ResponseEntity<List<ActividadFisica>> filtrarActividadesFisicasRealizadas(@Parameter(description = "Fecha de la actividad a filtrar") @RequestParam LocalDate fecha,
                                                                                     @Parameter(description = "Tipo de la actividad a filtrar") @RequestParam TipoActividadFisica tipoActividad) throws ActividadFisicaInvalidaException  {

        List<ActividadFisica> actividades = actividadFisicaService.filtrarActividadesFisicasRealizadas(fecha,tipoActividad);

        return ResponseEntity.ok(actividades);
    }

    @Operation(summary = "Modificar una actividade fisica realizada por el usuario.", description = "Modifica una actividad fisica realizada del usuario logeado.")
    @PutMapping("/modificar")
    public ResponseEntity<String> modificarActividadFisica(@Parameter(description = "Actividad fisica modificada") @RequestBody @Valid ActividadFisicaDTO actividadFisicaDTO,
                                                           @Parameter(description = "Fecha de la actividad a modificar") @RequestParam LocalDate fecha,
                                                           @Parameter(description = "Id de la actividad a modificar") @RequestParam long id) throws ActividadFisicaInvalidaException {

        actividadFisicaService.modificarActividadFisica(fecha,id, actividadFisicaDTO.getTipoActividad(), actividadFisicaDTO.getIntensidad(), actividadFisicaDTO.getDuracionMin());

        return ResponseEntity.ok("Actividad fisica modificada con exito");

    }

}
