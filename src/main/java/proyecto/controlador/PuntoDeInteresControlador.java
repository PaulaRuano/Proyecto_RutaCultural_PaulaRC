package proyecto.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.servicio.PuntoDeInteresServicio;

/**
 * Controlador REST para gestionar los puntos de interés
 * 
 * Proporciona endpoints para obtener información sobre puntos de interés y
 * municipios, con soporte para paginación y búsqueda por prefijos
 * 
 * @author Paula Ruano
 */
@RestController
@RequestMapping("/api/")  
public class PuntoDeInteresControlador {
	// Variable local
	@Autowired
	private final PuntoDeInteresServicio puntoDeInteresServicio;

	// Constructor de la clase
	@Autowired
	public PuntoDeInteresControlador(PuntoDeInteresServicio puntoDeInteresServicio) {
		this.puntoDeInteresServicio = puntoDeInteresServicio;
	}

	/**
	 * Obtiene una lista de puntos de interés por municipio con soporte para paginación
	 * 
	 * @param municipio Nombre del municipio para filtrar los puntos
	 * @param page Número de la página (opcional, por defecto 0)
	 * @param size Tamaño de la página (opcional, por defecto 10)
	 * @return ResponseEntity con la lista de puntos de interés o un error en caso de fallo
	 */
	@GetMapping("/puntos")
	public ResponseEntity<List<PuntoDeInteresDTO>> obtenerPuntosPorMunicipio(
			@RequestParam String municipio,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			// Obtener puntos de interés paginados por municipio
			List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio
					.obtenerPuntosDeInteresPorMunicipioPaginados(municipio, page, size);
			return ResponseEntity.ok(puntos);  // Respuesta exitosa
		} catch (Exception e) {
			// Respuesta con error interno del servidor
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Obtiene una lista de municipios que coinciden con un prefijo proporcionado
	 * 
	 * @param sugerencia Prefijo para filtrar los nombres de los municipios
	 * @return ResponseEntity con la lista de municipios o un error en caso de fallo
	 */
	@GetMapping("/municipios")
	public ResponseEntity<List<String>> obtenerMunicipiosPorPrefijo(@RequestParam String sugerencia) {
		try {
			// Obtener municipios que coincidan con el prefijo
			List<String> municipios = puntoDeInteresServicio.obtenerMunicipiosPorPrefijo(sugerencia);      
			return ResponseEntity.ok(municipios); // Respuesta exitosa
		} catch (Exception e) {
			// Respuesta con error interno del servidor
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}