package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.AlimentoInvalidoException;
import com.NutriApp.NutriApp.exceptions.AlimetoIngreadoPorElUsuarioException;
import com.NutriApp.NutriApp.modelo.AlimentoIngresadoPorUsuario;
import com.NutriApp.NutriApp.modelo.dto.AlimentoBusquedaDTO;
import com.NutriApp.NutriApp.repository.AlimentoIngresadoPorUsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//Service para englobar los alimentos de la api y los de nuestra BDD
@Service
public class AlimentoGlobalService {

    @Autowired
    private AlimentoIngresadoPorUsuarioService alimentoIngresadoPorUsuarioService;

    @Autowired
    private FoodDataService foodDataService;

    @Autowired
    private NutricionService nutricionService;


    //lista alimentos combinando de la api con los de nuestra BDD
    @Transactional
    public List<AlimentoBusquedaDTO>  filtarAlimentosCombinadosPorNombreComida (String nombreComida) throws Exception{

        //obtenemos una lista de alimentos de nustra BDD y la convierto a una de AliemtnoBusquedaDTO para que este igual a los de la api
        List<AlimentoBusquedaDTO> alimentosBDD = alimentoIngresadoPorUsuarioService.convertirListaDTO(alimentoIngresadoPorUsuarioService.filtrarAlimentosPorNombreComidaSinException(nombreComida));

        //obtenemos los alimentos de la api
        List<AlimentoBusquedaDTO> alimentosAPI = foodDataService.buscarAlimentosPorNombreSinException(nombreComida);

        //validamos las dos listas
        if (alimentosBDD.isEmpty() && alimentosAPI.isEmpty()){
            throw new AlimentoInvalidoException("No se encotro ninguna comida con el nombre = " + nombreComida);
        }

        //creamos la lista que va a unificar
        List<AlimentoBusquedaDTO> listasUnificadas = new ArrayList<>();

        //unificamos las dos listas
        listasUnificadas.addAll(alimentosBDD);
        listasUnificadas.addAll(alimentosAPI);

        return listasUnificadas;
    }
}
