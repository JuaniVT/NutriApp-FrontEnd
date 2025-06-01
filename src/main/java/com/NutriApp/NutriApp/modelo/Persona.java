package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.Genero;
import com.NutriApp.NutriApp.modelo.enums.GeneroConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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
    @Convert(converter = GeneroConverter.class)
    private Genero genero;

    @NotBlank
    @Email(message = "El email debe tener un formato válido y contener '@'")
    private String email;

    // Es recomendable modelar las relaciones en ambos lados (bidireccionales) si se necesita
    // acceder a ambas entidades desde el código, no solo desde la base de datos.
    // Al usar JPA/Hibernate, modelar ambos lados permite navegar desde una entidad a otra,
    // y mantener sincronizadas las asociaciones en memoria.
    // El uso de 'mappedBy' indica que esta clase es de la que va a depender la otra clase relacionada.
    // EL uso del cascade para eliminar el con el cascade = TIPOCASCADE
    // y el orphanRemoval (este es para el OnDeleteCascade de mysql)
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude //esto hace falta para que no entre en un ciclo infinito cuando se llama al tostring
    private Usuario usuario;
}
