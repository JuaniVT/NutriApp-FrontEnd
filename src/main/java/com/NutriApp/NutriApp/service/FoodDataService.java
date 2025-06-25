package com.NutriApp.NutriApp.service;


import com.NutriApp.NutriApp.exceptions.AlimentoInvalidoException;
import com.NutriApp.NutriApp.modelo.dto.AlimentoBusquedaDTO;
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
import java.util.Optional;

@Service
public class FoodDataService {


    // Inyectamos el valor de la API key definida en application.properties (clave para acceder a la API externa)
    @Value("${fdc.api.key}")
    private String apiKey;

    // Creamos un cliente HTTP que usaremos para hacer peticiones a la API externa
    private final RestTemplate restTemplate = new RestTemplate();


    // Método para buscar alimentos según el nombre ingresado por el usuario
    public List<AlimentoBusquedaDTO> buscarAlimentosPorNombre(String nombre) throws Exception {
        // Codificamos el nombre del alimento para que sea válido en una URL (espacios, acentos, etc.)
        String encodedName = URLEncoder.encode(nombre, StandardCharsets.UTF_8);

        // Armamos la URL del endpoint de búsqueda, agregando el nombre codificado y la API key
        String url = "https://api.nal.usda.gov/fdc/v1/foods/search?query=" + encodedName + "&api_key=" + apiKey;

        // Hacemos la petición HTTP GET a la API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        // Verificamos si la respuesta fue exitosa (código 200). Si no, lanzamos una excepción
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Error al buscar alimentos: " + response.getStatusCode());
        }

        // Convertimos la respuesta JSON a un árbol de nodos usando Jackson
        JsonNode root = new ObjectMapper().readTree(response.getBody());

        // Accedemos al nodo "foods" que contiene la lista de resultados
        JsonNode foods = root.path("foods");

        // Creamos una lista vacía donde guardaremos los resultados formateados como DTOs
        List<AlimentoBusquedaDTO> resultados = new ArrayList<>();

        // Recorremos todos los alimentos en el JSON
        for (JsonNode food : foods) {
            // Obtenemos el ID y la descripción de cada alimento
            Long fdcId = food.path("fdcId").asLong();
            String descripcion = food.path("description").asText();

            // Creamos un nuevo DTO con esos datos y lo agregamos a la lista de resultados
            resultados.add(new AlimentoBusquedaDTO(fdcId, descripcion));
        }

        //validamos si encontro algo
        if (resultados.isEmpty()){
            throw new AlimentoInvalidoException("No se encontro ninguna comida con el nombre = " + nombre);
        }

        // Devolvemos la lista de alimentos encontrados
        return resultados;
    }


    // Método para obtener los detalles completos de un alimento usando su ID (fdcId)
    public JsonNode obtenerDetallePorId(Long fdcId) throws Exception {
        // Armamos la URL del endpoint de detalles, incluyendo el ID y la API key
        String url = "https://api.nal.usda.gov/fdc/v1/food/" + fdcId + "?api_key=" + apiKey;

        // Hacemos la petición HTTP GET
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        // Verificamos si la respuesta fue exitosa
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Error al obtener detalles del alimento: " + response.getStatusCode());
        }

        // Convertimos la respuesta JSON en un objeto JsonNode (árbol JSON) y lo devolvemos
        return new ObjectMapper().readTree(response.getBody());
    }

    public Optional<JsonNode> obtenerDetallePorIdOptional(Long fdcId) {
        try {
            String url = "https://api.nal.usda.gov/fdc/v1/food/" + fdcId + "?api_key=" + apiKey;

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return Optional.empty();
            }

            JsonNode detalle = new ObjectMapper().readTree(response.getBody());
            return Optional.of(detalle);

        } catch (Exception e) {
            // Podés loguear si querés: log.error("Error al obtener detalles del alimento con ID: " + fdcId, e);
            return Optional.empty();
        }
    }


    //no se valida si encontro algo porque este metodo se usa en otro para unificar los alimentos de nuestra bdd y los de la api
    public List<AlimentoBusquedaDTO> buscarAlimentosPorNombreSinException(String nombre) throws Exception {
        // Codificamos el nombre del alimento para que sea válido en una URL (espacios, acentos, etc.)
        String encodedName = URLEncoder.encode(nombre, StandardCharsets.UTF_8);

        // Armamos la URL del endpoint de búsqueda, agregando el nombre codificado y la API key
        String url = "https://api.nal.usda.gov/fdc/v1/foods/search?query=" + encodedName + "&api_key=" + apiKey;

        // Hacemos la petición HTTP GET a la API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        // Verificamos si la respuesta fue exitosa (código 200). Si no, lanzamos una excepción
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Error al buscar alimentos: " + response.getStatusCode());
        }

        // Convertimos la respuesta JSON a un árbol de nodos usando Jackson
        JsonNode root = new ObjectMapper().readTree(response.getBody());

        // Accedemos al nodo "foods" que contiene la lista de resultados
        JsonNode foods = root.path("foods");

        // Creamos una lista vacía donde guardaremos los resultados formateados como DTOs
        List<AlimentoBusquedaDTO> resultados = new ArrayList<>();

        // Recorremos todos los alimentos en el JSON
        for (JsonNode food : foods) {
            // Obtenemos el ID y la descripción de cada alimento
            Long fdcId = food.path("fdcId").asLong();
            String descripcion = food.path("description").asText();

            // Creamos un nuevo DTO con esos datos y lo agregamos a la lista de resultados
            resultados.add(new AlimentoBusquedaDTO(fdcId, descripcion));
        }

        // Devolvemos la lista de alimentos encontrados
        return resultados;
    }


}
