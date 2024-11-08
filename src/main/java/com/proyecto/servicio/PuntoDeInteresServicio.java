package com.proyecto.servicio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.modelo.dto.PuntoDeInteresDTO;
import com.proyecto.modelo.dto.TipoLugar;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

//PuntoDeInteresServicio.java
@Service
@Slf4j  // Añadido para mejor logging
public class PuntoDeInteresServicio {
 
 private static final String API_URL = "https://analisis.datosabiertos.jcyl.es/api/explore/v2.1/catalog/datasets/registro-de-infraestructuras-culturales/records";
 
 private final RestTemplate restTemplate;
 private final ObjectMapper objectMapper;

 @Autowired
 public PuntoDeInteresServicio(RestTemplate restTemplate, ObjectMapper objectMapper) {
     this.restTemplate = restTemplate;
     this.objectMapper = objectMapper;
 }


 public List<PuntoDeInteresDTO> obtenerPuntosDeInteres() {
	    List<PuntoDeInteresDTO> puntosDeInteres = new ArrayList<>();
	    try {
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                .queryParam("limit", 20)
	                .queryParam("exclude", "calle:null")
	                .build()
	                .toUriString();
	        log.info("URL generada para la solicitud: {}", url);

	        // Obtén la respuesta en bruto
	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);

	        // Procesa los resultados
	        JsonNode results = rootNode.path("results");
	        if (results.isMissingNode() || !results.isArray()) {
	            log.error("El nodo 'results' no existe o no es un array.");
	            throw new RuntimeException("La respuesta de la API no contiene datos en 'results'.");
	        }

	        // Mapea cada registro a PuntoDeInteresDTO
	        for (JsonNode record : results) {
	            try {
	                PuntoDeInteresDTO punto = mapearCampos(record);
	                puntosDeInteres.add(punto);
	            } catch (Exception e) {
	                log.error("Error al procesar registro: {}", e.getMessage());
	            }
	        }
	    } catch (Exception e) {
	        log.error("Error al obtener puntos de interés: {}", e.getMessage());
	        throw new RuntimeException("Error al obtener puntos de interés", e);
	    }
	    return puntosDeInteres;
	}

 private PuntoDeInteresDTO mapearCampos(JsonNode record) {
	    log.info("Procesando registro: {}", record); // Log para depurar

	    // Acceso directo a los campos dentro del nodo `record`
	    String nombreOrganismo = record.path("nombre_del_organismo").asText("");
	    Long id = parseLong(record.path("identificador").asText(""));
	    String calle = record.path("calle").asText("");
	    String localidad = record.path("localidad").asText("");
	    Double latitud = parseDouble(record.path("latitud").asText());
	    Double longitud = parseDouble(record.path("longitud").asText());
	    TipoLugar tipoLugar = TipoLugar.fromString(nombreOrganismo);

	    // Depuración de valores extraídos
	    log.info("Nombre: {}, Calle: {}, Localidad: {}, Lat: {}, Lon: {}",
	            nombreOrganismo, calle, localidad, latitud, longitud);

	    return new PuntoDeInteresDTO(
	    		id,
	            nombreOrganismo,
	            calle,
	            localidad,
	            latitud,
	            longitud,
	            tipoLugar
	    );
	}
 //rutaPreder
 public List<PuntoDeInteresDTO> obtenerPuntosPorIds(List<Long> ids) {
	    List<PuntoDeInteresDTO> puntosFiltrados = new ArrayList<>();
	    try {
	        for (Long id : ids) {
	            try {
	                PuntoDeInteresDTO punto = obtenerPuntoPorId(id);
	                if (punto != null) {
	                    puntosFiltrados.add(punto);
	                }
	            } catch (Exception e) {
	                log.warn("Error al obtener el punto con ID {}: {}", id, e.getMessage());
	            }
	        }
	    } catch (Exception e) {
	        log.error("Error en obtenerPuntosPorIds: {}", e.getMessage(), e);
	        throw e;
	    }
	    return puntosFiltrados;
	}

	public PuntoDeInteresDTO obtenerPuntoPorId(Long id) {
	    try {
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                .queryParam("where", "identificador=" + id) // Ajusta según la API
	                .build()
	                .toUriString();

	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (results.isArray() && results.size() > 0) {
	            JsonNode record = results.get(0); // Solo el primer resultado
	            return new PuntoDeInteresDTO(
	                    parseLong(record.path("fields").path("identificador").asText("")),
	                    record.path("fields").path("nombre_del_organismo").asText(""),
	                    record.path("fields").path("calle").asText(""),
	                    record.path("fields").path("localidad").asText(""),
	                    parseDouble(record.path("fields").path("latitud").asText("")),
	                    parseDouble(record.path("fields").path("longitud").asText("")),
	                    TipoLugar.fromString(record.path("fields").path("nombre_del_organismo").asText(""))
	            );
	        }
	    } catch (Exception e) {
	        log.error("Error al obtener punto con ID {}: {}", id, e.getMessage());
	    }
	    return null;
	}


 private Double parseDouble(String value) {
     try {
         return Double.parseDouble(value);
     } catch (NumberFormatException e) {
         return 0.0;
     }
 }
 
 private Long parseLong(String value) {
	    try {
	        return Long.parseLong(value); // Convertir a Long
	    } catch (NumberFormatException e) {
	        log.warn("No se pudo parsear a Long: {}", value);
	        return 0L; // Valor predeterminado si hay error
	    }
	}
}

