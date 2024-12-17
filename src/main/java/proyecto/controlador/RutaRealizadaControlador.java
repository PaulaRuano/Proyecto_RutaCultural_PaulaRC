package proyecto.controlador;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.modelo.UsuarioDetalles;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dao.RutaUsuarioDAO;
import proyecto.modelo.dao.UsuarioDAO;
import proyecto.modelo.dto.RutaDTO;
import proyecto.modelo.dto.RutaRealizadaDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.RutaRealizadaServicio;

@Controller
public class RutaRealizadaControlador {

    private final RutaRealizadaServicio rutaRealizadaServicio;
    private final RutaUsuarioDAO rutaUsuarioDAO;
    private final RutaPredeterminadaDAO rutaPredeterminadaDAO;
    private final UsuarioDAO usuarioDAO;

    @Autowired
    public RutaRealizadaControlador(RutaRealizadaServicio rutaRealizadaServicio,
                                    RutaUsuarioDAO rutaUsuarioDAO,
                                    RutaPredeterminadaDAO rutaPredeterminadaDAO,
                                    UsuarioDAO usuarioDAO) {
        this.rutaRealizadaServicio = rutaRealizadaServicio;
        this.rutaUsuarioDAO = rutaUsuarioDAO;
        this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
        this.usuarioDAO = usuarioDAO;
    }
    

    @GetMapping("/listaRutasRealizadas")
    public String mostrarRutasRealizadas(Model model) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetalles usuarioDetalles = (UsuarioDetalles) authentication.getPrincipal();

        // Usar el ID del usuario para filtrar rutas
        int usuarioId = usuarioDetalles.getUsuario().getId();

        // Obtener las rutas realizadas por el usuario
        List<RutaRealizadaDTO> rutasRealizadas = rutaRealizadaServicio.obtenerRutasRealizadasPorUsuario(usuarioId);

        model.addAttribute("rutasRealizadas", rutasRealizadas);
        return "listaRutasRealizadas";
    }


    @PostMapping("/rutasRealizadas/guardar")
    public String guardarRutaRealizada(
        @RequestParam Integer rutaId,
        @RequestParam Integer usuarioId,
        @RequestParam String fecha,
        @RequestParam Integer tiempo,
        Model model
    ) {
        try {
            // Buscar la ruta y el usuario
            Optional<RutaDTO> rutaOpt = rutaUsuarioDAO.findById(rutaId)
                    .map(ruta -> (RutaDTO) ruta)
                    .or(() -> rutaPredeterminadaDAO.findById(rutaId).map(ruta -> (RutaDTO) ruta));

            RutaDTO ruta = rutaOpt.orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada."));
            UsuarioDTO usuario = usuarioDAO.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

            // Guardar la ruta realizada
            RutaRealizadaDTO rutaRealizada = new RutaRealizadaDTO();
            rutaRealizada.setRuta(ruta);
            rutaRealizada.setUsuario(usuario);
            rutaRealizada.setFecha(LocalDate.parse(fecha));
            rutaRealizada.setTiempo(tiempo);
            rutaRealizadaServicio.crearRutaRealizada(rutaRealizada);

            // Cargar las rutas realizadas en el modelo
            List<RutaRealizadaDTO> rutasRealizadas = rutaRealizadaServicio.obtenerRutasRealizadasPorUsuario(usuarioId);
            model.addAttribute("rutasRealizadas", rutasRealizadas);

            return "listaRutasRealizadas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la ruta: " + e.getMessage());
            return "hacerRuta";
        }
    }

}
