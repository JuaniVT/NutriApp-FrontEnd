package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @NotNull
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 3, max = 50) String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank @Size(min = 3, max = 50) String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank @Size(min = 3, max = 50) String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank @Size(min = 3, max = 50) String apellido) {
        this.apellido = apellido;
    }

    public @NotBlank @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos") String getDni() {
        return dni;
    }

    public void setDni(@NotBlank @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos") String dni) {
        this.dni = dni;
    }

    public @NotBlank @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(@NotBlank @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public @NotBlank String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotBlank String telefono) {
        this.telefono = telefono;
    }

    public @NotBlank String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotBlank String direccion) {
        this.direccion = direccion;
    }

    public @NotBlank Genero getGenero() {
        return genero;
    }

    public void setGenero(@NotBlank Genero genero) {
        this.genero = genero;
    }

    public @NotBlank @Email(message = "El email debe tener un formato válido y contener '@'") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email(message = "El email debe tener un formato válido y contener '@'") String email) {
        this.email = email;
    }
}
