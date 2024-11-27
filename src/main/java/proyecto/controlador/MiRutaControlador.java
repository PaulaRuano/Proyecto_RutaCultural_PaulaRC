package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.MisRutasServicio;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.servicio.DetallesUsuarioServicio;

import java.util.List;

@Controller
public class MiRutaControlador {

    private final MisRutasServicio misRutasServicio;
    private final RutaUsuarioServicio rutaUsuarioServicio;
    private final DetallesUsuarioServicio detallesUsuarioServicio;

    @Autowired
    public MiRutaControlador(MisRutasServicio misRutasServicio,
                               RutaUsuarioServicio rutaUsuarioServicio,
                               DetallesUsuarioServicio detallesUsuarioServicio) {
        this.misRutasServicio = misRutasServicio;
        this.rutaUsuarioServicio = rutaUsuarioServicio;
        this.detallesUsuarioServicio = detallesUsuarioServicio;
    }

    @GetMapping("/listaMisRutas")
    public String listarMisRutas(Model model) {
        // Obtener el usuario autenticado
        UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();

        // Rutas creadas por el usuario
        List<RutaUsuarioDTO> rutasUsuario = rutaUsuarioServicio.obtenerRutasPorUsuario(usuario);

        // Rutas predeterminadas añadidas a "Mis Rutas"
        List<RutaPredeterminadaDTO> rutasMisRutas = misRutasServicio.obtenerRutasPredeterminadasPorUsuario(usuario);

        // Pasar las listas al modelo
        model.addAttribute("rutasUsuario", rutasUsuario);
        model.addAttribute("rutasMisRutas", rutasMisRutas);

        return "listaMisRutas";
    }
    
    @PostMapping("/guardarEnMisRutas")
    public String guardarEnMisRutas(@RequestParam Integer rutaId, RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario autenticado
            UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();

            // Guardar la relación en Mis Rutas
            misRutasServicio.guardarMisRutas(rutaId, usuario);

            redirectAttributes.addFlashAttribute("success", "Ruta guardada en 'Mis Rutas' con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo guardar la ruta en 'Mis Rutas': " + e.getMessage());
        }

        return "redirect:/listaRutasPredeterminadas";
    }

}
