package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.dto.LoginRequest;
import com.NutriApp.NutriApp.dto.LoginResponse;
import com.NutriApp.NutriApp.dto.RegistroUsuarioRequest;
import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.exceptions.UsuarioExistente;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsuarioService usuarioDetailsService;
    @Autowired
    private PersonaService personaService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(@RequestBody LoginRequest request) {

        // Autenticamos al usuario con nombre y contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Obtenemos los detalles del usuario desde la base de datos
        UserDetails user = usuarioDetailsService.loadUserByUsername(request.getUsername());

        // Generamos el token JWT
        String token = jwtService.generateToken(user);

        // Devolvemos el token en la respuesta
        return new LoginResponse(token);
    }

    public LoginResponse registrarUsuario(@RequestBody RegistroUsuarioRequest request) throws UsuarioExistente, PersonaInvalidaException {

        if (usuarioDetailsService.existsByUsername(request.getUsuario().getUsername())) {
            throw new UsuarioExistente("El nombre de usaurio ya está en uso");
        }

        if (personaService.existsByDni(request.getPersona().getDni())) {
            throw new PersonaInvalidaException("La persona a ingresar ya se encuentra registrada");
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
        personaService.guardar(persona);


        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsuario().getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getUsuario().getPassword()));
        usuario.setEnabled(true);
        usuario.setPersona(persona);

        Authority authority = Authority.builder()
                .authority(Role.ROL_ADMIN)
                .usuario(usuario)
                .build();

        usuario.setRole(authority);
        usuarioDetailsService.guardar(usuario);

        authorityRepository.save(usuario.getRole());

        // Cargar UserDetails para generar token
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(request.getUsuario().getUsername());
        String token = jwtService.generateToken(userDetails);

        // Devolver token en la respuesta
        return new LoginResponse(token);
    }

}
