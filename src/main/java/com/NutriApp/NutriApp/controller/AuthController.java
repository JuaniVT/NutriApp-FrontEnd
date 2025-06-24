package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.LoginRequest;
import com.NutriApp.NutriApp.modelo.dto.LoginResponse;
import com.NutriApp.NutriApp.modelo.dto.RegistroUsuarioRequest;
import com.NutriApp.NutriApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponse> registrarUsuario(@RequestBody RegistroUsuarioRequest request) {
        return ResponseEntity.ok(authService.registrarUsuario(request));
    }

    @GetMapping("/debug")
    public ResponseEntity<String> verRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + auth.getAuthorities());

        return ResponseEntity.ok("Mirá la consola");
    }

}