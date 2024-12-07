package proyecto.controlador.administrador;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CerrarSesionAdministradorControlador {

    @GetMapping("/cerrarSesionAdmin")
    public String cerrarSesion(HttpServletRequest request, HttpServletResponse response) {
        // Obtener la autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // Invalidar sesión
            request.getSession().invalidate();

            // Limpiar el contexto de seguridad
            SecurityContextHolder.clearContext();
        }

        // Redirigir al usuario a la página de inicio o inicio de sesión
        return "redirect:/login"; // Cambia "/login" a la URL adecuada en tu proyecto
    }
}