package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }

        Persona persona = new Persona();
        persona.setNombre(request.getPersona().getNombre());
        persona.setApellido(request.getPersona().getApellido());
        persona.setDni(request.getPersona().getDni());
        persona.setGenero(request.getPersona().getGenero());
        persona.setFechaNacimiento(request.getPersona().getFechaNacimiento());
        persona.setTelefono(request.getPersona().getTelefono());
        persona.setDireccion(request.getPersona().getDireccion());
        persona.setEmail(request.getPersona().getEmail());
        personaRepository.save(persona);

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEnabled(true);
        usuario.setPersona(persona);
        usuario.setAuthority(Authority.builder()
                .role(Role.ROL_CLIENT)
                .username(request.getUsername())
                .build());

        authorityRepository.save(usuario.getAuthority());


        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente.");
    }
}

