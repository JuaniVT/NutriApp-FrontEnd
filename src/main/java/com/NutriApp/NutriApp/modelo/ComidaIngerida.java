package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private String nombrePaquete;

    private String nombreComida;

    private Double calorias;

    private Double proteinas;

    private Double grasas;

    private Double carbohidratos;

    private Double cantidad;  // cantidad de porciones

    private TipoComida tipoComida;

    private Long idComidaApi;

    @ManyToOne
    @JoinColumn(name = "dia_id", nullable = false, unique = false, foreignKey = @ForeignKey(name = "fk_comidaIngerida_dia"))
    @JsonIgnore  // Para que no se arme ciclo infinito al hacer toString o JSON
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dia dia;
}
