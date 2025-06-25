package com.NutriApp.NutriApp.modelo.dto;
import com.NutriApp.NutriApp.modelo.AlimentoIngresadoPorUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlimentoBusquedaDTO {

    private Long fdcId;

    private String descripcion;


    public static AlimentoBusquedaDTO from (AlimentoIngresadoPorUsuario alimento){
        return new AlimentoBusquedaDTO(alimento.getId(), alimento.getNombreComida());
    }


}