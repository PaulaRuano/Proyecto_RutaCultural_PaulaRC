package proyecto.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
import proyecto.servicio.RutaUsuarioServicio;

@Controller
public class HacerRutaControlador {

    private final RutaUsuarioServicio rutaUsuarioServicio;
    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;
    private final PuntoDeInteresServicio puntoDeInteresServicio;

    @Autowired
    public HacerRutaControlador(RutaUsuarioServicio rutaUsuarioServicio,
                                RutaPredeterminadaServicio rutaPredeterminadaServicio,
                                PuntoDeInteresServicio puntoDeInteresServicio) {
        this.rutaUsuarioServicio = rutaUsuarioServicio;
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
        this.puntoDeInteresServicio = puntoDeInteresServicio;
    }

    @GetMapping("/hacerRuta")
    public String hacerRuta(@RequestParam Integer id, @RequestParam String tipo, Model model) {
        if ("U".equals(tipo)) {
            // Rutas de usuario
            RutaUsuarioDTO rutaUsuario = rutaUsuarioServicio.obtenerRutaPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ruta de usuario no encontrada."));
            
            cargarVistaHacerRutaUsuario(model, rutaUsuario);
        } else if ("P".equals(tipo)) {
            // Rutas predeterminadas
            RutaPredeterminadaDTO rutaPredeterminada = rutaPredeterminadaServicio.obtenerRutaPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ruta predeterminada no encontrada."));
            
            cargarVistaHacerRutaPredeterminada(model, rutaPredeterminada);
        } else {
            throw new IllegalArgumentException("Tipo de ruta no válido.");
        }

        return "hacerRuta";
    }

    private void cargarVistaHacerRutaUsuario(Model model, RutaUsuarioDTO ruta) {
        // Obtener los puntos de interés relacionados con la ruta
        List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(
                rutaUsuarioServicio.obtenerIdsPuntosDeRuta(ruta.getId())
        );

        // Pasar la información a la vista
        model.addAttribute("ruta", ruta);
        model.addAttribute("puntos", puntos);
    }

    private void cargarVistaHacerRutaPredeterminada(Model model, RutaPredeterminadaDTO ruta) {
        // Obtener los puntos de interés relacionados con la ruta
        List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(
                rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(ruta.getId())
        );

        // Pasar la información a la vista
        model.addAttribute("ruta", ruta);
        model.addAttribute("puntos", puntos);
    }
}
