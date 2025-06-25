package com.NutriApp.NutriApp.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

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
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$", message = "Solo se permiten letras y espacios", groups = ValidacionBasica.class)
    @Size(min = 2, max = 20, groups = ValidacionBasica.class)
    private String nombreComida;

    @NotNull    // con esto creo un grupo de validaciones que cuando yo le meto un @Validated a un parametro de un objeto de esta clase,
    @PositiveOrZero(groups = ValidacionBasica.class)   // lo pongo para que me valide solo los campos del grupo que yo especifique porque en este caso necesito que no me valide
    private double porcion;                      // el nombre de comida

    @NotNull
    @DecimalMin(value = "0.0", groups = ValidacionBasica.class)
    private Double calorias;

    @NotNull
    @DecimalMin(value = "0.0", groups = ValidacionBasica.class)
    private Double proteinas;

    @NotNull
    @DecimalMin(value = "0.0", groups = ValidacionBasica.class)
    private Double grasas;

    @NotNull
    @DecimalMin(value = "0.0", groups = ValidacionBasica.class)
    private Double carbohidratos;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_solicitud_usuario")/*anotacion para generar un nombre interno de la FK en la bdd*/)
    @JsonIgnore
    private Usuario usuario;

    @JsonProperty ("username")
    public String getUsername(){
        return usuario.getUsername();
    }

    @Override
    public String toString() {
        return "SolicitudAltaAlimento{" +
                "id=" + id +
                ", nombreComida='" + nombreComida + '\'' +
                ", porcion=" + porcion +
                ", calorias=" + calorias +
                ", proteinas=" + proteinas +
                ", grasas=" + grasas +
                ", carbohidratos=" + carbohidratos +
                ", fecha=" + fecha +
                ", usuario=" + usuario.getUsername() +
                '}';
    }

    public void setearDatosDesdeNuevaSolicitud(SolicitudAltaAlimento nueva) {
        if (nueva.getNombreComida() != null) this.setNombreComida(nueva.getNombreComida());
        if (nueva.getPorcion() != 0.0) this.setPorcion(nueva.getPorcion());
        if (nueva.getCalorias() != null) this.setCalorias(nueva.getCalorias());
        if (nueva.getProteinas() != null) this.setProteinas(nueva.getProteinas());
        if (nueva.getGrasas() != null) this.setGrasas(nueva.getGrasas());
        if (nueva.getCarbohidratos() != null) this.setCarbohidratos(nueva.getCarbohidratos());
    }
}
