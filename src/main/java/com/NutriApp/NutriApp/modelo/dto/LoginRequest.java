package com.NutriApp.NutriApp.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
// es un DTO usado para recibir los datos del usuario cuando quiere iniciar sesión (login).
//Contiene típicamente el username y la password.
@Data
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
