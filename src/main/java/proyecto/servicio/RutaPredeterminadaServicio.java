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

@Service
public class RutaPredeterminadaServicio {

    private final RutaPredeterminadaDAO rutaPredeterminadaDAO;
    private final PuntoDeInteresServicio puntoDeInteresServicio;
    private final RestTemplate restTemplate;
    private final RutaPuntoInteresDAO rutaPuntoInteresDAO;
    
    @Value("${google.maps.api.key}") // Clave de API de Google Maps
    private String googleMapsApiKey;


    @Autowired
    public RutaPredeterminadaServicio(RutaPredeterminadaDAO rutaPredeterminadaDAO, PuntoDeInteresServicio puntoDeInteresServicio, RestTemplate restTemplate,RutaPuntoInteresDAO rutaPuntoInteresDAO) {
        this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
        this.puntoDeInteresServicio = puntoDeInteresServicio;
        this.restTemplate = restTemplate;
        this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
    }

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

        // Crear registros en la tabla intermedia para asociar los puntos de interés
        for (PuntoDeInteresDTO punto : puntosValidos) {
            RutaPuntoInteresDTO rutaPuntoInteres = new RutaPuntoInteresDTO();
            rutaPuntoInteres.setRuta(rutaGuardada); // Asociar la ruta creada
            rutaPuntoInteres.setPuntoInteres(punto); // Asociar el punto de interés
            rutaPuntoInteresDAO.save(rutaPuntoInteres); // Guardar en la tabla intermedia
        }
    }



    public List<RutaPredeterminadaDTO> obtenerTodasLasRutas() {
        return rutaPredeterminadaDAO.findAll(); // Obtener todas las rutas predeterminadas
    }
    
    public Optional<RutaPredeterminadaDTO> obtenerRutaPorId(Integer id) {
        return rutaPredeterminadaDAO.findById(id);
    }

    public Map<String, Object> calcularDistanciaYDuracion(List<PuntoDeInteresDTO> puntos) throws JsonProcessingException {
        if (puntos == null || puntos.isEmpty()) {
            throw new IllegalArgumentException("La lista de puntos de interés no puede estar vacía.");
        }

        List<String> coordenadas = puntos.stream()
            .map(p -> p.getLatitud() + "," + p.getLongitud())
            .collect(Collectors.toList());

        String origen = coordenadas.get(0);
        String destino = coordenadas.get(coordenadas.size() - 1);
        String waypoints = String.join("|", coordenadas.subList(1, coordenadas.size() - 1));

        String url = String.format(
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&waypoints=%s&key=%s&mode=walking",
            origen, destino, waypoints, googleMapsApiKey
        );

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener datos de Google Maps: " + response.getStatusCode());
        }

        JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
        JsonNode legs = rootNode.path("routes").path(0).path("legs");

        int totalDistance = 0;
        int totalDuration = 0;

        for (JsonNode leg : legs) {
            totalDistance += leg.path("distance").path("value").asInt(); // Distancia en metros
            totalDuration += leg.path("duration").path("value").asInt(); // Duración en segundos
        }

        Map<String, Object> result = new HashMap<>();
        result.put("distance", totalDistance);
        result.put("duration", totalDuration);

        return result;
    }

    public List<Long> obtenerIdsPuntosDeRuta(Integer rutaId) {
        return rutaPuntoInteresDAO.findPuntosInteresByRutaId(rutaId)
                                  .stream()
                                  .map(PuntoDeInteresDTO::getId)
                                  .collect(Collectors.toList());
    }

}
