package proyecto.configuracion;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que gestiona el comportamiento después de una autenticación exitosa
 * 
 * Esta clase redirige a los usuarios a diferentes vistas según su rol 
 * después de un inicio de sesión exitoso 
 * 
 * @author Paula Ruano
 */
@Component // Componente gestionado por el contenedor de Spring
public class AutenticacionExitosa implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

	/**
	 * Método que se ejecuta tras una autenticación exitosa
	 * 
	 * Redirige a la vista del administrador si el rol del usuario es ADMIN, 
	 * de lo contrario, redirige a la página de inicio del cliente
	 * 
	 * @param request Solicitud HTTP
	 * @param response Respuesta HTTP
	 * @param authentication Información de autenticación del usuario
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		// Verificar los roles del usuario autenticado
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
		if (isAdmin) {
			// Redirigir al administrador a la vista del administrador
			response.sendRedirect("/administradorVista");
		} else {
			// Redirigir al cliente o a la página de inicio
			response.sendRedirect("/home");
		}
	}
}

