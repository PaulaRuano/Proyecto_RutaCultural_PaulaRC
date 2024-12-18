package proyecto.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dao.RutaUsuarioDAO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Servicio para gestionar la lógica relacionada con las rutas creadas por los usuarios
 * 
 * @author Paula Ruano
 */
@Service
public class RutaUsuarioServicio {
	// Variables locales de la clase
	private final RutaUsuarioDAO rutaUsuarioDAO;
	private final RutaPuntoInteresDAO rutaPuntoInteresDAO;

	// Constructor de la clase
	@Autowired
	public RutaUsuarioServicio(RutaUsuarioDAO rutaUsuarioDAO,  RutaPuntoInteresDAO rutaPuntoInteresDAO) {
		this.rutaUsuarioDAO = rutaUsuarioDAO;
		this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
	}

	/**
	 * Crea una nueva ruta de usuario y la guarda en la base de datos
	 * 
	 * @param nombre Nombre de la ruta
	 * @param duracion Duración estimada de la ruta en minutos
	 * @param distancia Distancia total de la ruta en metros
	 * @param municipio Municipio al que pertenece la ruta
	 * @param usuario Usuario que crea la ruta
	 * @return RutaUsuarioDTO La ruta creada y guardada
	 * 
	 * @see ConfirmarRutaUsuarioControlador
	 */
	@Transactional
	public RutaUsuarioDTO crearRutaUsuario(String nombre, Double duracion, int distancia, 
			String municipio, UsuarioDTO usuario) {
		// Crear una nueva instancia de RutaUsuarioDTO con los datos proporcionados
		RutaUsuarioDTO nuevaRuta = new RutaUsuarioDTO(nombre, duracion, distancia, municipio, usuario);

		// Asignar la fecha actual como fecha de creación
		nuevaRuta.setFechaCreacion(LocalDate.now());

		// Guardar la ruta en la base de datos y retornar el objeto guardado
		return rutaUsuarioDAO.save(nuevaRuta); 
	}

	/**
	 * Obtiene todas las rutas creadas por un usuario específico
	 * 
	 * @param usuario Usuario que creó las rutas
	 * @return Lista de rutas creadas por el usuario
	 * 
	 * @see MiRutaControlador
	 */
	public List<RutaUsuarioDTO> obtenerRutasPorUsuario(UsuarioDTO usuario) {
		// Llamar al DAO para obtener las rutas asociadas al usuario
		return rutaUsuarioDAO.findByUsuario(usuario);
	}

	/**
	 * Obtiene los IDs de los puntos de interés asociados a una ruta específica
	 * 
	 * @param rutaId ID de la ruta
	 * @return Lista de IDs de puntos de interés asociados a la ruta
	 * 
	 * @see DetalleRutaControlador
	 * @see HacerRutaControlador
	 * @see MiRutaControlador
	 */
	public List<Long> obtenerIdsPuntosDeRuta(Integer rutaId) {
		// Obtener los puntos de interés asociados a la ruta y extraer sus IDs
		return rutaPuntoInteresDAO.findPuntosInteresByRutaId(rutaId)
				.stream()
				.map(PuntoDeInteresDTO::getId)
				.collect(Collectors.toList());
	}

	/**
	 * Busca una ruta de usuario por su ID
	 * 
	 * @param id ID de la ruta
	 * @return Optional que contiene la ruta si existe
	 * 
	 * @see DetalleRutaControlador
	 * @see HacerRutaControlador
	 */
	public Optional<RutaUsuarioDTO> obtenerRutaPorId(Integer id) {
		// Buscar y devolver la ruta por su ID
		return rutaUsuarioDAO.findById(id);
	}

	/**
	 * Elimina una ruta de usuario por su ID, junto con sus relaciones en la tabla intermedia
	 * 
	 * @param id ID de la ruta a eliminar
	 * @throws RuntimeException Si la ruta no existe
	 * 
	 * @see MiRutaControlador
	 */
	@Transactional
	public void eliminarRutaUsuario(int id) {
		// Verificar si la ruta existe
		RutaUsuarioDTO ruta = rutaUsuarioDAO.findById(id)
				.orElseThrow(() -> new RuntimeException("La ruta con ID " + id + " no existe."));

		// Eliminar las relaciones en la tabla intermedia entre ruta y puntos de interés
		rutaPuntoInteresDAO.deleteByRutaId(id);

		// Eliminar la ruta de la base de datos
		rutaUsuarioDAO.deleteById(id);
	}   
}