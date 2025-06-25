package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rol")
@PreAuthorize("hasRole('ADMIN')")
public class AuthorityController {

    private final AuthorityService authorityService;

    @PostMapping("/cambiar/admin")
    public ResponseEntity<String> cambiarRolAdmin (@RequestParam String username) throws AuthorityInvalidaException {
        authorityService.cambiaRol_A_ADMIN(username);

        return ResponseEntity.ok("Se cambio el rol a ADMIN correctamente");
    }

    @PostMapping("/cambiar/cliente")
    public ResponseEntity<String> cambiarRolCliente (@RequestParam String username) throws AuthorityInvalidaException {
        authorityService.cambiaRol_A_CLIENTE(username);

        return ResponseEntity.ok("Se cambio el rol a CLIENTE correctamente");
    }


}
