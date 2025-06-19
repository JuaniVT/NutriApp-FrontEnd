package com.NutriApp.NutriApp.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity (name = "solicitudAltaAlimento")
public class SolicitudAltaAlimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String nombreComida;

    @NotNull
    @Positive
    private Long porcion;

    @NotNull
    @DecimalMin("0.0")
    private Double calorias;

    @NotNull
    @DecimalMin("0.0")
    private Double proteinas;

    @NotNull
    @DecimalMin("0.0")
    private Double grasas;

    @NotNull
    @DecimalMin("0.0")
    private Double carbohidratos;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_solicitud_usuario")/*anotacion para generar un nombre interno de la FK en la bdd*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;


}
