package com.NutriApp.NutriApp.modelo.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioRequest {
    private UsuarioDTO usuario;
    private PersonaDTO persona;
    private PerfilNutricionalDTO perfilNutricional;
}

