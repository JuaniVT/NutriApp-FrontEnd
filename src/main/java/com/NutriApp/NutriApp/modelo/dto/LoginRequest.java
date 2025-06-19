package com.NutriApp.NutriApp.modelo.dto;

import lombok.Data;
// es un DTO usado para recibir los datos del usuario cuando quiere iniciar sesión (login).
//Contiene típicamente el username y la password.
@Data
public class LoginRequest {
    private String username;
    private String password;
}
