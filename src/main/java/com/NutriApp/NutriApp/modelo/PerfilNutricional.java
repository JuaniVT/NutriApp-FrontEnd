package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.ObjetivoCaloricoTipo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "NutritionalProfile")
public class PerfilNutricional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser un número positivo")
    @Max(value = 500, message = "El peso no puede superar los 500 kg")
    private Double peso;    // kg

    @NotNull(message = "La altura es obligatoria")
    @Positive(message = "La altura debe ser un número positivo")
    @Max(value = 300, message = "La altura no puede superar los 300 cm")
    private Double altura;  // cm

    @NotNull(message = "El nivel de actividad física es obligatorio")
    private NivelActividadFisica nivelActividadFisica;

    @PositiveOrZero(message = "El GEB no puede ser negativo")
    private Double GEB;

    @NotNull(message = "El objetivo calórico es obligatorio")
    private ObjetivoCaloricoTipo objetivoCaloricoTipo;

    @PositiveOrZero(message = "El objetivo diario no puede ser negativo")
    private Double objetivoDiario;

    @Min(value = 1, message = "La edad debe ser al menos 1 año")
    @Max(value = 130, message = "La edad no puede superar los 130 años")
    private int edad;

    @OneToOne(mappedBy = "perfilNutricional", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude //esto hace falta para que no entre en un ciclo infinito cuando se llama al tostring
    @JsonIgnore
    private Usuario usuario;

    @JsonProperty ("username")  //le estamos diciendo que cuando agararre un json de este objeto tambien tome este como atributo, ya que el usuario lo ignora con el @JsonIgnore
    public String getUsername(){
        return usuario.getUsername();
    }

}
