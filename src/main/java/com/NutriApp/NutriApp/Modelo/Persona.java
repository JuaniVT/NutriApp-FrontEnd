package com.NutriApp.NutriApp.Modelo;

import com.NutriApp.NutriApp.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "persona")
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nombre;

    @NotBlank
    @Size(min = 3, max = 50)
    private String apellido;

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    @Column(unique = true)
    private String dni;

    @NonNull
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;

    @NotNull
    private Genero genero;

    @NotBlank
    @Email(message = "El email debe tener un formato válido y contener '@'")
    private String email;

}
