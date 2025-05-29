package com.NutriApp.NutriApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    @Autowired
    private JdbcUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // http://localhost:8080/api/admin/users/create?username=test&password=1234
    @PostMapping("/create")
    public String createUser(@RequestParam String username, @RequestParam String password) {
        if (userDetailsManager.userExists(username)) {
            return "El usuario ya existe.";
        }

        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")  // se puede hacer dinamico y seleccionar el rol
                .build();

        userDetailsManager.createUser(user);
        return "Usuario creado correctamente.";
    }

    @DeleteMapping("/delete")
//http://localhost:8080/api/admin/users/delete?username=test
    public String deleteUser(@RequestParam String username) {
        if (!userDetailsManager.userExists(username)) {
            return "El usuario no existe.";
        }

        userDetailsManager.deleteUser(username);
        return "Usuario eliminado correctamente.";
    }
}

