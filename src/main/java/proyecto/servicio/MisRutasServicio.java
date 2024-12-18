package proyecto.servicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.MiRutaDAO;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dto.MiRutaDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Servicio para gestionar la lógica relacionada con las rutas guardadas por los usuarios (Mis Rutas)
 * 
 * @author Paula Ruano
 */
@Service
public class MisRutasServicio {
	// Variables locales de la clase
	private final MiRutaDAO misRutasDAO;
	private final RutaPredeterminadaDAO rutaPredeterminadaDAO;
	private final RutaPuntoInteresDAO rutaPuntoInteresDAO;

	// Constructor de la clase
	@Autowired
	public MisRutasServicio(MiRutaDAO misRutasDAO, RutaPredeterminadaDAO rutaPredeterminadaDAO, RutaPuntoInteresDAO rutaPuntoInteresDAO) {
		this.misRutasDAO = misRutasDAO;
		this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
		this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
	}

	/**
	 * Guarda una ruta predeterminada en la lista "Mis Rutas" de un usuario
	 * 
	 * @param rutaId ID de la ruta predeterminada a guardar
	 * @param usuario Usuario que guarda la ruta
	 * @throws IllegalArgumentException Si la ruta predeterminada no existe
	 * @throws IllegalStateException Si la ruta ya está guardada en "Mis Rutas"
	 * 
	 * @see MiRutaControlador
	 */
	@Transactional
	public void guardarMisRutas(Integer rutaId, UsuarioDTO usuario) {
		// Verificar que la ruta predeterminada existe
		RutaPredeterminadaDTO ruta = rutaPredeterminadaDAO.findById(rutaId)
				.orElseThrow(() -> new IllegalArgumentException("La ruta predeterminada no existe."));

		// Verificar si ya está en Mis Rutas del usuario
		if (misRutasDAO.existsByRutaAndUsuario(ruta, usuario)) {
			throw new IllegalStateException("Esta ruta ya está guardada en 'Mis Rutas'.");
		}

		// Crear una nueva relación en Mis Rutas
		MiRutaDTO misRutas = new MiRutaDTO();
		misRutas.setRuta(ruta); // Asignar la ruta predeterminada
		misRutas.setUsuario(usuario); // Asignar el usuario
		misRutas.setFecha(LocalDate.now()); // Asignar la fecha actual como fecha de adición

		// Guardar en la base de datos
		misRutasDAO.save(misRutas);
	}

	/**
	 * Obtiene las rutas predeterminadas guardadas por un usuario, con detalles adicionales
	 * 
	 * Los detalles adicionales incluyen:
	 * - Fecha en la que se añadió la ruta
	 * - Imagen del primer punto de la ruta (si existe) o una imagen por defecto
	 * 
	 * @param usuario Usuario del que se obtendrán las rutas guardadas
	 * @return Lista de mapas con información de las rutas y sus detalles
	 * 
	 * @see MiRutaControlador
	 */
	public List<Map<String, Object>> obtenerRutasPredeterminadasConDetalles(UsuarioDTO usuario) {
		// Obtener las rutas predeterminadas guardadas por el usuario
		List<RutaPredeterminadaDTO> rutas = misRutasDAO.findRutasPredeterminadasByUsuario(usuario);

		// Añadir cada ruta con detalles adicionales
		return rutas.stream().map(ruta -> {
			Map<String, Object> rutaMap = new HashMap<>();
			// Agregar la información de la ruta
			rutaMap.put("ruta", ruta);

			// Obtener la fecha en que se añadió la ruta a Mis Rutas
			LocalDate fechaAdicion = misRutasDAO
					.findByUsuarioAndRuta(usuario, ruta)
					.map(MiRutaDTO::getFecha)
					.orElse(null);
			rutaMap.put("fecha", fechaAdicion);

			// Obtener la imagen asociada al primer punto de interés de la ruta
			List<Long> idsPuntos = obtenerIdsPuntosDeRuta(ruta.getId());
			if (!idsPuntos.isEmpty()) {
				rutaMap.put("imagen", "/img/puntosInteres/" + idsPuntos.get(0) + ".jpg");
			} else {
				rutaMap.put("imagen", "/img/ImgComunPi.png"); // Imagen por defecto
			}

			return rutaMap;
		}).toList();
	}

	/**
	 * Obtiene los IDs de los puntos de interés asociados a una ruta específica
	 * 
	 * @param rutaId ID de la ruta
	 * @return Lista de IDs de los puntos de interés asociados a la ruta
	 * 
	 * @see MisRutasServicio
	 */
	public List<Long> obtenerIdsPuntosDeRuta(Integer rutaId) {
		// Obtener puntos de interés asociados a la ruta y extraer sus IDs
		return rutaPuntoInteresDAO.findPuntosInteresByRutaId(rutaId)
				.stream()
				.map(PuntoDeInteresDTO::getId) // Obtener los IDs de los puntos
				.collect(Collectors.toList());
	}
}