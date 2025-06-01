package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.exceptions.UsuarioInvalidoException;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaService personaService;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;



    //Ese método es obligatorio si estás usando Spring Security con autenticación basada en formulario(formLogin()),
    //porque Spring necesita saber cómo obtener al usuario desde tu base de datos.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public Usuario obtener (String username) throws UsuarioInvalidoException {
        return usuarioRepository.findById(username)
                .orElseThrow(() -> new UsuarioInvalidoException(username));
    }

    public void insertarUsuarioCliente (Usuario usuario) throws PersonaInvalidaException, UsuarioInvalidoException, AuthorityInvalidaException{

        if (usuarioRepository.findById(usuario.getUsername()).isPresent()){
            throw new UsuarioInvalidoException("El usuario ya existe con el id = " +usuario.getUsername());
        }

        Persona persona = personaService.obtenerPorId(usuario.getPersona().getId());

        Authority authority = Authority.builder()
                .usuario(usuario)
                .username(usuario.getUsername())
                .role(Role.ROL_CLIENT)      //se setea por defecto en este metodo el rol de cliente
                .build();

        persona.setUsuario(usuario);
        usuario.setPersona(persona);    //hace falta setearle los 3 objetos a cada uno porque tenemos una relacion bidireccional
        usuario.setAuthority(authority);

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //se cifra la contraseña

        usuarioRepository.save(usuario); //se guarda en la bdd
        authorityService.insertar(authority);
    }

    public void insertarUsuarioAdmin (Usuario usuario) throws PersonaInvalidaException, UsuarioInvalidoException, AuthorityInvalidaException, AuthenticationException {

        if (usuarioRepository.findById(usuario.getUsername()).isPresent()){
            throw new UsuarioInvalidoException("El usuario ya existe con el id = " +usuario.getUsername());
        }

        Persona persona = personaService.obtenerPorId(usuario.getPersona().getId());

        Authority authority = Authority.builder()
                .usuario(usuario)
                .username(usuario.getUsername())
                .role(Role.ROL_ADMIN)      //se setea por defecto en este metodo el rol de admin
                .build();

        persona.setUsuario(usuario);
        usuario.setPersona(persona);    //hace falta setearle los 3 objetos a cada uno porque tenemos una relacion bidireccional
        usuario.setAuthority(authority);

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //se cifra la contraseña

        usuarioRepository.save(usuario); //se guarda en la bdd
        authorityService.insertar(authority);
    }

}

