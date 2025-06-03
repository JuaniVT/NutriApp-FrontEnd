package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.dto.LoginRequest;
import com.NutriApp.NutriApp.dto.LoginResponse;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import com.NutriApp.NutriApp.service.JwtService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Define que esta clase manejará peticiones HTTP
@RequestMapping("/auth") // El endpoint completo será /auth/login
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // Autenticamos al usuario con nombre y contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Obtenemos los detalles del usuario desde la base de datos
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        // Generamos el token JWT
        String token = jwtService.generateToken(user);

        // Devolvemos el token en la respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponse> registrarUsuario(@RequestBody Usuario request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(null);
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
        usuario.setRole(Authority.builder()
                .authority(Role.ROL_ADMIN)
                .username(request.getUsername())
                .build());
        usuarioRepository.save(usuario);

        authorityRepository.save(usuario.getRole());

        // Cargar UserDetails para generar token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        // Devolver token en la respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }
}