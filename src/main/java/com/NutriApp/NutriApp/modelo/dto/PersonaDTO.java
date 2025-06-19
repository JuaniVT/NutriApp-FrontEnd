package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.Genero;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String nombre;

    @NotBlank
    @Size(min = 3, max = 50)
    private String apellido;

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotNull
    @Past
    private LocalDate fechaNacimiento;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;

    @NotNull
    private Genero genero;

    @NotBlank
    @Email
    private String email;
}
