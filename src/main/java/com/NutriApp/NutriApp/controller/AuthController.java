package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.dto.LoginRequest;
import com.NutriApp.NutriApp.dto.LoginResponse;
import com.NutriApp.NutriApp.dto.PerfilNutricionalDTO;
import com.NutriApp.NutriApp.dto.RegistroUsuarioRequest;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import com.NutriApp.NutriApp.service.AuthService;
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
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponse> registrarUsuario(@RequestBody RegistroUsuarioRequest request, @RequestBody PerfilNutricionalDTO perfilNutricionalDTO) {
        return ResponseEntity.ok(authService.registrarUsuario(request, perfilNutricionalDTO));
    }

}