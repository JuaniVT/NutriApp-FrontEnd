package com.NutriApp.NutriApp.service;


import com.NutriApp.NutriApp.exceptions.SolicitudInvalidaException;
import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.repository.AlimentoIngresadoPorUsuarioRepository;
import com.NutriApp.NutriApp.repository.SolicitudRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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


        solicitudRespository.save(solicitud);

        mailService.enviarMail("ekianuruzuna@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
        mailService.enviarMail("zuriuruzuna6@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
        mailService.enviarMail("juanignaciovalletorres241104@gmail.com", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
        mailService.enviarMail("sachetamail", "Solicitud de Alta de Comida", "Se solicito la alta de esta comida = " +solicitud);
    }
}
