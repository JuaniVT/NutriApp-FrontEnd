package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;
@Entity
@Data
@Table(name = "comidas_favoritas")
public class ComidaFavorita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotBlank(message = "El nombre del paquete es obligatorio")
    private String nombrePaquete;

    @NotBlank(message = "El nombre de la comida es obligatorio")
    private String nombreComida;

    @Positive(message = "El ID de la comida debe ser positivo")
    private long comidaId;

    @Positive(message = "La cantidad debe ser mayor a cero")
    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = false)
    @JsonIgnore  // Para que no se arme ciclo infinito al hacer toString o JSON
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;
}

