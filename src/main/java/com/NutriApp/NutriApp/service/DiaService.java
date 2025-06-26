package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.DiaInvalidoException;
import com.NutriApp.NutriApp.modelo.Dia;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.repository.DiaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaService {

    private final DiaRepository diaRepository;
    private final UsuarioService usuarioService;


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



    public Dia verDiaCompleto(LocalDate fecha){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();

        Dia dia = obtenerDiaPorFecha(fecha, user)
                .orElseThrow(() -> new DiaInvalidoException("No se encontro el dia registrado con fecha: " + fecha));


        return dia;

    }


    public List<Dia> verHistorialDias() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();
        Usuario usuarioConDias = usuarioService.obtenerUsuarioConDias(user.getUsername());

        List<Dia> dias = usuarioConDias.getDias();

        if (dias.isEmpty()){
            throw new DiaInvalidoException("El usuario todavia no tiene dias cargados ");
        }

        return dias;

    }


}

