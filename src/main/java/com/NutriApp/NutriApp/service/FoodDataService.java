package com.NutriApp.NutriApp.service;


import com.NutriApp.NutriApp.modelo.DTO.AlimentoBusquedaDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodDataService {

    @Value("${fdc.api.key}")  // <-- debe coincidir con application.properties
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<AlimentoBusquedaDTO> buscarAlimentosPorNombre(String nombre) throws Exception {
        String encodedName = URLEncoder.encode(nombre, StandardCharsets.UTF_8);

        String url = "https://api.nal.usda.gov/fdc/v1/foods/search?query=" + encodedName + "&api_key=" + apiKey;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Error al buscar alimentos: " + response.getStatusCode());
        }

        JsonNode root = new ObjectMapper().readTree(response.getBody());
        JsonNode foods = root.path("foods");

        List<AlimentoBusquedaDTO> resultados = new ArrayList<>();
        for (JsonNode food : foods) {
            Long fdcId = food.path("fdcId").asLong();
            String descripcion = food.path("description").asText();
            resultados.add(new AlimentoBusquedaDTO(fdcId, descripcion));
        }
        return resultados;
    }

    public JsonNode obtenerDetallePorId(Long fdcId) throws Exception {
        String url = "https://api.nal.usda.gov/fdc/v1/food/" + fdcId + "?api_key=" + apiKey;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Error al obtener detalles del alimento: " + response.getStatusCode());
        }

        return new ObjectMapper().readTree(response.getBody());
    }
}