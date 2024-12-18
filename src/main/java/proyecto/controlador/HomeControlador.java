package proyecto.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para gestionar la página principal (home)
 * 
 * Maneja la solicitud para mostrar la página de inicio y verifica
 * si el usuario está autenticado para personalizar la vista
 * 
 * @author Paula Ruano
 */
@Controller
public class HomeControlador {
	/**
	 * Muestra la página de inicio
	 * 
	 * Verifica si el usuario está autenticado y pasa el estado al modelo
	 * para personalizar la vista
	 * 
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para la página de inicio
	 */
	@GetMapping({"/home", "/"}) 
	public String mostrarHome(Model model) {
		// Obtener la autenticación actual
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && 
				!authentication.getPrincipal().equals("anonymousUser");

		// Pasar el estado de autenticación al modelo
		model.addAttribute("isAuthenticated", isAuthenticated);

		return "home";
	}
}
