package com.NutriApp.NutriApp.modelo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres")
    @Pattern(regexp = ".*\\D.*", message = "El nombre de usuario no puede contener solo números")
    @Pattern(regexp = ".*[a-zA-Z0-9].*", message = "El nombre de usuario debe contener al menos una letra o número")
    @Pattern(regexp = "\\S+", message = "El nombre de usuario no debe contener espacios")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "El nombre de usuario solo puede contener letras, números, puntos y guiones bajos")
    @Pattern(regexp = "^(?![._])[a-zA-Z0-9._]+(?<![._])$", message = "El nombre de usuario no puede comenzar ni terminar con punto o guion bajo")
    @Pattern(regexp = "^(?!.*[._]{2})[a-zA-Z0-9._]+$", message = "El nombre de usuario no puede contener caracteres especiales consecutivos")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "La contraseña debe contener al menos una letra, un número y un carácter especial"
    )
    private String password;
}

