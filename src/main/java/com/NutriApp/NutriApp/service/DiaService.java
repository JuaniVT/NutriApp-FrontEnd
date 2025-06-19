package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.repository.DiaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaService {

    private final DiaRepository diaRepository;


    // Crear nuevo dia
    public void guardar(Dia dia) {

        diaRepository.save(dia);
    }


    // Devuelve un día por fecha, o lo crea si no existe
    public Dia obtenerODiaOCrear(LocalDate fecha, Usuario usuario) {
        return diaRepository.findByFechaAndUsuario(fecha, usuario)
                .orElseGet(() -> {
                    Dia nuevoDia = new Dia();
                    nuevoDia.setFecha(fecha);
                    nuevoDia.setUsuario(usuario);
                    guardar(nuevoDia);
                    return nuevoDia;
                });
    }

    // Obtener un día por fecha
    public Optional<Dia> obtenerDiaPorFecha(LocalDate fecha, Usuario usuario) {
        return diaRepository.findByFechaAndUsuario(fecha, usuario);
    }


}

