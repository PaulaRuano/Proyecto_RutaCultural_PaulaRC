package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;

import java.util.List;

/**
 * Controlador para gestionar la visualización de detalles de rutas
 * 
 * Permite mostrar información detallada sobre rutas predeterminadas y rutas creadas por los usuarios,
 * incluyendo los puntos de interés asociados y una imagen
 * 
 * @author Paula Ruano
 */
@Controller
@RequestMapping("/detalleRuta")
public class DetalleRutaControlador {
	// Variables locales
	private final RutaPredeterminadaServicio rutaPredeterminadaServicio;
	private final RutaUsuarioServicio rutaUsuarioServicio;
	private final PuntoDeInteresServicio puntoDeInteresServicio;

	// Constructor de la clase
	@Autowired
	public DetalleRutaControlador(RutaPredeterminadaServicio rutaPredeterminadaServicio, 
			RutaUsuarioServicio rutaUsuarioServicio, 
			PuntoDeInteresServicio puntoDeInteresServicio) {
		this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
		this.rutaUsuarioServicio = rutaUsuarioServicio;
		this.puntoDeInteresServicio = puntoDeInteresServicio;
	}

	/**
	 * Muestra los detalles de una ruta predeterminada, incluyendo los puntos de interés asociados
	 * 
	 * @param id ID de la ruta predeterminada
	 * @param origen Indica la página de origen para gestionar la navegación
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para los detalles de la ruta predeterminada
	 */
	@GetMapping("/predeterminada/{id}")
	public String mostrarDetalleRutaPredeterminada(@PathVariable("id") int id,
			@RequestParam(value = "origen", required = false, defaultValue = "listaRutasPredeterminadas") String origen,
			Model model) {
		// Obtener la ruta predeterminada
		RutaPredeterminadaDTO ruta = rutaPredeterminadaServicio.obtenerRutaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Ruta con ID " + id + " no encontrada."));

		// Obtener los IDs de los puntos relacionados con la ruta
		List<Long> idsPuntos = rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(id);
		PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
				.findFirst()
				.orElse(null);

		// Configurar la imagen
		String imagenRuta = (primerPunto != null) ? "/img/puntosInteres/" + primerPunto.getId() + ".jpg" : "/img/ImgComunPI.png";
		List<PuntoDeInteresDTO> puntosDeInteres = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos);

		// Añadir datos al modelo para la vista
		model.addAttribute("ruta", ruta);
		model.addAttribute("imagen", imagenRuta);
		model.addAttribute("puntosDeInteres", puntosDeInteres);
		model.addAttribute("origen", origen);

		return "detalleRutaPredeterminada";
	}

	/**
	 * Muestra los detalles de una ruta creada por un usuario, incluyendo los puntos de interés asociados
	 * 
	 * @param id ID de la ruta creada por el usuario
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para los detalles de la ruta del usuario
	 */
	@GetMapping("/usuario/{id}")
	public String mostrarDetalleRutaUsuario(@PathVariable("id") int id, Model model) {
		// Obtener la ruta creada por el usuario
		RutaUsuarioDTO ruta = rutaUsuarioServicio.obtenerRutaPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Ruta con ID " + id + " no encontrada."));

		// Obtener los IDs de los puntos relacionados con la ruta
		List<Long> idsPuntos = rutaUsuarioServicio.obtenerIdsPuntosDeRuta(id);
		PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
				.findFirst()
				.orElse(null);

		// Configurar la imagen
		String imagenRuta = (primerPunto != null) ? "/img/puntosInteres/" + primerPunto.getId() + ".jpg" : "/img/ImgComunPI.png";
		List<PuntoDeInteresDTO> puntosDeInteres = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos);

		// Añadir datos al modelo para la vista
		model.addAttribute("ruta", ruta);
		model.addAttribute("imagen", imagenRuta);
		model.addAttribute("puntosDeInteres", puntosDeInteres);

		return "detalleRutaUsuario";
	}
}