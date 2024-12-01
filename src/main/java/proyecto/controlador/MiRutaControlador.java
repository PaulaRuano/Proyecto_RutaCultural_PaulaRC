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
    @GetMapping("/listaMisRutas")
    public String listarMisRutas(Model model) {
        UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();

        // Rutas creadas por el usuario
        List<RutaUsuarioDTO> rutasUsuario = rutaUsuarioServicio.obtenerRutasPorUsuario(usuario);
        List<Map<String, Object>> rutasUsuarioConImagen = rutasUsuario.stream()
            .map(ruta -> {
                Map<String, Object> rutaMap = new HashMap<>();
                rutaMap.put("ruta", ruta);

                // Obtener IDs de los puntos asociados a la ruta
                List<Long> idsPuntos = rutaUsuarioServicio.obtenerIdsPuntosDeRuta(ruta.getId());
                if (!idsPuntos.isEmpty()) {
                    rutaMap.put("imagen", "/img/puntosInteres/" + idsPuntos.get(0) + ".jpg");
                } else {
                    rutaMap.put("imagen", "/img/ImgComunPi.png");
                }
                return rutaMap;
            })
            // Ordenar por fechaCreacion (más recientes primero)
            .sorted((a, b) -> {
                LocalDate fechaA = ((RutaUsuarioDTO) a.get("ruta")).getFechaCreacion();
                LocalDate fechaB = ((RutaUsuarioDTO) b.get("ruta")).getFechaCreacion();
                return fechaB.compareTo(fechaA);
            })
            .toList();

        // Rutas predeterminadas añadidas a "Mis Rutas"
        List<Map<String, Object>> rutasMisRutasConImagen = misRutasServicio.obtenerRutasPredeterminadasConDetalles(usuario)
            .stream()
            // Ordenar por fecha (más recientes primero)
            .sorted((a, b) -> {
                LocalDate fechaA = (LocalDate) a.get("fecha");
                LocalDate fechaB = (LocalDate) b.get("fecha");
                return fechaB.compareTo(fechaA);
            })
            .toList();

        // Pasar las listas al modelo
        model.addAttribute("rutasUsuario", rutasUsuarioConImagen);
        model.addAttribute("rutasMisRutas", rutasMisRutasConImagen);

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
