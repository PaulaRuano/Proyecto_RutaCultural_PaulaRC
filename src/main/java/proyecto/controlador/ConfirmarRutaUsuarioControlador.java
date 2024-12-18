package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.List;

import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPuntoInteresDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.DetallesUsuarioServicio;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaUsuarioServicio;

/**
 * Controlador para gestionar la creación y confirmación de rutas personalizadas por los usuarios
 * 
 * Permite a los usuarios seleccionar puntos de interés, confirmar los detalles de la ruta
 * y guardar la nueva ruta en la base de datos
 * 
 * @author Paula Ruano
 */
@Controller
public class ConfirmarRutaUsuarioControlador {
	// Variables locales
	private final PuntoDeInteresServicio puntoDeInteresServicio;
	private final RutaUsuarioServicio rutaUsuarioServicio;
	private final DetallesUsuarioServicio usuarioDetallesServicio;
	private final RutaPuntoInteresDAO rutaPuntoInteresDAO;

	// Constructor de la clase
	@Autowired
	public ConfirmarRutaUsuarioControlador(PuntoDeInteresServicio puntoDeInteresServicio,
			RutaUsuarioServicio rutaUsuarioServicio,
			DetallesUsuarioServicio usuarioDetallesServicio,
			RutaPuntoInteresDAO rutaPuntoInteresDAO) {
		this.puntoDeInteresServicio = puntoDeInteresServicio;
		this.rutaUsuarioServicio = rutaUsuarioServicio;
		this.usuarioDetallesServicio = usuarioDetallesServicio;
		this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
	}

	/**
	 * Muestra una vista de confirmación para una ruta basada en los puntos seleccionados
	 * 
	 * Valida que haya al menos 2 puntos seleccionados y que pertenezcan al mismo municipio
	 * 
	 * @param puntoIds Lista de IDs de puntos seleccionados
	 * @param model Modelo para pasar datos a la vista
	 * @param redirectAttributes Objeto para pasar mensajes entre redirecciones
	 * @return Nombre de la vista de confirmación o redirección en caso de error
	 */
	@PostMapping("/confirmarRutaUsuario")
	public String confirmarRuta(
			@RequestParam List<Long> puntoIds,     
			Model model,
			RedirectAttributes redirectAttributes) {

		// Validar que al menos hay 2 puntos seleccionados
		if (puntoIds == null || puntoIds.size() < 2) {
			redirectAttributes.addFlashAttribute("error", "Debes seleccionar al menos 2 puntos para crear una ruta.");
			return "redirect:/crearRutaUsuario";
		}

		// Obtener los puntos seleccionados
		List<PuntoDeInteresDTO> puntosSeleccionados = puntoDeInteresServicio.obtenerPuntosPorIds(puntoIds);

		// Validar que todos los puntos pertenezcan al mismo municipio
		String municipio = puntosSeleccionados.get(0).getLocalidad();
		boolean mismoMunicipio = puntosSeleccionados.stream()
				.allMatch(p -> p.getLocalidad().equalsIgnoreCase(municipio));

		if (!mismoMunicipio) {
			redirectAttributes.addFlashAttribute("error", "Todos los puntos seleccionados deben pertenecer al mismo municipio.");
			return "redirect:/crearRutaUsuario";
		}

		// Agregar los puntos al modelo
		model.addAttribute("puntos", puntosSeleccionados); 

		// Devolver el nombre de la vista para confirmar la ruta
		return "confirmarRutaUsuario";
	}

	/**
	 * Guarda una nueva ruta personalizada creada por el usuario
	 * 
	 * Valida los datos ingresados, crea la ruta en la base de datos
	 * y asocia los puntos seleccionados a la ruta
	 * 
	 * @param duracion Duración estimada de la ruta en minutos
	 * @param distancia Distancia total de la ruta en metros
	 * @param municipio Municipio al que pertenece la ruta
	 * @param nombreRuta Nombre de la nueva ruta
	 * @param puntoIds Lista de IDs de puntos seleccionados
	 * @param redirectAttributes Objeto para pasar mensajes entre redirecciones
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la vista o redirección tras guardar la ruta
	 */
	@PostMapping("/guardarRutaUsuario")
	public String guardarRutaUsuario(@RequestParam String duracion,
			@RequestParam int distancia,
			@RequestParam String municipio,
			@RequestParam String nombreRuta,
			@RequestParam List<Long> puntoIds,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			// Validar si se han seleccionado puntos
			if (puntoIds == null || puntoIds.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos 2 puntos.");
				return "crearRutaUsuario";
			}

			// Validar si el nombre de la ruta está vacío
			if (nombreRuta == null || nombreRuta.trim().isEmpty()) {
				model.addAttribute("error", "Introduce un nombre para la ruta, por favor.");
				List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosPorIds(puntoIds); // Recuperar los puntos seleccionados
				model.addAttribute("puntos", puntos); // Agregar puntos al modelo
				return "confirmarRutaUsuario"; // Retornar la vista sin redirección
			}

			// Obtener el usuario autenticado
			UsuarioDTO usuario = usuarioDetallesServicio.obtenerUsuarioActual();

			// Verificar los puntos en la base de datos, y crear si no existen
			List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerOCrearPuntosDeInteres(puntoIds);

			// Crear la ruta de usuario
			RutaUsuarioDTO nuevaRuta = rutaUsuarioServicio.crearRutaUsuario(nombreRuta, Double.parseDouble(duracion), distancia, municipio, usuario);

			// Crear registros en la tabla intermedia ruta_punto_interes
			for (PuntoDeInteresDTO punto : puntos) {
				RutaPuntoInteresDTO relacion = new RutaPuntoInteresDTO();
				relacion.setRuta(nuevaRuta);
				relacion.setPuntoInteres(punto);
				rutaPuntoInteresDAO.save(relacion); // Guardar relación en la tabla intermedia
			}

			model.addAttribute("success", "Ruta creada con éxito.");
			return "fragmentos/modalRutaUsuarioCreada"; 
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al guardar la ruta: " + e.getMessage());
			return "confirmarRutaUsuario";
		}
	}
}