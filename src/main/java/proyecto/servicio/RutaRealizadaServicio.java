package proyecto.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.RutaRealizadaDAO;
import proyecto.modelo.dto.RutaRealizadaDTO;

/**
 * Servicio para gestionar la lógica relacionada con las rutas realizadas por los usuarios
 * 
 * @author Paula Ruano
 */
@Service
public class RutaRealizadaServicio {
	// Variables locales de la clase
	private final RutaRealizadaDAO rutaRealizadaDAO;	
	
	// Constructor de la clase
	@Autowired
	public RutaRealizadaServicio(RutaRealizadaDAO rutaRealizadaDAO) {
		this.rutaRealizadaDAO = rutaRealizadaDAO;	
	}

	 /**
     * Crea y guarda una nueva ruta realizada en la base de datos
     * 
     * @param rutaRealizada El objeto RutaRealizadaDTO a guardar
     * 
     * @see RutaRealizadaControlador
     */
	@Transactional
	public void crearRutaRealizada(RutaRealizadaDTO rutaRealizada) {
		 // Guardar la ruta realizada en la base de datos
		rutaRealizadaDAO.save(rutaRealizada);
	}
	
	 /**
     * Obtiene una lista de rutas realizadas por un usuario específico
     * 
     * @param usuarioId ID del usuario para filtrar las rutas realizadas
     * @return Lista de rutas realizadas asociadas al usuario
     * 
     * @see RutaRealizadaControlador
     */	
	public List<RutaRealizadaDTO> obtenerRutasRealizadasPorUsuario(int usuarioId) {
		// Filtrar las rutas realizadas por el ID del usuario
		return rutaRealizadaDAO.findAll().stream()
				.filter(ruta -> ruta.getUsuario().getId() == usuarioId)
				.toList();
	}
}