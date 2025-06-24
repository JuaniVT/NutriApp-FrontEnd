package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.dto.ActividadFisicaResponseDTO;
import com.NutriApp.NutriApp.exceptions.ActividadFisicaInvalidaException;
import com.NutriApp.NutriApp.exceptions.DiaInvalidoException;
import com.NutriApp.NutriApp.modelo.ActividadFisica;
import com.NutriApp.NutriApp.modelo.ActividadesFisicas.Correr;
import com.NutriApp.NutriApp.modelo.ActividadesFisicas.Gym;
import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.repository.ActividadFisicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActividadFisicaService
{

private final ActividadFisicaRepository actividadFisicaRepository;
private final DiaService diaService;


public void guardar (ActividadFisica actividadFisica){

    actividadFisicaRepository.save(actividadFisica);

}

public void agregarActividadFsicaRealizada(String tipoActividad, NivelActividadFisica intensidad,double duracionMin){

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Usuario user = (Usuario) auth.getPrincipal();

    Dia dia = diaService.obtenerODiaOCrear(user.getFechaActiva(),user);

    ActividadFisica actividadFisica;
    switch (tipoActividad.toLowerCase()) {
        case "correr":
            actividadFisica = new Correr();
            break;
        case "gym":
            actividadFisica = new Gym();
            break;

        default:

            throw new ActividadFisicaInvalidaException("Tipo de actividad no reconocido : " + tipoActividad);
    }

    actividadFisica.setDia(dia);
    actividadFisica.setIntensidad(intensidad);
    actividadFisica.setDuracionMin(duracionMin);
    actividadFisica.setCaloriasGastadas(actividadFisica.calcularCalorias(user.getPerfilNutricional()));

    actividadFisicaRepository.save(actividadFisica);


}


public List<ActividadFisicaResponseDTO> obtenerTodas(){
    List<ActividadFisica> actividadFisicas = actividadFisicaRepository.findAll();
    if (actividadFisicas.isEmpty()){
        throw new ActividadFisicaInvalidaException("No se encontraron actividades fisicas cargadas");
    }

    // DTO para poder asignarle el usuario y el dia en el que se agrego la actividad
    return actividadFisicas.stream().map(actividad ->
            new ActividadFisicaResponseDTO(
                    actividad.getClass().getSimpleName().toLowerCase(), // tipoActividad
                    actividad.getIntensidad(),
                    actividad.getDuracionMin(),
                    actividad.getCaloriasGastadas(),
                    actividad.getDia().getFecha(),                      // fecha
                    actividad.getDia().getUsuario().getUsername()       // username
            )
    ).toList();
}


public List<ActividadFisica> obtenerTodasPorDia(LocalDate fecha){

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Usuario user = (Usuario) auth.getPrincipal();

    Dia dia = diaService.obtenerDiaPorFecha(fecha, user)
            .orElseThrow(() -> new DiaInvalidoException("No se encontro el dia registrado con fecha: " + fecha));

    List<ActividadFisica> actividadFisicasRealizadas = actividadFisicaRepository.findActividadFisicasByDia(dia);

    if (actividadFisicasRealizadas.isEmpty()){
        throw new ActividadFisicaInvalidaException("No se encontraron actividades fisicas cargadas para el dia : " + dia);
    }

    return actividadFisicasRealizadas;
}


    public List<String> obtenerTiposDisponibles() {
        return List.of("correr", "gym"); // mantenido manualmente por ahora
    }









}