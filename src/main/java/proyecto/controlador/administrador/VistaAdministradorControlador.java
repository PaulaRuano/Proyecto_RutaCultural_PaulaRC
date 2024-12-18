package proyecto.controlador.administrador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para gestionar la vista principal del administrador
 * 
 * Este controlador simplemente redirige a la plantilla de la vista del administrador
 * 
 * @author Paula Ruano
 */
@Controller
public class VistaAdministradorControlador {
	/**
	 * Muestra la vista principal del administrador
	 * 
	 * @return Nombre de la plantilla de la vista para el administrador
	 */
	@GetMapping("/administradorVista")
	public String mostrarVistaAdministrador() {
		return "administradorVista"; 
	}
}
