package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.Genero;
import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.ObjetivoCaloricoTipo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Tolerate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "persona")
public class Persona {

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
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 digitos numericos")
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

    private Double peso;

    private Double altura;

    private Integer edad;

    @Enumerated(EnumType.STRING)
    private NivelActividadFisica nivelActividadFisica;

    @Enumerated(EnumType.STRING)
    private ObjetivoCaloricoTipo objetivoCaloricoTipo;

    private Double GEB;

    private Double objetivoDiario;

    private Double imb;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Usuario usuario;

    @JsonProperty("username")
    public String getUsername(){
        return usuario != null ? usuario.getUsername() : null;
    }

    @Tolerate
    public Persona(String nombre, String apellido, String dni, LocalDate fechaNacimiento, String telefono, String direccion, Genero genero, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.genero = genero;
        this.email = email;
    }

}
