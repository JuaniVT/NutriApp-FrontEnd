package com.NutriApp.NutriApp.modelo.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioRequest {

    @NotNull(message = "El usuario es obligatorio")
    @Valid
    private UsuarioDTO usuario;

    @NotNull(message = "La persona es obligatoria")
    @Valid
    private PersonaDTO persona;

    @NotNull(message = "El perfil nutricional es obligatorio")
    @Valid
    private PerfilNutricionalDTO perfilNutricional;
}

