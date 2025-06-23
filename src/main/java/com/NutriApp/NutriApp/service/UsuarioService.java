package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.dto.UsuarioDTO;
import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.exceptions.UsuarioInexistenteException;
import com.NutriApp.NutriApp.exceptions.UsuarioInvalidoException;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaService personaService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public boolean existsByUsername(String username) {
        if (usuarioRepository.existsByUsername(username)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean existsByDni (String username){
        if (usuarioRepository.existsByUsername(username)) {
            return true;
        }
        return false;
    }


    public Usuario buscarPorEmail(String username){

        return usuarioRepository.findById(username)
                .orElseThrow(() -> new UsuarioInexistenteException("Usuario no encontrado con el email : " + username));

    }

    // Crear nueva usuario
    public void guardar(Usuario user) {

        usuarioRepository.save(user);
    }

    //este metodo inserta un usuario con rol cliente
    // por defecto a una persona ya registrada
    @Transactional
    public void insertarUsuarioCliente(Usuario usuario, int idPerosonaBuscar) throws PersonaInvalidaException, UsuarioInvalidoException, AuthorityInvalidaException {

        if (usuarioRepository.existsById(usuario.getUsername())) {
            throw new UsuarioInvalidoException("El usuario ya existe con el username = " + usuario.getUsername());
        }

        Persona persona = personaService.obtenerPorId(idPerosonaBuscar);

        if (persona.getUsuario() != null) {
            throw new PersonaInvalidaException("La persona ya tiene un usuario asociado");
        }

        Authority authority = Authority.builder()

                .authority(Role.ROL_CLIENT)      //se setea por defecto en este metodo el rol de cliente
                .usuario(usuario)
                .build();


        usuario.setPersona(persona);

        usuario.setRole(authority);  //hace falta setearle los 3 objetos a cada uno porque tenemos una relacion bidireccional


        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //se cifra la contraseña
        usuario.setEnabled(true);    //se setea la cuenta como activa


        //----IMPORTANTE----//        // Limpia el contexto de persistencia (entityManager) para evitar conflictos
//        entityManager.clear();        // con entidades duplicadas ya gestionadas en la sesión actual de Hibernate.
        //----IMPORTANTE----//        // En este caso, evita el error de identidad duplicada al asociar una Persona
        // ya cargada con un nuevo Usuario.


        usuarioRepository.save(usuario); //se guarda en la bdd
    }

    @Transactional
    public void actualizarDatosUsuario (UsuarioDTO usuario)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = (Usuario) auth.getPrincipal();
        user.setUsername(usuario.getUsername());
        user.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(user);
    }

}

