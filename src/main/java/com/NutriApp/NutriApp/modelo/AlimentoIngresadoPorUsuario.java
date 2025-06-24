package com.NutriApp.NutriApp.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity (name = "alimentoIngresadoPorUsuario")
public class AlimentoIngresadoPorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String nombreComida;

    @NotNull
    @Positive
    private Double gramosPorPorcion;

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


    public boolean esValido(){
        if (nombreComida == null){
            return false;
        }

        if (gramosPorPorcion == null){
            return false;
        }

        if (calorias == null){
            return false;
        }

        if (proteinas == null){
            return false;
        }

        if (grasas == null){
            return false;
        }

        if (carbohidratos == null){
            return false;
        }

        return true;
    }
}
