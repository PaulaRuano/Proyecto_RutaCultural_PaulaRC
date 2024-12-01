package proyecto.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CerrarSesionControlador {

    @GetMapping("/cerrarSesion")
    public String cerrarSesion(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            // Invalidar la sesión y eliminar autenticación
            request.getSession().invalidate();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo cerrar la sesión. Intente nuevamente.");
            return "redirect:/home";
        }
        // Agregar mensaje de éxito
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada correctamente.");
        return "redirect:/home";
    }
}