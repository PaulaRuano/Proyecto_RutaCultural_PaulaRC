package proyecto.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<Map<String, Object>> rutasConTitulos = rutaRealizadaServicio.obtenerRutasConTitulos();
        model.addAttribute("rutasRealizadas", rutasConTitulos);
        return "listaRutasRealizadas"; // Nombre de la vista
    }

    @PostMapping("/rutasRealizadas/guardar")
    public String guardarRutaRealizada(
        @RequestParam Integer rutaId,
        @RequestParam Integer usuarioId,
        @RequestParam String fecha,
        @RequestParam Integer tiempo,
        RedirectAttributes redirectAttributes
    ) {
        try {
            // Buscar la ruta en ambos DAOs
            Optional<RutaDTO> rutaOpt = rutaUsuarioDAO.findById(rutaId)
                    .map(ruta -> (RutaDTO) ruta) // Convertir a RutaDTO para compatibilidad
                    .or(() -> rutaPredeterminadaDAO.findById(rutaId).map(ruta -> (RutaDTO) ruta));

            RutaDTO ruta = rutaOpt.orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada."));

            // Buscar el usuario
            UsuarioDTO usuario = usuarioDAO.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

            // Crear la entidad RutaRealizadaDTO
            RutaRealizadaDTO rutaRealizada = new RutaRealizadaDTO();
            rutaRealizada.setRuta(ruta);
            rutaRealizada.setUsuario(usuario);
            rutaRealizada.setFecha(LocalDate.parse(fecha));
            rutaRealizada.setTiempo(tiempo);

            // Guardar la entidad
            rutaRealizadaServicio.crearRutaRealizada(rutaRealizada);

            // Añadir mensaje de éxito
            redirectAttributes.addFlashAttribute("success", "Ruta realizada guardada con éxito.");
            return "listaRutasRealizadas";

        } catch (Exception e) {
            // Capturar cualquier excepción y redirigir con mensaje de error
            redirectAttributes.addFlashAttribute("error", "Error al guardar la ruta: " + e.getMessage());
            return "redirect:/hacerRuta";
        }
    }

}
