package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.MisRutasServicio;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.servicio.DetallesUsuarioServicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar las rutas del usuario, tanto creadas como guardadas
 * 
 * Permite a los usuarios comenzar, guardar y eliminar rutas personales,
 * así como interactuar con las rutas predeterminadas añadidas a su lista
 * 
 * @author Paula Ruano
 */
@Controller
public class MiRutaControlador {
	// Variables locales
	private final MisRutasServicio misRutasServicio;
	private final RutaUsuarioServicio rutaUsuarioServicio;
	private final DetallesUsuarioServicio detallesUsuarioServicio;

	// Constructor de la clase
	@Autowired
	public MiRutaControlador(MisRutasServicio misRutasServicio,
			RutaUsuarioServicio rutaUsuarioServicio,
			DetallesUsuarioServicio detallesUsuarioServicio) {
		this.misRutasServicio = misRutasServicio;
		this.rutaUsuarioServicio = rutaUsuarioServicio;
		this.detallesUsuarioServicio = detallesUsuarioServicio;
	}

	/**
	 * Carga las rutas creadas por el usuario y las guardadas en "Mis Rutas" en el modelo
	 * 
	 * Las rutas se ofrecen  información como imágenes y se ordenan por fecha de creación
	 * 
	 * @param model Modelo para pasar datos a la vista
	 */
	private void cargarRutasEnModelo(Model model) {
		UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();

		// Cargar las rutas creadas por el usuario
		List<RutaUsuarioDTO> rutasUsuario = rutaUsuarioServicio.obtenerRutasPorUsuario(usuario);
		List<Map<String, Object>> rutasUsuarioConImagen = rutasUsuario.stream()
				.map(ruta -> {
					Map<String, Object> rutaMap = new HashMap<>();
					rutaMap.put("ruta", ruta);

					// Obtener IDs de los puntos asociados a la ruta
					List<Long> idsPuntos = rutaUsuarioServicio.obtenerIdsPuntosDeRuta(ruta.getId());
					rutaMap.put("imagen", idsPuntos.isEmpty() ? "/img/ImgComunPi.png" : "/img/puntosInteres/" + idsPuntos.get(0) + ".jpg");
					return rutaMap;
				})
				.sorted((a, b) -> {
					LocalDate fechaA = ((RutaUsuarioDTO) a.get("ruta")).getFechaCreacion();
					LocalDate fechaB = ((RutaUsuarioDTO) b.get("ruta")).getFechaCreacion();
					return fechaB.compareTo(fechaA); // Orden descendente por fecha
				})
				.toList();

		// Cargar las rutas predeterminadas añadidas por el usuario
		List<Map<String, Object>> rutasMisRutasConImagen = misRutasServicio.obtenerRutasPredeterminadasConDetalles(usuario)
				.stream()
				.sorted((a, b) -> {
					LocalDate fechaA = (LocalDate) a.get("fecha");
					LocalDate fechaB = (LocalDate) b.get("fecha");
					return fechaB.compareTo(fechaA); // Orden descendente por fecha
				})
				.toList();

		// Agregar las rutas al modelo
		model.addAttribute("rutasUsuario", rutasUsuarioConImagen);
		model.addAttribute("rutasMisRutas", rutasMisRutasConImagen);
	}

	/**
	 * Muestra la lista de rutas del usuario, incluyendo las creadas y las guardadas
	 * 
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla para la vista de "Mis Rutas"
	 */
	@GetMapping("/listaMisRutas")
	public String listarMisRutas(Model model) {
		cargarRutasEnModelo(model);
		return "listaMisRutas";
	}

	/**
	 * Elimina una ruta creada por el usuario.
	 * 
	 * @param id ID de la ruta a eliminar
	 * @param redirectAttributes Atributos para mensajes flash en la redirección
	 * @param model Modelo para recargar las rutas
	 * @return Nombre de la plantilla para la vista de "Mis Rutas"
	 */
	@GetMapping("/eliminarRutaUsuario")
	public String eliminarRutaUsuario(@RequestParam int id, RedirectAttributes redirectAttributes, Model model) {
		try {
			// Eliminar la ruta
			rutaUsuarioServicio.eliminarRutaUsuario(id);

			// Mensaje de éxito
			redirectAttributes.addFlashAttribute("success", "Ruta eliminada con éxito.");
		} catch (Exception e) {
			// Manejo de errores
			redirectAttributes.addFlashAttribute("error", "Error al eliminar la ruta: " + e.getMessage());
		}

		// Volver a cargar las rutas en el modelo
		cargarRutasEnModelo(model);

		return "listaMisRutas";
	}

	/**
	 * Guarda una ruta predeterminada en la lista de "Mis Rutas".
	 * 
	 * @param rutaId ID de la ruta predeterminada a guardar
	 * @param redirectAttributes Atributos para mensajes flash en la redirección
	 * @return Redirección a la vista de rutas predeterminadas
	 */
	@PostMapping("/guardarEnMisRutas")
	public String guardarEnMisRutas(@RequestParam Integer rutaId, RedirectAttributes redirectAttributes) {
		try {
			// Obtener el usuario autenticado
			UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();

			// Guardar la relación en Mis Rutas
			misRutasServicio.guardarMisRutas(rutaId, usuario);

			redirectAttributes.addFlashAttribute("success", "Ruta guardada en 'Mis Rutas' con éxito.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "No se pudo guardar la ruta en 'Mis Rutas': " + e.getMessage());
		}

		return "redirect:/listaRutasPredeterminadas";
	}
}