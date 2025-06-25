package com.NutriApp.NutriApp.modelo.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlimentoBusquedaDTO {

    @NotNull(message = "El fdcId es obligatorio")
    @Min(value = 1, message = "El fdcId debe ser mayor o igual a 1")
    private Long fdcId;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;



}