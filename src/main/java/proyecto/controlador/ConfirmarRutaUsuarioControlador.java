package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.List;

import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPuntoInteresDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.DetallesUsuarioServicio;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaUsuarioServicio;

@Controller
public class ConfirmarRutaUsuarioControlador {

    private final PuntoDeInteresServicio puntoDeInteresServicio;
    private final RutaUsuarioServicio rutaUsuarioServicio;
    private final DetallesUsuarioServicio usuarioDetallesServicio;
    private final RutaPuntoInteresDAO rutaPuntoInteresDAO;

    @Autowired
    public ConfirmarRutaUsuarioControlador(PuntoDeInteresServicio puntoDeInteresServicio,
                                           RutaUsuarioServicio rutaUsuarioServicio,
                                           DetallesUsuarioServicio usuarioDetallesServicio,
                                           RutaPuntoInteresDAO rutaPuntoInteresDAO) {
        this.puntoDeInteresServicio = puntoDeInteresServicio;
        this.rutaUsuarioServicio = rutaUsuarioServicio;
        this.usuarioDetallesServicio = usuarioDetallesServicio;
        this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
    }

    @PostMapping("/confirmarRutaUsuario")
    public String confirmarRuta(
            @RequestParam List<Long> puntoIds,     
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validar que al menos hay 2 puntos seleccionados
        if (puntoIds == null || puntoIds.size() < 2) {
            redirectAttributes.addFlashAttribute("error", "Debes seleccionar al menos 2 puntos para crear una ruta.");
            return "redirect:/crearRutaUsuario";
        }

        // Obtener los puntos seleccionados
        List<PuntoDeInteresDTO> puntosSeleccionados = puntoDeInteresServicio.obtenerPuntosPorIds(puntoIds);

        // Validar que todos los puntos pertenezcan al mismo municipio
        String municipio = puntosSeleccionados.get(0).getLocalidad();
        boolean mismoMunicipio = puntosSeleccionados.stream()
                .allMatch(p -> p.getLocalidad().equalsIgnoreCase(municipio));

        if (!mismoMunicipio) {
            redirectAttributes.addFlashAttribute("error", "Todos los puntos seleccionados deben pertenecer al mismo municipio.");
            return "redirect:/crearRutaUsuario";
        }

        // Agregar los puntos al modelo
        model.addAttribute("puntos", puntosSeleccionados); 

        // Devolver el nombre de la vista para confirmar la ruta
        return "confirmarRutaUsuario";
    }

    @PostMapping("/guardarRutaUsuario")
    public String guardarRutaUsuario(@RequestParam String duracion,
                                     @RequestParam int distancia,
                                     @RequestParam String municipio,
                                     @RequestParam String nombreRuta,
                                     @RequestParam List<Long> puntoIds,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        try {
            // Validar si se han seleccionado puntos
            if (puntoIds == null || puntoIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos 2 puntos.");
                return "crearRutaUsuario";
            }
            
            // Validar si el nombre de la ruta está vacío
            if (nombreRuta == null || nombreRuta.trim().isEmpty()) {
                model.addAttribute("error", "Introduce un nombre para la ruta, por favor.");
                List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosPorIds(puntoIds); // Recuperar los puntos seleccionados
                model.addAttribute("puntos", puntos); // Agregar puntos al modelo
                return "confirmarRutaUsuario"; // Retornar la vista sin redirección
            }

            // Obtener el usuario autenticado
            UsuarioDTO usuario = usuarioDetallesServicio.obtenerUsuarioActual();

            // Verificar los puntos en la base de datos, y crear si no existen
            List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerOCrearPuntosDeInteres(puntoIds);

            // Crear la ruta de usuario
            RutaUsuarioDTO nuevaRuta = rutaUsuarioServicio.crearRutaUsuario(nombreRuta, Double.parseDouble(duracion), distancia, municipio, usuario);

            // Crear registros en la tabla intermedia ruta_punto_interes
            for (PuntoDeInteresDTO punto : puntos) {
                RutaPuntoInteresDTO relacion = new RutaPuntoInteresDTO();
                relacion.setRuta(nuevaRuta);
                relacion.setPuntoInteres(punto);
                rutaPuntoInteresDAO.save(relacion); // Guardar relación en la tabla intermedia
            }

            model.addAttribute("success", "Ruta creada con éxito.");
            return "fragmentos/modalRutaUsuarioCreada"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la ruta: " + e.getMessage());
            return "confirmarRutaUsuario";
        }
    }

}
