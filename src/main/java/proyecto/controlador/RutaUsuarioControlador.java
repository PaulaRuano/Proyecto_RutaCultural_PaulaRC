package proyecto.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.servicio.PuntoDeInteresServicio;

/**
 * Controlador para gestionar la creación de rutas personalizadas por los usuarios
 * 
 * Permite mostrar puntos de interés paginados y manejar los puntos seleccionados
 * para formar una ruta
 * 
 * @author Paula Ruano
 */
@Controller
public class RutaUsuarioControlador {
	// Variable local
	private final PuntoDeInteresServicio puntoDeInteresServicio;

	// Constructor de la clase
	@Autowired
	public RutaUsuarioControlador(PuntoDeInteresServicio puntoDeInteresServicio) {
		this.puntoDeInteresServicio = puntoDeInteresServicio;       
	}

	/**
	 * Muestra los puntos de interés disponibles para crear una ruta
	 * 
	 * Los puntos se obtienen de manera paginada para facilitar la navegación
	 * 
	 * @param page Página actual de puntos (valor predeterminado: 0)
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para mostrar puntos
	 */
	@GetMapping("/crearRutaUsuario")
	public String mostrarPuntos(@RequestParam(defaultValue = "0") int page, Model model) {
		int pageSize = 10; // Número de puntos por página
		List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosDeInteresPaginados(page, pageSize);

		// Verifica si hay más puntos disponibles
		boolean hasNextPage = puntos.size() == pageSize; 

		model.addAttribute("puntos", puntos);
		model.addAttribute("currentPage", page);
		model.addAttribute("hasNextPage", hasNextPage);

		return "crearRutaUsuario";
	}

	/**
	 * Proporciona una lista vacía inicial para los puntos seleccionados en la sesión
	 * 
	 * @return Lista inicial vacía de puntos seleccionados
	 */
	@ModelAttribute("puntosSeleccionados")
	public List<PuntoDeInteresDTO> puntosSeleccionados() {
		return new ArrayList<>();
	}

	/**
	 * Guarda los puntos seleccionados en una lista de la sesión
	 * 
	 * Este método recibe los puntos seleccionados en el frontend y los almacena
	 * en una lista en el backend para usarlos posteriormente
	 * 
	 * @param puntos Lista de puntos seleccionados enviada desde el cliente
	 * @param puntosSeleccionados Lista de puntos seleccionados en la sesión
	 * @return Mensaje de confirmación
	 */
	@PostMapping("/guardarPuntosSeleccionados")
	@ResponseBody // Indica que el valor devuelto por este método se enviará directamente al cuerpo de la respuesta HTTP, en lugar de redirigir a una vista.
	public String guardarPuntosSeleccionados(@RequestBody List<PuntoDeInteresDTO> puntos,
			@ModelAttribute("puntosSeleccionados") List<PuntoDeInteresDTO> puntosSeleccionados) {
		// Limpiar la lista actual y agregar los nuevos puntos seleccionados
		puntosSeleccionados.clear();
		puntosSeleccionados.addAll(puntos);
		return "Puntos seleccionados guardados en sesión";
	}

}