package proyecto.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import proyecto.modelo.dao.PuntoDeInteresDAO;
import proyecto.modelo.dto.CategoriaDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.utiles.Parsear;

/**
 * Servicio para gestionar la lógica relacionada con los puntos de interés
 * 
 * @author Paula Ruano
 */
@Service
public class PuntoDeInteresServicio {
	// Variable local de la clase
	private static final String API_URL = "https://analisis.datosabiertos.jcyl.es/api/explore/v2.1/catalog/datasets/registro-de-infraestructuras-culturales/records";

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final CategoriaServicio categoriaServicio;
	private final PuntoDeInteresDAO puntoDeInteresDAO;

	// Constructor de la clase
	public PuntoDeInteresServicio(RestTemplate restTemplate, ObjectMapper objectMapper, CategoriaServicio categoriaServicio, PuntoDeInteresDAO puntoDeInteresDAO) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.categoriaServicio = categoriaServicio;
		this.puntoDeInteresDAO = puntoDeInteresDAO;
	}

	 /**
     * Mapea los campos de un registro JSON a un objeto PuntoDeInteresDTO
     * 
     * Este método se utiliza en el método obtenerPuntoPorIdDesdeAPI de esta misma clase
     * 
     * @param record Nodo JSON con los datos del registro
     * @return PuntoDeInteresDTO con los datos mapeados
     */
	private PuntoDeInteresDTO mapearCampos(JsonNode record) {

		String nombreOrganismo = record.path("nombre_del_organismo").asText("");
		Long id = Parsear.parseLong(record.path("identificador").asText(""));
		String calle = record.path("calle").asText("");
		String localidad = record.path("localidad").asText("");
		Double latitud = Parsear.parseDouble(record.path("latitud").asText());
		Double longitud =Parsear.parseDouble(record.path("longitud").asText());

		// Obtener la categoría según el nombre del organismo
		CategoriaDTO categoria = categoriaServicio.obtenerCategoriaPorNombre(nombreOrganismo);	

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
	
	/**
	 * Obtiene un punto de interés específico desde la API externa utilizando su ID
	 * 
	 * Este método se utiliza en el método obtenerOCrearPuntosDeInteres de esta misma clase
	 * 
	 * @param id ID del punto de interés a buscar
	 * @return PuntoDeInteresDTO con los datos obtenidos de la API, o null si no se encuentra
	 * @throws RuntimeException Si ocurre un error durante la solicitud a la API
	 */
	private PuntoDeInteresDTO obtenerPuntoPorIdDesdeAPI(Long id) {
		try {
			// Construir la URL con el filtro por ID
			String url = UriComponentsBuilder.fromHttpUrl(API_URL)
					.queryParam("where", "identificador=" + id) // Filtrar por identificador
					.build()
					.toUriString();
			
			// Realizar la solicitud a la API
			String response = restTemplate.getForObject(url, String.class);
			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode results = rootNode.path("results");
			
			// Verificar si se encontraron resultados y mapear el primer registro
			if (results.isArray() && results.size() > 0) {
				JsonNode record = results.get(0);
				return mapearCampos(record);
			}
		} catch (Exception e) {
			// Capturar y propagar cualquier error
			throw new RuntimeException("Error al obtener el punto con ID " + id + " desde la API: " + e.getMessage());
		}
		// Retornar null si no se encuentra el punto
		return null;
	}	

	/**
	 * Obtiene puntos de interés desde la base de datos o los crea si no existen, 
	 * obteniéndolos desde la API externa
	 * 
	 * @param ids Lista de IDs de puntos de interés
	 * @return Lista de puntos de interés procesados
	 * 
	 * @see CrearRutaPredeterminadaControlador
	 * @see ConfirmarRutaUsuarioControlador
	 */
	public List<PuntoDeInteresDTO> obtenerOCrearPuntosDeInteres(List<Long> ids) {
		List<PuntoDeInteresDTO> puntosProcesados = new ArrayList<>();

		for (Long id : ids) {
			try {
				// Buscar en la base de datos
				PuntoDeInteresDTO existente = puntoDeInteresDAO.findById(id).orElse(null);

				if (existente != null) {
					 // Si existe, añadir a la lista de resultados
					puntosProcesados.add(existente);
				} else {
					// Si no existe en la base de datos, buscar en la API
					PuntoDeInteresDTO nuevoPunto = obtenerPuntoPorIdDesdeAPI(id);
					if (nuevoPunto != null) {
						// Guardar el nuevo punto en la base de datos
						puntoDeInteresDAO.save(nuevoPunto); 
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
	
	/**
	 * Obtiene un punto de interés específico desde la API externa utilizando su ID
	 * 
	 * Realiza una solicitud HTTP a la API con el identificador como parámetro y mapea 
	 * los campos obtenidos en un objeto `PuntoDeInteresDTO`. Si no se encuentra el punto 
	 * o si ocurre un error, retorna null
	 * 
	 * Este método se utiliza en el método obtenerPuntosPorIds de esta misma clase
	 * 
	 * @param id ID del punto de interés a buscar
	 * @return PuntoDeInteresDTO con los datos obtenidos de la API, o null si no existe
	 */
	public PuntoDeInteresDTO obtenerPuntoPorId(Long id) {
		try {
			// Construir la URL con el filtro por identificador
			String url = UriComponentsBuilder.fromHttpUrl(API_URL)
					.queryParam("where", "identificador=" + id) // Filtrar por ID en la API
					.build()
					.toUriString();
			
			// Realizar la solicitud GET a la API
			String response = restTemplate.getForObject(url, String.class);

			// Parsear la respuesta JSON
			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode results = rootNode.path("results");

	        // Verificar si se encontraron resultados
			if (results.isArray() && results.size() > 0) {
				JsonNode record = results.get(0); // Obtener el primer registro del array

				// Extraer campos directamente del nodo "record"
				Long identificador = Parsear.parseLong(record.path("identificador").asText(""));
				String nombreOrganismo = record.path("nombre_del_organismo").asText("");
				String calle = record.path("calle").asText("");
				String localidad = record.path("localidad").asText("");
				Double latitud = Parsear.parseDouble(record.path("latitud").asText(""));
				Double longitud = Parsear.parseDouble(record.path("longitud").asText(""));

				// Obtener la categoría desde el servicio de categorías
				CategoriaDTO categoria = categoriaServicio.obtenerCategoriaPorNombre(nombreOrganismo);

				 // Crear y retornar el objeto PuntoDeInteresDTO
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

				 // Retornar null si no se encontraron resultados
				return null;
			}
		} catch (Exception e) {
			 // Capturar cualquier error durante la ejecución y retornar null
			return null;
		}
	}

	/**
	 * Obtiene una lista de puntos de interés desde la API externa usando una lista de IDs
	 * 
	 * @param ids Lista de IDs de puntos de interés a buscar
	 * @return Lista de puntos de interés encontrados
	 * 
	 * @see ConfirmarRutaUsuarioControlador
	 */
	public List<PuntoDeInteresDTO> obtenerPuntosPorIds(List<Long> ids) {
		List<PuntoDeInteresDTO> puntosFiltrados = new ArrayList<>();
		try {
			for (Long id : ids) {
				try {
					// Obtener punto de interés por ID desde la API
					PuntoDeInteresDTO punto = obtenerPuntoPorId(id);
					if (punto != null) {
						puntosFiltrados.add(punto);
					}
				} catch (Exception e) {
					 throw e;
				}
			}
		} catch (Exception e) {
			// Propagar una excepción si ocurre un error 
			throw e;
		}
		return puntosFiltrados;
	}

    /**
     * Obtiene puntos de interés desde la base de datos por una lista de IDs
     * 
     * @param ids Lista de IDs de puntos de interés
     * @return Lista de puntos de interés filtrados
     * @throws IllegalArgumentException Si un punto de interés no existe en la base de datos
     * 
     * @see DetalleRutaControlador
     * @see HacerRuta Controlador
     * @see RutaPredeterminadaControlador
     */
	public List<PuntoDeInteresDTO> obtenerPuntosPorIdsDesdeBBDD(List<Long> ids) {
		List<PuntoDeInteresDTO> puntosFiltrados = new ArrayList<>();
		 // Buscar cada punto por su ID en la base de datos
		for (Long id : ids) {
			PuntoDeInteresDTO punto = puntoDeInteresDAO.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("El punto de interés con ID " + id + " no existe en la base de datos."));
			puntosFiltrados.add(punto);
		}
		
		return puntosFiltrados;
	}

	/**
	 * Obtiene puntos de interés desde la API externa de manera paginada
	 * 
	 * @param page Número de la página
	 * @param pageSize Tamaño de la página (cantidad de registros)
	 * @return Lista de puntos de interés obtenidos desde la API
	 * 
	 * @see RutaUsuarioControlador
	 */
	public List<PuntoDeInteresDTO> obtenerPuntosDeInteresPaginados(int page, int pageSize) {
		try {
			 // Construir la URL con paginación
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
			int contadorExcluidos = 0; // Contador de puntos excluidos
			for (JsonNode record : results) {
				  // Filtrar registros con calle válida
				if (!record.path("calle").asText("").isEmpty()) {
					puntos.add(mapearCampos(record));
				} else {
					contadorExcluidos++;
				}
			}
			return puntos;
		} catch (Exception e) {
			throw new RuntimeException("Error al obtener puntos paginados", e);
		}
	}

	/**
	 * Obtiene una lista de municipios cuyos nombres comienzan con un prefijo específico
	 * 
	 * Realiza una búsqueda paginada en la API externa y filtra los resultados según el prefijo
	 * 
	 * @param prefijo Prefijo con el que deben comenzar los nombres de los municipios
	 * @return Lista de nombres de municipios ordenada alfabéticamente y sin duplicados
	 * @throws RuntimeException Si ocurre un error durante la solicitud a la API
	 * 
	 * @see PuntoDeInteresControlador
	 */
	public List<String> obtenerMunicipiosPorPrefijo(String prefijo) {
		try {	       

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
					break; // Finaliza si no hay más resultados
				}

				for (JsonNode record : results) {
					String localidad = record.path("localidad").asText("");	                
					if (!localidad.isEmpty() && localidad.toLowerCase().startsWith(prefijo.trim().toLowerCase())) {
						municipios.add(localidad);
					}
				}

				offset += limit; // Incrementa el offset para la siguiente página
				hayMasDatos = results.size() == limit; // Si se devolvieron menos de 'limit', no hay más datos
			}

			return new ArrayList<>(municipios);

		} catch (Exception e) {	        
			throw new RuntimeException("Error al obtener municipios por prefijo", e);
		}
	}
	
	/**
	 * Obtiene puntos de interés filtrados por municipio de manera paginada.
	 * 
	 * @param municipio Municipio por el que se filtrarán los puntos de interés
	 * @param page Número de la página
	 * @param pageSize Tamaño de la página (número de registros por página)
	 * @return Lista de puntos de interés obtenidos desde la API externa
	 * @throws RuntimeException Si ocurre un error durante la solicitud a la API
	 * 
	 * @see PuntoDeInteresControlador
	 */
	public List<PuntoDeInteresDTO> obtenerPuntosDeInteresPorMunicipioPaginados(String municipio, int page, int pageSize) {
		try {
			// Construir la URL con el filtro por municipio y paginación
			String url = UriComponentsBuilder.fromHttpUrl(API_URL)
					.queryParam("where", "localidad='" + municipio + "'")
					.queryParam("offset", page * pageSize)
					.queryParam("limit", pageSize)
					.build()
					.toUriString();	 
			
			// Realizar la solicitud a la API
			String response = restTemplate.getForObject(url, String.class);
			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode results = rootNode.path("results");

			if (!results.isArray()) {
				// Retornar lista vacía si no hay resultados
				return Collections.emptyList();
			}

			List<PuntoDeInteresDTO> puntos = new ArrayList<>();
			int contadorExcluidos = 0; // Contador de puntos excluidos
			for (JsonNode record : results) {
				 // Filtrar puntos con calle válida
				if (!record.path("calle").asText("").isEmpty()) {
					puntos.add(mapearCampos(record));
				} else {
					contadorExcluidos++;
				}
			}

			return puntos;
		} catch (Exception e) {	        
			throw new RuntimeException("Error al obtener puntos paginados por municipio", e);
		}
	}	
}