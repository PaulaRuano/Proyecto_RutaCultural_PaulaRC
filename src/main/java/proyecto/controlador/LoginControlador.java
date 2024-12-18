package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.modelo.dto.UsuarioDTO;

/**
 * Controlador para gestionar el inicio de sesión de usuarios
 * 
 * Maneja las solicitudes para mostrar el formulario de login y redirigir a las vistas
 * correspondientes según el rol del usuario
 * 
 * @author Paula Ruano
 */
@Controller
public class LoginControlador {
	// Constructor de la clase
	@Autowired
	public LoginControlador() {

	}

	/**
	 * Muestra el formulario de login.
	 * 
	 * @param model Modelo para pasar datos a la vista
	 * @param error Parámetro opcional que indica si ocurrió un error de login
	 * @return Nombre de la plantilla de la vista para el formulario de login
	 */
	@GetMapping("/login")
	public String mostrarFormularioLogin(Model model,
			@RequestParam(value = "error", required = false) String error) {
		if (!model.containsAttribute("usuarioDTO")) {
			model.addAttribute("usuarioDTO", new UsuarioDTO());
		}

		// Agregar mensaje de error si hay uno
		if (error != null) {
			model.addAttribute("error", "Credenciales inválidas");
		}

		return "login"; 
	}

	/**
	 * Maneja el inicio de sesión del usuario.
	 * 
	 * Verifica si el usuario ya está autenticado y lo redirige a la vista correspondiente
	 * según su rol.
	 * 
	 * @param usuarioDTO Objeto con los datos del usuario que intenta iniciar sesión
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la vista o redirección según el estado de autenticación
	 */
	@PostMapping("/iniciar_Sesion")
	public String iniciarSesion(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
		// Obtener la autenticación actual
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Verificar si el usuario ya está autenticado
		if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
			// Verificar si el rol es ADMIN
			if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
				// Redirigir a la vista administrador
				return "administradorVista";
			} 
			// Verificar si el rol es CLIENTE
			else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE"))) {
				// Redirigir a home
				return "home";
			}
		}

		// Si no está autenticado, mostrar el formulario de login
		if (!model.containsAttribute("usuarioDTO")) {
			model.addAttribute("usuarioDTO", new UsuarioDTO());
		}       

		return "login"; 
	}
}