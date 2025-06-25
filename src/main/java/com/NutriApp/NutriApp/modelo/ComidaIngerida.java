package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comidaIngerida")
public class ComidaIngerida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la comida es obligatorio")
    private String nombreComida;

    @NotNull(message = "Las calorías son obligatorias")
    @PositiveOrZero(message = "Las calorías no pueden ser negativas")
    private Double calorias;

    @NotNull(message = "Las proteínas son obligatorias")
    @PositiveOrZero(message = "Las proteínas no pueden ser negativas")
    private Double proteinas;

    @NotNull(message = "Las grasas son obligatorias")
    @PositiveOrZero(message = "Las grasas no pueden ser negativas")
    private Double grasas;

    @NotNull(message = "Los carbohidratos son obligatorios")
    @PositiveOrZero(message = "Los carbohidratos no pueden ser negativos")
    private Double carbohidratos;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Double cantidad;  // cantidad de porciones

    @NotNull(message = "El tipo de comida es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoComida tipoComida;

    @NotNull(message = "El ID de comida API es obligatorio")
    private Long idComidaApi;

    @ManyToOne
    @JoinColumn(name = "dia_id", nullable = false, unique = false, foreignKey = @ForeignKey(name = "fk_comidaIngerida_dia"))
    @JsonIgnore  // Para que no se arme ciclo infinito al hacer toString o JSON
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dia dia;
}
