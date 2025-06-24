package com.NutriApp.NutriApp.Init;

import com.NutriApp.NutriApp.modelo.dto.PerfilNutricionalDTO;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.PerfilNutricional;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Genero;
import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.ObjetivoCaloricoTipo;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import com.NutriApp.NutriApp.service.PerfilNutricionalService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CargaInicialDeUsuarios {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilNutricionalService perfilNutricionalService;


    //esta notacion le dice a spring que ejecute esto al inicializar la aplicacion
    @PostConstruct
    public void inicializarUsuarios (){
        if (!usuarioRepository.existsById("ekianuruzuna")) {
            usuarioRepository.save(usuarioEkian());
        }

        if (!usuarioRepository.existsById("zuriuruzuna")) {
            usuarioRepository.save(usuarioZuri());
        }

        if (!usuarioRepository.existsById("JuaniVT")) {
            usuarioRepository.save(usuarioJuani());
        }

        if (!usuarioRepository.existsById("valentinsacchetta")) {
            usuarioRepository.save(usuarioValentin());
        }
    }

    private Usuario usuarioZuri(){
        Usuario usuario = Usuario.builder()
                .username("zuriuruzuna")
                .password(passwordEncoder.encode("zuri"))
                .enabled(true)
                .build();

        Persona persona = (Persona.builder()
                .nombre("zuri")
                .apellido("uruzuna")
                .dni("47057835")
                .email("zuriuruzuna@gmail.com")
                .fechaNacimiento(LocalDate.of(2005, 9, 4))
                .telefono("2236826147")
                .genero(Genero.MASCULINO)
                .direccion("Udine 1355")
                .usuario(usuario)
                .build());

        Authority authority = (Authority.builder()
                .authority(Role.ROLE_ADMIN)
                .usuario(usuario)
                .build());

        PerfilNutricionalDTO perfilDTO = PerfilNutricionalDTO.builder()
                .peso(70.0)
                .altura(173.0)
                .nivelActividadFisica(NivelActividadFisica.MUY_INTENSA)
                .edad(19)
                .objetivoCaloricoTipo(ObjetivoCaloricoTipo.SUPERAVIT_LIGERO)
                .build();

        PerfilNutricional perfilNutricional = perfilNutricionalService.realizar_calculo_BMR(perfilDTO, persona.getGenero());
        usuario.setPerfilNutricional(perfilNutricional);
        usuario.setRole(authority);
        usuario.setPersona(persona);

        usuario.setFechaActiva(LocalDate.now());

        return usuario;
    }

    private Usuario usuarioValentin(){

        Usuario usuario = Usuario.builder()
                .username("valentinsacchetta")
                .password(passwordEncoder.encode("valentin"))
                .enabled(true)
                .build();

        Persona persona = (Persona.builder()
                .nombre("valentin")
                .apellido("sacchetta")
                .dni("45543487")
                .email("valen6sacchetta@gmail.com")
                .fechaNacimiento(LocalDate.of(2004, 2, 12))
                .telefono("2235982283")
                .genero(Genero.MASCULINO)
                .direccion("Puan 3449")
                .usuario(usuario)
                .build());

        Authority authority = (Authority.builder()
                .authority(Role.ROLE_ADMIN)
                .usuario(usuario)
                .build());

        PerfilNutricionalDTO perfilDTO = PerfilNutricionalDTO.builder()
                .peso(70.0)
                .altura(173.0)
                .nivelActividadFisica(NivelActividadFisica.MODERADA)
                .edad(19)
                .objetivoCaloricoTipo(ObjetivoCaloricoTipo.SUPERAVIT_LIGERO)
                .build();

        PerfilNutricional perfilNutricional = perfilNutricionalService.realizar_calculo_BMR(perfilDTO, persona.getGenero());
        usuario.setPerfilNutricional(perfilNutricional);

        usuario.setRole(authority);
        usuario.setPersona(persona);
        usuario.setFechaActiva(LocalDate.now());


        return (usuario);
    }

    private Usuario usuarioJuani(){

        Usuario usuario = Usuario.builder()
                .username("JuaniVT")
                .password(passwordEncoder.encode("juanjuli2"))
                .enabled(true)
                .build();

        Persona persona = (Persona.builder()
                .nombre("Juan Ignacio")
                .apellido("Valle Torres")
                .dni("46277918")
                .email("juanignaciovalletorres241104@gmail.com")
                .fechaNacimiento(LocalDate.of(2004, 11, 24))
                .telefono("2235836600")
                .genero(Genero.MASCULINO)
                .direccion("Ortiz de Zarate 7062")
                .usuario(usuario)
                .build());

        Authority authority = (Authority.builder()
                .authority(Role.ROLE_ADMIN)
                .usuario(usuario)
                .build());

        PerfilNutricionalDTO perfilDTO = PerfilNutricionalDTO.builder()
                .peso(74.0)
                .altura(173.0)
                .nivelActividadFisica(NivelActividadFisica.INTENSA)
                .edad(20)
                .objetivoCaloricoTipo(ObjetivoCaloricoTipo.SUPERAVIT_LIGERO)
                .build();

        PerfilNutricional perfilNutricional = perfilNutricionalService.realizar_calculo_BMR(perfilDTO, persona.getGenero());
        usuario.setPerfilNutricional(perfilNutricional);

        usuario.setRole(authority);
        usuario.setPersona(persona);

        usuario.setFechaActiva(LocalDate.now());

        return (usuario);
    }

    private Usuario usuarioEkian (){

        Usuario usuario = Usuario.builder()
                .username("ekianuruzuna")
                .password(passwordEncoder.encode("ekian"))
                .enabled(true)
                .build();

        Persona persona = (Persona.builder()
                .nombre("ekian")
                .apellido("uruzuna")
                .dni("47057836")
                .email("ekianuruzuna@gmail.com")
                .fechaNacimiento(LocalDate.of(2005, 9, 4))
                .telefono("2236826147")
                .genero(Genero.MASCULINO)
                .direccion("Udine 1355")
                .usuario(usuario)
                .build());

        Authority authority = (Authority.builder()
                .authority(Role.ROLE_ADMIN)
                .usuario(usuario)
                .build());

        PerfilNutricionalDTO perfilDTO = PerfilNutricionalDTO.builder()
                .peso(70.0)
                .altura(173.0)
                .nivelActividadFisica(NivelActividadFisica.MUY_INTENSA)
                .edad(19)
                .objetivoCaloricoTipo(ObjetivoCaloricoTipo.SUPERAVIT_LIGERO)
                .build();

        PerfilNutricional perfilNutricional = perfilNutricionalService.realizar_calculo_BMR(perfilDTO, persona.getGenero());
        usuario.setPerfilNutricional(perfilNutricional);
        usuario.setRole(authority);
        usuario.setPersona(persona);

        usuario.setFechaActiva(LocalDate.now());

        return (usuario);
    }
}
