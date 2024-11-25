package proyecto.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaAdministradorControlador {

    @GetMapping("/administradorVista")
    public String mostrarVistaAdministrador() {
        return "administradorVista"; // Nombre del archivo HTML (sin extensión) ubicado en `src/main/resources/templates`
    }
}
