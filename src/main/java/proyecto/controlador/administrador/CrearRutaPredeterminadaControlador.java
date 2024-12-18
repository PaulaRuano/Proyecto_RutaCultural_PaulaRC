package proyecto.controlador.administrador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;

/**
 * Controlador para gestionar la creación de rutas predeterminadas.
 * 
 * Este controlador permite a los administradores crear rutas predeterminadas
 * utilizando puntos de interés y calculando automáticamente la distancia y duración
 * 
 * Requiere que el usuario tenga el rol de ADMIN
 * 
 * @author Paula Ruano
 */
@Controller
public class CrearRutaPredeterminadaControlador {
	// Variables locales
	private final PuntoDeInteresServicio puntoDeInteresServicio;
	private final RutaPredeterminadaServicio rutaPredeterminadaServicio;

	// Constructor de la clase
	@Autowired
	public CrearRutaPredeterminadaControlador(PuntoDeInteresServicio puntoDeInteresServicio, 
			RutaPredeterminadaServicio rutaPredeterminadaServicio) {
		this.puntoDeInteresServicio = puntoDeInteresServicio;
		this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
	}

	/**
	 * Procesa el formulario para crear una ruta predeterminada
	 * 
	 * Valida los datos ingresados, calcula la distancia y duración de la ruta
	 * y crea la nueva ruta predeterminada si todo es válido
	 * 
	 * @param nombreRuta Nombre de la ruta predeterminada
	 * @param puntoInteresIds Lista de IDs de los puntos de interés incluidos en la ruta
	 * @param redirectAttributes Objeto para enviar mensajes entre redirecciones
	 * @param model Modelo para pasar datos a la vista
	 * @return Redirección a la vista del administrador o vista de error
	 */
	@PostMapping("/crearRutaPredeterminada")
	@PreAuthorize("hasRole('ADMIN')")
	public String procesarFormulario(@RequestParam String nombreRuta,
			@RequestParam List<String> puntoInteresIds,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			// Validar y convertir los IDs de puntos de interés
			List<Long> puntoIds = new ArrayList<>();
			for (String id : puntoInteresIds) {
				if (id == null || id.trim().isEmpty()) {
					redirectAttributes.addFlashAttribute("error", "Los campos de ID no pueden estar vacíos.");
					return "redirect:/administradorVista";
				}
				try {
					// Intentar convertir a Long
					puntoIds.add(Long.parseLong(id)); 
				} catch (NumberFormatException e) {
					model.addAttribute("error", "Todos los IDs de puntos de interés deben ser números, no caractéres.");
					model.addAttribute("nombreRuta", nombreRuta); 
					model.addAttribute("puntoInteresIds", puntoInteresIds);
					return "administradorVista"; // Volver a la vista con el error
				}
			}

			// Obtener o crear los puntos de interés
			List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerOCrearPuntosDeInteres(puntoIds);

			// Validar que se obtuvieron todos los puntos necesarios
			if (puntos.size() != puntoIds.size()) {
				model.addAttribute("error", "Algunos puntos de interés no pudieron ser procesados.");
				return "administradorVista";
			}

			// Calcular distancia y duración
			Map<String, Object> calculos = rutaPredeterminadaServicio.calcularDistanciaYDuracion(puntos);
			double duracion = (int) calculos.get("duration") / 60.0; // Convertir a minutos
			int distancia = (int) calculos.get("distance"); // En metros

			// Crear la ruta predeterminada
			rutaPredeterminadaServicio.crearRutaPredeterminada(
					nombreRuta, puntos, duracion, distancia, puntos.get(0).getLocalidad()
					);

			// Agregar mensaje de éxito
			redirectAttributes.addFlashAttribute("success", "Ruta creada con éxito.");
		} catch (Exception e) {
			model.addAttribute("error", "Error al crear la ruta: " + e.getMessage());
			return "administradorVista";
		}

		return "redirect:/administradorVista";
	}
}