package com.NutriApp.NutriApp.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioRequest {
    private UsuarioDTO usuario;
    private PersonaDTO persona;
}

