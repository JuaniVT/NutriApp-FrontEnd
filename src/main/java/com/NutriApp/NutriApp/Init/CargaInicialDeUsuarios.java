package com.NutriApp.NutriApp.Init;

import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Genero;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
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


    //esta notacion le dice a spring que ejecute esto al inicializar la aplicacion
    @PostConstruct
    public void inicializarUsuarios (){
        if (!usuarioRepository.existsById("ekianuruzuna")) {
            usuarioRepository.save(usuarioEkian());
        }

        if (!usuarioRepository.existsById("zuriuruzuna")) {
            usuarioRepository.save(usuarioZuri());
        }

        if (!usuarioRepository.existsById("juanivalles")) {
            usuarioRepository.save(usuarioJuani());
        }

        if (!usuarioRepository.existsById("valentinssachetta")) {
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
                .role(Role.ROL_ADMIN)
                .usuario(usuario)
                .build());


        usuario.setAuthority(authority);
        usuario.setPersona(persona);

        return usuario;
    }

    private Usuario usuarioValentin(){

        Usuario usuario = Usuario.builder()
                .username("valentinssachetta")
                .password(passwordEncoder.encode("valentin"))
                .enabled(true)
                .build();

        Persona persona = (Persona.builder()
                .nombre("valentin")
                .apellido("ssachetta")
                .dni("47057880")
                .email("ekianuruzuna@gmail.com")
                .fechaNacimiento(LocalDate.of(2005, 9, 4))
                .telefono("2236826147")
                .genero(Genero.MASCULINO)
                .direccion("Udine 1355")
                .usuario(usuario)
                .build());

        Authority authority = (Authority.builder()
                .role(Role.ROL_ADMIN)
                .usuario(usuario)
                .build());


        usuario.setAuthority(authority);
        usuario.setPersona(persona);


        return (usuario);
    }

    private Usuario usuarioJuani(){

        Usuario usuario = Usuario.builder()
                .username("juanivalles")
                .password(passwordEncoder.encode("juani"))
                .enabled(true)
                .build();

        Persona persona = (Persona.builder()
                .nombre("juan")
                .apellido("valles")
                .dni("47057890")
                .email("ekianuruzuna@gmail.com")
                .fechaNacimiento(LocalDate.of(2005, 9, 4))
                .telefono("2236826147")
                .genero(Genero.MASCULINO)
                .direccion("Udine 1355")
                .usuario(usuario)
                .build());

        Authority authority = (Authority.builder()
                .role(Role.ROL_ADMIN)
                .usuario(usuario)
                .build());


        usuario.setAuthority(authority);
        usuario.setPersona(persona);


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
                .role(Role.ROL_ADMIN)
                .usuario(usuario)
                .build());


        usuario.setAuthority(authority);
        usuario.setPersona(persona);


        return (usuario);
    }
}
