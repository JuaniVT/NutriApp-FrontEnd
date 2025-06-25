package com.NutriApp.NutriApp.modelo.dto;

import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComidaIngeridaDTO {


    @NotNull(message = "El ID no puede ser nulo")
    @Min(value = 1, message = "El ID debe ser mayor que cero")
    private Long id;

    @NotBlank(message = "El nombre de la comida es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre de la comida debe tener entre 2 y 100 caracteres")
    private String nombreComida;

    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    private Double gramos;

    @NotNull(message = "El tipo de comida es obligatorio")
    private TipoComida tipoComida;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}
