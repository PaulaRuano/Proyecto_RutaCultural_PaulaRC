package proyecto.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControlador {
	@GetMapping("/home")
	public String mostrarHome(Model model) {
		// Verificar si el usuario está autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && 
				!authentication.getPrincipal().equals("anonymousUser");

		// Pasar el estado al modelo
		model.addAttribute("isAuthenticated", isAuthenticated);

		return "home";
	}
}
