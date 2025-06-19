package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.exceptions.UsuarioInvalidoException;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import com.NutriApp.NutriApp.repository.PersonaRepository;
import com.NutriApp.NutriApp.repository.UsuarioRepository;
import com.NutriApp.NutriApp.service.PersonaService;
import com.NutriApp.NutriApp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuarioCliente (@RequestBody @Validated Usuario usuario, @RequestParam int idPerosonaBuscar) throws PersonaInvalidaException, UsuarioInvalidoException, AuthorityInvalidaException {
        usuarioService.insertarUsuarioCliente(usuario, idPerosonaBuscar);

        return ResponseEntity.ok("Usuario registrado correctamente como cliente");
    }

}

