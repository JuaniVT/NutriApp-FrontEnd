package com.NutriApp.NutriApp.dto;

import lombok.Data;
/*es el DTO que el backend le devuelve al cliente después de que el login fue exitoso.
📤 Contiene:
Un token JWT (que el cliente usará en las siguientes peticiones para autenticarse)
(Opcional) Info adicional como roles, nombre de usuario, fecha de expiración, etc.*/

@Data
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

}