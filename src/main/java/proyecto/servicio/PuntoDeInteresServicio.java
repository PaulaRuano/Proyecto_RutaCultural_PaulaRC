package proyecto.servicio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import proyecto.modelo.dto.CategoriaDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.utiles.Parsear;

@Service
@Slf4j  // Añadido para mejor logging
public class PuntoDeInteresServicio {

	private static final String API_URL = "https://analisis.datosabiertos.jcyl.es/api/explore/v2.1/catalog/datasets/registro-de-infraestructuras-culturales/records";

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final CategoriaServicio categoriaServicio;

	@Autowired
	public PuntoDeInteresServicio(RestTemplate restTemplate, ObjectMapper objectMapper, CategoriaServicio categoriaServicio) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.categoriaServicio = categoriaServicio;
	}

	public List<PuntoDeInteresDTO> obtenerPuntosDeInteres() {
	    List<PuntoDeInteresDTO> puntosDeInteres = new ArrayList<>();
	    try {
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                .queryParam("limit", 20)
	                .build()
	                .toUriString();
	        log.info("URL generada para la solicitud: {}", url);

	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (results.isMissingNode() || !results.isArray()) {
	            log.error("El nodo 'results' no existe o no es un array.");
	            throw new RuntimeException("La respuesta de la API no contiene datos en 'results'.");
	        }

	        for (JsonNode record : results) {
	            try {
	                // Filtrar registros con calle vacía o null
	                String calle = record.path("calle").asText("");
	                if (calle == null || calle.isEmpty()) {
	                    log.warn("Registro excluido por calle vacía o null: {}", record);
	                    continue;
	                }

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
		log.info("Procesando registro: {}", record);

		String nombreOrganismo = record.path("nombre_del_organismo").asText("");
		Long id = Parsear.parseLong(record.path("identificador").asText(""));
		String calle = record.path("calle").asText("");
		String localidad = record.path("localidad").asText("");
		Double latitud = Parsear.parseDouble(record.path("latitud").asText());
		Double longitud =Parsear.parseDouble(record.path("longitud").asText());

		// Obtener categoría desde el servicio
		CategoriaDTO categoria = categoriaServicio.obtenerCategoriaPorNombre(nombreOrganismo);

		log.info("Categoría asignada: {}", categoria.getNombreCategoria());

		return new PuntoDeInteresDTO(
				id,
				nombreOrganismo,
				calle,
				localidad,
				latitud,
				longitud,
				categoria
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

	        log.info("Solicitando punto de interés con URL: {}", url);
	        String response = restTemplate.getForObject(url, String.class);

	        log.info("Respuesta de la API: {}", response);

	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (results.isArray() && results.size() > 0) {
	            JsonNode record = results.get(0); // Solo el primer resultado

	            log.info("Datos del registro: {}", record);

	            // Extraer campos directamente del nodo "record"
	            Long identificador = Parsear.parseLong(record.path("identificador").asText(""));
	            String nombreOrganismo = record.path("nombre_del_organismo").asText("");
	            String calle = record.path("calle").asText("");
	            String localidad = record.path("localidad").asText("");
	            Double latitud = Parsear.parseDouble(record.path("latitud").asText(""));
	            Double longitud = Parsear.parseDouble(record.path("longitud").asText(""));

	            CategoriaDTO categoria = categoriaServicio.obtenerCategoriaPorNombre(nombreOrganismo);
	            log.info("Contenido del nodo record: {}", record.toPrettyString());
	            log.info("Datos extraídos - ID: {}, Nombre: {}, Calle: {}, Latitud: {}, Longitud: {}", 
	                    identificador, nombreOrganismo, calle, latitud, longitud);

	            // Crear y retornar el DTO con la categoría asignada
	            return new PuntoDeInteresDTO(
	                    identificador,
	                    nombreOrganismo,
	                    calle,
	                    localidad,
	                    latitud,
	                    longitud,
	                    categoria
	            );
	        } else {
	            log.warn("No se encontraron resultados para el ID: {}", id);
	            return null;
	        }
	    } catch (Exception e) {
	        log.error("Error al obtener punto de interés con ID {}: {}", id, e.getMessage());
	        return null;
	    }
	}
}