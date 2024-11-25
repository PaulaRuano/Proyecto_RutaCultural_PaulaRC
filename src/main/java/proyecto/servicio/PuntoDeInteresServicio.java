package proyecto.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import proyecto.modelo.dao.PuntoDeInteresDAO;
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
	private final PuntoDeInteresDAO puntoDeInteresDAO;

	public PuntoDeInteresServicio(RestTemplate restTemplate, ObjectMapper objectMapper, CategoriaServicio categoriaServicio, PuntoDeInteresDAO puntoDeInteresDAO) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.categoriaServicio = categoriaServicio;
		this.puntoDeInteresDAO = puntoDeInteresDAO;
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
	
	public PuntoDeInteresDTO guardarPuntoDeInteres(PuntoDeInteresDTO punto) {
		return puntoDeInteresDAO.save(punto); // Persistir en la base de datos
	}

	public List<PuntoDeInteresDTO> obtenerPuntosPorIdsDesdeBBDD(List<Long> ids) {
	    List<PuntoDeInteresDTO> puntosFiltrados = new ArrayList<>();
	    for (Long id : ids) {
	        PuntoDeInteresDTO punto = puntoDeInteresDAO.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("El punto de interés con ID " + id + " no existe en la base de datos."));
	        puntosFiltrados.add(punto);
	    }
	    return puntosFiltrados;
	}

	public List<PuntoDeInteresDTO> obtenerOCrearPuntosDeInteres(List<Long> ids) {
	    List<PuntoDeInteresDTO> puntosProcesados = new ArrayList<>();

	    for (Long id : ids) {
	        try {
	            PuntoDeInteresDTO existente = puntoDeInteresDAO.findById(id)
	                .orElse(null);

	            if (existente != null) {
	                puntosProcesados.add(existente);
	            } else {
	                // Si no existe en la base de datos, buscar en la API
	                PuntoDeInteresDTO nuevoPunto = obtenerPuntoPorIdDesdeAPI(id);
	                if (nuevoPunto != null) {
	                    puntoDeInteresDAO.save(nuevoPunto); // Guardar en la base de datos
	                    puntosProcesados.add(nuevoPunto);
	                } else {
	                    throw new IllegalArgumentException("El punto de interés con ID " + id + " no existe ni en la API.");
	                }
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Error al procesar el punto de interés con ID " + id + ": " + e.getMessage());
	        }
	    }
	    return puntosProcesados;
	}

	private PuntoDeInteresDTO obtenerPuntoPorIdDesdeAPI(Long id) {
	    try {
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	            .queryParam("where", "identificador=" + id) // Ajusta según la API
	            .build()
	            .toUriString();

	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (results.isArray() && results.size() > 0) {
	            JsonNode record = results.get(0);
	            return mapearCampos(record);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error al obtener el punto con ID " + id + " desde la API: " + e.getMessage());
	    }
	    return null;
	}


	
	public List<PuntoDeInteresDTO> obtenerPuntosDeInteresPaginados(int page, int pageSize) {
	    try {
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                .queryParam("offset", page * pageSize)
	                .queryParam("limit", pageSize)
	                .build()
	                .toUriString();

	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (!results.isArray()) {
	            return Collections.emptyList();
	        }

	        List<PuntoDeInteresDTO> puntos = new ArrayList<>();
	        int excludedCount = 0; // Contador de puntos excluidos
	        for (JsonNode record : results) {
	            if (!record.path("calle").asText("").isEmpty()) {
	                puntos.add(mapearCampos(record));
	            } else {
	                excludedCount++;
	            }
	        }

	        log.debug("Puntos incluidos: {}", puntos.size());
	        log.debug("Puntos excluidos (sin calle): {}", excludedCount);
	        return puntos;
	    } catch (Exception e) {
	        throw new RuntimeException("Error al obtener puntos paginados", e);
	    }
	}
	
	public List<PuntoDeInteresDTO> obtenerPuntosPorMunicipio(String municipio, int page, int pageSize) {
	    try {
	        int offset = page * pageSize; // Calcula el desplazamiento
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                .queryParam("where", "localidad='" + municipio + "'") // Filtro por municipio
	                .queryParam("offset", offset) // Paginación
	                .queryParam("limit", pageSize) // Límite de resultados
	                .build()
	                .toUriString();

	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (!results.isArray() || results.isEmpty()) {
	            return Collections.emptyList();
	        }

	        List<PuntoDeInteresDTO> puntos = new ArrayList<>();
	        for (JsonNode record : results) {
	            if (!record.path("calle").asText("").isEmpty()) { // Filtrar por puntos con calle válida
	                puntos.add(mapearCampos(record));
	            }
	        }
	        return puntos;
	    } catch (Exception e) {
	        throw new RuntimeException("Error al obtener puntos por municipio", e);
	    }
	}
	
	public List<String> obtenerMunicipiosPorPrefijo(String prefijo) {
	    try {
	        log.info("Buscando municipios con prefijo: '{}'", prefijo);

	        Set<String> municipios = new TreeSet<>(); // Evitar duplicados
	        int offset = 0; // Desplazamiento inicial
	        int limit = 100; // Límite de resultados por página
	        boolean hayMasDatos = true;

	        while (hayMasDatos) {
	            String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                    .queryParam("limit", limit)
	                    .queryParam("offset", offset) // Paginación
	                    .build()
	                    .toUriString();

	            String response = restTemplate.getForObject(url, String.class);
	            JsonNode rootNode = objectMapper.readTree(response);
	            JsonNode results = rootNode.path("results");

	            if (!results.isArray() || results.isEmpty()) {
	                log.warn("No hay más resultados en la página actual.");
	                break; // Finaliza si no hay más resultados
	            }

	            for (JsonNode record : results) {
	                String localidad = record.path("localidad").asText("");
	                log.info("Municipio encontrado en datos sin filtrar: '{}'", localidad);
	                if (!localidad.isEmpty() && localidad.toLowerCase().startsWith(prefijo.trim().toLowerCase())) {
	                    municipios.add(localidad);
	                }
	            }

	            offset += limit; // Incrementa el offset para la siguiente página
	            hayMasDatos = results.size() == limit; // Si se devolvieron menos de 'limit', no hay más datos
	        }

	        log.info("Municipios filtrados: {}", municipios);
	        return new ArrayList<>(municipios);

	    } catch (Exception e) {
	        log.error("Error al obtener municipios por prefijo: ", e);
	        throw new RuntimeException("Error al obtener municipios por prefijo", e);
	    }
	}
	
	public List<PuntoDeInteresDTO> obtenerPuntosDeInteresPorMunicipioPaginados(String municipio, int page, int pageSize) {
	    try {
	        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
	                .queryParam("where", "localidad='" + municipio + "'")
	                .queryParam("offset", page * pageSize)
	                .queryParam("limit", pageSize)
	                .build()
	                .toUriString();

	        log.info("URL generada para paginación por municipio: {}", url);

	        String response = restTemplate.getForObject(url, String.class);
	        JsonNode rootNode = objectMapper.readTree(response);
	        JsonNode results = rootNode.path("results");

	        if (!results.isArray()) {
	            return Collections.emptyList();
	        }

	        List<PuntoDeInteresDTO> puntos = new ArrayList<>();
	        int excludedCount = 0; // Contador de puntos excluidos
	        for (JsonNode record : results) {
	            if (!record.path("calle").asText("").isEmpty()) {
	                puntos.add(mapearCampos(record));
	            } else {
	                excludedCount++;
	            }
	        }

	        log.debug("Puntos incluidos: {}", puntos.size());
	        log.debug("Puntos excluidos (sin calle): {}", excludedCount);
	        return puntos;
	    } catch (Exception e) {
	        log.error("Error al obtener puntos paginados por municipio: ", e);
	        throw new RuntimeException("Error al obtener puntos paginados por municipio", e);
	    }
	}


}