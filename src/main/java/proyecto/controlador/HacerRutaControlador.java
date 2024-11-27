package proyecto.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.DetallesUsuarioServicio;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.servicio.UsuarioServicio;

@Controller
public class HacerRutaControlador {

    private final RutaUsuarioServicio rutaUsuarioServicio;
    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;
    private final PuntoDeInteresServicio puntoDeInteresServicio;
    private final DetallesUsuarioServicio detallesUsuarioServicio;

    @Autowired
    public HacerRutaControlador(RutaUsuarioServicio rutaUsuarioServicio,
                                RutaPredeterminadaServicio rutaPredeterminadaServicio,
                                PuntoDeInteresServicio puntoDeInteresServicio,
                                DetallesUsuarioServicio detallesUsuarioServicio) {
        this.rutaUsuarioServicio = rutaUsuarioServicio;
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
        this.puntoDeInteresServicio = puntoDeInteresServicio;
        this.detallesUsuarioServicio = detallesUsuarioServicio;
    }

    @GetMapping("/hacerRuta")
    public String hacerRuta(@RequestParam Integer id, @RequestParam String tipo, Model model) {
    	
        UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();
        
        List<PuntoDeInteresDTO> puntos;

        if ("U".equals(tipo)) {
            RutaUsuarioDTO rutaUsuario = rutaUsuarioServicio.obtenerRutaPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ruta de usuario no encontrada."));
            puntos = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(
                    rutaUsuarioServicio.obtenerIdsPuntosDeRuta(rutaUsuario.getId())
            );
            model.addAttribute("ruta", rutaUsuario);
        } else if ("P".equals(tipo)) {
            RutaPredeterminadaDTO rutaPredeterminada = rutaPredeterminadaServicio.obtenerRutaPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ruta predeterminada no encontrada."));
            puntos = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(
                    rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(rutaPredeterminada.getId())
            );
            model.addAttribute("ruta", rutaPredeterminada);
        } else {
            throw new IllegalArgumentException("Tipo de ruta no válido.");
        }

        model.addAttribute("puntos", puntos);
        model.addAttribute("usuario", usuario); // Añadir el usuario al modelo
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
