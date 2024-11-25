package proyecto.configuracion;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AutenticacionExitosa implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Verificar los roles del usuario autenticado
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Redirigir directamente a la vista del administrador
            response.sendRedirect("/administradorVista");
        } else {
            // Redirigir al cliente o a la página de inicio
            response.sendRedirect("/home");
        }
    }
}

