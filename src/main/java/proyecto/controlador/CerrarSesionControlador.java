package proyecto.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para gestionar el cierre de sesión de los usuarios
 * 
 * Maneja la invalidación de la sesión actual del cliente y redirige a la página de inicio
 * 
 * @author Paula Ruano
 */
@Controller
public class CerrarSesionControlador {
	/**
	 * Maneja la solicitud para cerrar sesión
	 * 
	 * Invalida la sesión del usuario, elimina la autenticación y redirige al inicio   
	 * 
	 * @param request Objeto HttpServletRequest para manejar la sesión
	 * @param response Objeto HttpServletResponse para manejar la respuesta
	 * @param redirectAttributes Objeto para pasar mensajes entre redirecciones
	 * @return Redirección a la página de inicio
	 */
	@GetMapping("/cerrarSesion")
	public String cerrarSesion(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			// Invalidar la sesión actual del usuario
			request.getSession().invalidate();
		} catch (Exception e) {
			// Enviar un mensaje de error si ocurre un problema al cerrar sesión
			redirectAttributes.addFlashAttribute("error", "No se pudo cerrar la sesión. Intente nuevamente.");
			return "redirect:/home";
		}

		return "redirect:/home";
	}
}