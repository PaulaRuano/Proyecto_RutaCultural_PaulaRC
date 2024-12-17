package proyecto.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.UsuarioServicio;

@Controller
public class LoginControlador {
	private final UsuarioServicio usuarioServicio;

	@Autowired
	public LoginControlador(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;        
	}

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model,
                                         @RequestParam(value = "error", required = false) String error) {
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO());
        }

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }

        return "login"; // Mostrar el formulario de login
    }

	@PostMapping("/iniciar_Sesion")
	public String iniciarSesion(@ModelAttribute UsuarioDTO usuarioDTO, Model model, @RequestParam(value = "error", required = false) String error) {
		  // Verificar si el usuario ya está autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            // Verificar si el rol es ADMIN
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                // Redirigir al administrador
                return "administradorVista";
            } 
            // Verificar si el rol es CLIENTE
            else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE"))) {
                // Redirigir al cliente
                return "home";
            }
        }

        // Si no está autenticado, mostrar el formulario de login
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO());
        }

        // Mostrar mensaje de error si hay uno
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }

        return "login"; // Mostrar la página de login
	}
}