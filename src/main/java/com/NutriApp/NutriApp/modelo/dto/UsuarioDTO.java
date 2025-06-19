package com.NutriApp.NutriApp.modelo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;
}

