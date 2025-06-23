package com.NutriApp.NutriApp.service;


import com.NutriApp.NutriApp.exceptions.SolicitudInvalidaException;
import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.repository.AlimentoIngresadoPorUsuarioRepository;
import com.NutriApp.NutriApp.repository.SolicitudRespository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRespository solicitudRespository;

    @Autowired
    private AlimentoIngresadoPorUsuarioRepository alimentoIngresadoPorUsuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MailService mailService;

    @Autowired
    UsuarioRepository usuarioRepository;



    public void insertar (SolicitudAltaAlimento solicitud) throws SolicitudInvalidaException{
        if (alimentoIngresadoPorUsuarioRepository.existsByNombreComida(solicitud.getNombreComida())){
            throw new SolicitudInvalidaException("El alimento ya existe con el nombre = " +solicitud.getNombreComida());
        }

        if (solicitudRespository.existsByNombreComida(solicitud.getNombreComida())){
            throw new SolicitudInvalidaException("La solicitud ya existe con el nombre = " +solicitud.getNombreComida());
        }

        // Obtener el nombre de usuario desde el token (ya que está autenticado)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioService.loadUserByUsername(username);

        // Asignar el usuario a la solicitud
        solicitud.setUsuario(usuario);

        // Se le setea la fecha del momento de insertar la solicitud
        solicitud.setFecha(LocalDateTime.now());


        solicitudRespository.save(solicitud);

        mailService.enviarMail("ekianuruzuna@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
        mailService.enviarMail("zuriuruzuna6@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
        mailService.enviarMail("juanignaciovalletorres241104@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
        mailService.enviarMail("valen6sacchetta@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
    }


    // te lista todas las solicitudes ordenadas por fehca de creacion con un limite de 100 para no sobrecarcar
    public List<SolicitudAltaAlimento> listarTodas() throws SolicitudInvalidaException{
        if (solicitudRespository.count() == 0){
            throw new SolicitudInvalidaException("No hay solicitudes cargadas");
        }

        return solicitudRespository.findAll().stream()
                .sorted(new Comparator<SolicitudAltaAlimento>() {
                    @Override
                    public int compare(SolicitudAltaAlimento o1, SolicitudAltaAlimento o2) {
                        return o1.getFecha().compareTo(o2.getFecha());
                    }
                })
                .limit(100)
                .toList();
    }

    //filtra todas las solicitudes de una fecha en adelante
    public List<SolicitudAltaAlimento> filtrarSolicitudesPorFecha (LocalDate fechaFiltrar) throws SolicitudInvalidaException{
        if (solicitudRespository.count() == 0){
            throw new SolicitudInvalidaException("No hay solicitudes cargadas");
        }

        return solicitudRespository.findAll().stream()
                .sorted(new Comparator<SolicitudAltaAlimento>() {
                    @Override
                    public int compare(SolicitudAltaAlimento o1, SolicitudAltaAlimento o2) {
                        return o1.getFecha().compareTo(o2.getFecha());
                    }
                })
                .filter(x -> !x.getFecha().toLocalDate().isBefore(fechaFiltrar))    //filtra todo lo que no es antes de esa fecha
                .limit(100)
                .toList();
    }

    public List<SolicitudAltaAlimento> filtrarSolicitudesPorUsername (String username) throws SolicitudInvalidaException{
        if (solicitudRespository.count() == 0){
            throw new SolicitudInvalidaException("No hay solicitudes cargadas");
        }

        return solicitudRespository.findAllByUsuarioUsername(username).stream()
                .sorted(new Comparator<SolicitudAltaAlimento>() {
                    @Override
                    public int compare(SolicitudAltaAlimento o1, SolicitudAltaAlimento o2) {
                        return o1.getFecha().compareTo(o2.getFecha());
                    }
                })
                .limit(100)
                .toList();
    }

    public List<SolicitudAltaAlimento> filtrarPorNombrecomida (String nombreComida) throws SolicitudInvalidaException{
        if (solicitudRespository.count() == 0){
            throw new SolicitudInvalidaException("No hay solicitudes cargadas");
        }

        return solicitudRespository.findAllByNombreComida(nombreComida).stream()
                .sorted(Comparator.comparing(SolicitudAltaAlimento::getFecha))    //ordena comparando por fecha
                .limit(100)
                .toList();
    }

    public List<SolicitudAltaAlimento> listarMisSolicitudes () throws SolicitudInvalidaException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        List<SolicitudAltaAlimento> list = solicitudRespository.findAllByUsuarioUsername(usuario.getUsername()).stream()
                .sorted(new Comparator<SolicitudAltaAlimento>() {
                    @Override
                    public int compare(SolicitudAltaAlimento o1, SolicitudAltaAlimento o2) {
                        return o1.getFecha().compareTo(o2.getFecha());
                    }
                })
                .limit(100)
                .toList();

        if (list.isEmpty()){
            throw new SolicitudInvalidaException("Usted no tiene nignuna solicitud cargada en el sistema");
        }

        return list;
    }

    @Transactional
    public String elimiarMiSolicitud (String nombreComidaSolicitudEliminar) throws SolicitudInvalidaException{  //busca en sus solicitudes y si encuentra el mismo nombre de comida (ignora las mayusculas o minusculas) lo elimina, sino tira exception
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();      //obtengo el usuario logeado

        if (solicitudRespository.existsByUsuarioUsernameAndNombreComidaIgnoreCase(usuario.getUsername(), nombreComidaSolicitudEliminar)){ //si el usuario logeado tiene esa solicitud

            solicitudRespository.deleteByUsuarioUsernameAndNombreComidaIgnoreCase(usuario.getUsername(), nombreComidaSolicitudEliminar);  //se elimina la solicitud
            return "Solicitud eliminada con exito";
        }



        throw new SolicitudInvalidaException("Usted no tiene ninguna solicitud cargada con el nombre de comida = " + nombreComidaSolicitudEliminar);    //tira exception porque ese usuaior no tiene esa solicitud
    }
}
