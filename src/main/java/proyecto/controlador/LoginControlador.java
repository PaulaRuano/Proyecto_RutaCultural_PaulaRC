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
                                         @RequestParam(value = "error", required = false) String error)
                                       // HttpSession session) 
                                         {
        // Verificar si ya hay un usuario autenticado en SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            // Obtener detalles del usuario autenticado
            String correo = authentication.getName(); // Obtener correo del usuario autenticado
            Optional<UsuarioDTO> usuarioOpt = usuarioServicio.buscarPorCorreo(correo);
            

            usuarioOpt.ifPresent(usuario -> 
                System.out.println("Usuario autenticado: " + usuario.getUsuario())
            );
          
               // UsuarioDTO usuarioLogueado = usuarioOpt.get();
                //session.setAttribute("usuario", usuarioLogueado); // Guardar usuario en la sesión                             
        }

        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO());
        }

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }

        return "login";
    }

    @PostMapping("/iniciar_Sesion")
    public String iniciarSesion(@ModelAttribute UsuarioDTO usuarioDTO, Model model, HttpSession session) {
        System.out.println("Inicio de sesión iniciado para correo: " + usuarioDTO.getCorreo());

        Optional<UsuarioDTO> usuarioOpt = usuarioServicio.buscarPorCorreo(usuarioDTO.getCorreo());
        if (usuarioOpt.isEmpty() || 
            !usuarioServicio.verificarContrasenia(usuarioDTO.getCorreo(), usuarioDTO.getContrasenia())) {
            System.out.println("Credenciales inválidas para correo: " + usuarioDTO.getCorreo());
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }

        UsuarioDTO usuarioLogueado = usuarioOpt.get();
        session.setAttribute("usuario", usuarioLogueado);
        System.out.println("Usuario almacenado en sesión: " + usuarioLogueado.getUsuario());

        return "redirect:/home";
    }
}