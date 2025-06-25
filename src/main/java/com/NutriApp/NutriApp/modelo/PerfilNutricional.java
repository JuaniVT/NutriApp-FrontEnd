package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.NivelActividadFisica;
import com.NutriApp.NutriApp.modelo.enums.ObjetivoCaloricoTipo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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

    private Double peso;    // kg

    private Double altura;  // cm

    private NivelActividadFisica nivelActividadFisica;

    private Double GEB;

    private ObjetivoCaloricoTipo objetivoCaloricoTipo;

    private Double objetivoDiario;

    private long edad;

    private Double imb;

    @OneToOne(mappedBy = "perfilNutricional", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude //esto hace falta para que no entre en un ciclo infinito cuando se llama al tostring
    @JsonIgnore
    private Usuario usuario;

    @JsonProperty ("username")  //le estamos diciendo que cuando agararre un json de este objeto tambien tome este como atributo, ya que el usuario lo ignora con el @JsonIgnore
    public String getUsername(){
        return usuario.getUsername();
    }

    public void setImb(double imb) {
        this.imb = imb;
    }

    public @NotBlank @Size(min = 3, max = 50) String getNombre() {
        if (usuario != null && usuario.getPersona() != null) {
            return usuario.getPersona().getNombre();
        }
        return "NULO";
    }

}
