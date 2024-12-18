package proyecto.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.RutaPuntoInteresDTO;

/**
 * Servicio para gestionar la lógica relacionada con las rutas predeterminadas
 * 
 * @author Paula Ruano
 */
@Service
public class RutaPredeterminadaServicio {
	// Variables locales de la clase
	private final RutaPredeterminadaDAO rutaPredeterminadaDAO;
	private final RestTemplate restTemplate;
	private final RutaPuntoInteresDAO rutaPuntoInteresDAO;

	@Value("${google.maps.api.key}") // Clave de API de Google Maps
	private String googleMapsApiKey;

	// Constructor de la clase
	@Autowired
	public RutaPredeterminadaServicio(RutaPredeterminadaDAO rutaPredeterminadaDAO, RestTemplate restTemplate,RutaPuntoInteresDAO rutaPuntoInteresDAO) {
		this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;		
		this.restTemplate = restTemplate;
		this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
	}
	
	/**
     * Crea una ruta predeterminada y asocia los puntos de interés a la ruta
     * 
     * @param nombre Nombre de la ruta
     * @param puntosValidos Lista de puntos de interés válidos
     * @param duracionTotal Duración total de la ruta en minutos
     * @param distanciaTotal Distancia total de la ruta en metros
     * @param municipio Municipio al que pertenece la ruta
     * 
     * @see CrearRutaPredeterminadaControlador
     */
	@Transactional
	public void crearRutaPredeterminada(String nombre, List<PuntoDeInteresDTO> puntosValidos, double duracionTotal, int distanciaTotal, String municipio) {
		if (puntosValidos == null || puntosValidos.isEmpty()) {
			throw new IllegalArgumentException("La lista de puntos de interés no puede estar vacía.");
		}

		// Crear la ruta principal
		RutaPredeterminadaDTO rutaPrincipal = new RutaPredeterminadaDTO();
		rutaPrincipal.setNombre(nombre);
		rutaPrincipal.setDuracion(duracionTotal);
		rutaPrincipal.setDistancia(distanciaTotal);
		rutaPrincipal.setMunicipio(municipio);
		rutaPrincipal.setTipo("P"); // Tipo "P" para predeterminada

		// Guardar la ruta en la base de datos
		RutaPredeterminadaDTO rutaGuardada = rutaPredeterminadaDAO.save(rutaPrincipal);

		// Asociar los puntos de interés con la ruta creada, creando los registros en la tabla intermedia
		for (PuntoDeInteresDTO punto : puntosValidos) {
			RutaPuntoInteresDTO rutaPuntoInteres = new RutaPuntoInteresDTO();
			rutaPuntoInteres.setRuta(rutaGuardada); // Asociar la ruta creada
			rutaPuntoInteres.setPuntoInteres(punto); // Asociar el punto de interés
			rutaPuntoInteresDAO.save(rutaPuntoInteres); // Guardar en la tabla intermedia
		}
	}

    /**
     * Obtiene todas las rutas predeterminadas almacenadas en la base de datos
     * 
     * @return Lista de rutas predeterminadas
     * 
     * @see RutaPredeterminadaControlador
     */
	public List<RutaPredeterminadaDTO> obtenerTodasLasRutas() {
		return rutaPredeterminadaDAO.findAll(); 
	}
	
	 /**
     * Busca una ruta predeterminada por su ID
     * 
     * @param id Identificador de la ruta
     * @return Optional que contiene la ruta si existe
     * 
     * @see DetalleRutaControlador
     * @see HacerRutaControlador
     */
	public Optional<RutaPredeterminadaDTO> obtenerRutaPorId(Integer id) {
		return rutaPredeterminadaDAO.findById(id);
	}
	
	/**
     * Calcula la distancia total y la duración total de una ruta utilizando la API de Google Maps
     * 
     * @param puntos Lista de puntos de interés que conforman la ruta
     * @return Mapa con las claves "distance" y "duration" 
     * @throws JsonProcessingException Si ocurre un error al procesar la respuesta JSON
     * 
     * @see CrearRutaPredeterminada
     */
	public Map<String, Object> calcularDistanciaYDuracion(List<PuntoDeInteresDTO> puntos) throws JsonProcessingException {
		// Verificar que la lista de puntos de interés no sea nula ni esté vacía
		if (puntos == null || puntos.isEmpty()) {
			throw new IllegalArgumentException("La lista de puntos de interés no puede estar vacía.");
		}
		
		// Generar una lista de coordenadas (latitud, longitud)
		List<String> coordenadas = puntos.stream()
				.map(p -> p.getLatitud() + "," + p.getLongitud())
				.collect(Collectors.toList());
		
		// Configurar origen, destino y waypoints para la API de Google Maps
		String origen = coordenadas.get(0);
		String destino = coordenadas.get(coordenadas.size() - 1);
		String waypoints = String.join("|", coordenadas.subList(1, coordenadas.size() - 1));
		
		// URL para la API de Google Maps
		String url = String.format(
				"https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&waypoints=%s&key=%s&mode=walking",
				origen, destino, waypoints, googleMapsApiKey
				);
		
		// Realizar solicitud a la API
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("Error al obtener datos de Google Maps: " + response.getStatusCode());
		}
		// Procesar la respuesta JSON
		JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
		JsonNode legs = rootNode.path("routes").path(0).path("legs");
		
		// Calcular la distancia y duración totales
		int totalDistance = 0;
		int totalDuration = 0;

		for (JsonNode leg : legs) {
			totalDistance += leg.path("distance").path("value").asInt(); // Distancia en metros
			totalDuration += leg.path("duration").path("value").asInt(); // Duración en segundos
		}

		// Devolver los resultados en un mapa
		Map<String, Object> result = new HashMap<>();
		result.put("distance", totalDistance);
		result.put("duration", totalDuration);

		return result;
	}

    /**
     * Obtiene los IDs de los puntos de interés asociados a una ruta específica
     * 
     * @param rutaId Identificador de la ruta
     * @return Lista de IDs de los puntos de interés asociados a la ruta
     * 
     * @see DetalleRutaControlador
     * @see HacerRutaControlador
     * @see RutaPredeterminadaControlador
     */
	public List<Long> obtenerIdsPuntosDeRuta(Integer rutaId) {
		return rutaPuntoInteresDAO.findPuntosInteresByRutaId(rutaId)
				.stream()
				.map(PuntoDeInteresDTO::getId)
				.collect(Collectors.toList());
	}
}