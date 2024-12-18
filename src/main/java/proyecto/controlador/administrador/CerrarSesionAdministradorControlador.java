package proyecto.controlador.administrador;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para gestionar el cierre de sesión de un administrador
 * 
 * Maneja la invalidación de la sesión del administrador y la limpieza
 * del contexto de seguridad
 * 
 * @author Paula Ruano
 */
@Controller
public class CerrarSesionAdministradorControlador {
	/**
	 * Maneja la solicitud para cerrar sesión de un administrador
	 * 
	 * Invalida la sesión actual, limpia el contexto de seguridad y redirige
	 * al administrador a la página de inicio de sesión
	 * 
	 * @param request Objeto HttpServletRequest para manejar la sesión
	 * @param response Objeto HttpServletResponse para manejar la respuesta
	 * @return Redirección a la página de inicio de sesión
	 */
	@GetMapping("/cerrarSesionAdmin")
	public String cerrarSesion(HttpServletRequest request, HttpServletResponse response) {
		// Obtener la autenticación actual
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			// Invalidar la sesión actual
			request.getSession().invalidate();

			// Limpiar el contexto de seguridad
			SecurityContextHolder.clearContext();
		}

		// Redirigir al inicio de sesión
		return "redirect:/login"; 
	}
}