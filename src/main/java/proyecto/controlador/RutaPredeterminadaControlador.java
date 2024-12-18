package proyecto.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;

/**
 * Controlador para gestionar las rutas predeterminadas
 * 
 * Permite listar las rutas predeterminadas junto con una imagen asociada al
 * primer punto de interés de cada ruta
 * 
 * @author Paula Ruano
 */
@Controller
public class RutaPredeterminadaControlador {
	// Variables locales
	private final RutaPredeterminadaServicio rutaPredeterminadaServicio;  
	private final PuntoDeInteresServicio puntoDeInteresServicio;

	// Constructor de la clase
	@Autowired
	public RutaPredeterminadaControlador(RutaPredeterminadaServicio rutaPredeterminadaServicio, PuntoDeInteresServicio puntoDeInteresServicio) {
		this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
		this.puntoDeInteresServicio = puntoDeInteresServicio;
	}

	/**
	 * Muestra la lista de rutas predeterminadas junto con imágenes asociadas
	 * 
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para mostrar las rutas
	 */
	@GetMapping("/listaRutasPredeterminadas")
	public String mostrarRutas(Model model) {
		// Obtener todas las rutas predeterminadas
		List<RutaPredeterminadaDTO> rutas = rutaPredeterminadaServicio.obtenerTodasLasRutas();

		// Crear una lista para agregar rutas con sus imágenes asociadas
		List<Map<String, Object>> rutasConImagen = new ArrayList<>();

		for (RutaPredeterminadaDTO ruta : rutas) {
			Map<String, Object> rutaConImagen = new HashMap<>();
			rutaConImagen.put("ruta", ruta);

			// Obtener IDs de los puntos asociados a la ruta
			List<Long> idsPuntos = rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(ruta.getId());
			if (!idsPuntos.isEmpty()) {
				// Obtener el primer punto de interés para generar la imagen
				PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
						.findFirst()
						.orElse(null);

				// Generar la URL de la imagen
				if (primerPunto != null) {         
					rutaConImagen.put("imagen", "/img/puntosInteres/" + primerPunto.getId() + ".jpg"); // Imagen primer punto
				} else {
					rutaConImagen.put("imagen", "/img/ImgComunPi.png"); // Imagen por defecto
				}
			} else {
				rutaConImagen.put("imagen", "/img/ImgComunPi.png"); // Imagen por defecto si no hay IDs
			}

			rutasConImagen.add(rutaConImagen);
		}

		// Pasar las rutas con imágenes al modelo
		model.addAttribute("rutas", rutasConImagen);

		return "listaRutasPredeterminadas"; 
	}
}