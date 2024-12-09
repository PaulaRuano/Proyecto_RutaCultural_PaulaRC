package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.MiRutaDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.MisRutasServicio;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.servicio.DetallesUsuarioServicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    private void cargarRutasEnModelo(Model model) {
        UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();

        // Cargar las rutas creadas por el usuario
        List<RutaUsuarioDTO> rutasUsuario = rutaUsuarioServicio.obtenerRutasPorUsuario(usuario);
        List<Map<String, Object>> rutasUsuarioConImagen = rutasUsuario.stream()
            .map(ruta -> {
                Map<String, Object> rutaMap = new HashMap<>();
                rutaMap.put("ruta", ruta);

                // Obtener IDs de los puntos asociados a la ruta
                List<Long> idsPuntos = rutaUsuarioServicio.obtenerIdsPuntosDeRuta(ruta.getId());
                rutaMap.put("imagen", idsPuntos.isEmpty() ? "/img/ImgComunPi.png" : "/img/puntosInteres/" + idsPuntos.get(0) + ".jpg");
                return rutaMap;
            })
            .sorted((a, b) -> {
                LocalDate fechaA = ((RutaUsuarioDTO) a.get("ruta")).getFechaCreacion();
                LocalDate fechaB = ((RutaUsuarioDTO) b.get("ruta")).getFechaCreacion();
                return fechaB.compareTo(fechaA);
            })
            .toList();

        // Cargar las rutas predeterminadas añadidas por el usuario
        List<Map<String, Object>> rutasMisRutasConImagen = misRutasServicio.obtenerRutasPredeterminadasConDetalles(usuario)
            .stream()
            .sorted((a, b) -> {
                LocalDate fechaA = (LocalDate) a.get("fecha");
                LocalDate fechaB = (LocalDate) b.get("fecha");
                return fechaB.compareTo(fechaA);
            })
            .toList();

        // Agregar las rutas al modelo
        model.addAttribute("rutasUsuario", rutasUsuarioConImagen);
        model.addAttribute("rutasMisRutas", rutasMisRutasConImagen);
    }

    
    @GetMapping("/listaMisRutas")
    public String listarMisRutas(Model model) {
        cargarRutasEnModelo(model);
        return "listaMisRutas";
    }

    @GetMapping("/eliminarRutaUsuario")
    public String eliminarRutaUsuario(@RequestParam int id, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Eliminar la ruta
            rutaUsuarioServicio.eliminarRutaUsuario(id);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("success", "Ruta eliminada con éxito.");
        } catch (Exception e) {
            // Manejo de errores
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la ruta: " + e.getMessage());
        }

        // Volver a cargar las rutas en el modelo
        cargarRutasEnModelo(model);

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
