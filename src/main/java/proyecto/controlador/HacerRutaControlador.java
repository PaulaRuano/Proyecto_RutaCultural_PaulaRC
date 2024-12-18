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
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.DetallesUsuarioServicio;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
import proyecto.servicio.RutaUsuarioServicio;

/**
 * Controlador para gestionar la vista y la funcionalidad de "hacer una ruta"
 * 
 * Permite a los usuarios visualizar detalles de una ruta seleccionada, ya sea predeterminada
 * o creada por el usuario, junto con los puntos de interés asociados
 * 
 * @author Paula Ruano
 */
@Controller
public class HacerRutaControlador {
	// Variables locales
    private final RutaUsuarioServicio rutaUsuarioServicio;
    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;
    private final PuntoDeInteresServicio puntoDeInteresServicio;
    private final DetallesUsuarioServicio detallesUsuarioServicio;
    
    // Constructor de la clase
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

    /**
     * Muestra los detalles de una ruta mientras que el usuario la realiza
     * 
     * Identifica si la ruta es predeterminada o creada por el usuario,
     * obtiene los puntos de interés asociados y pasa la información a la vista.
     * 
     * @param id ID de la ruta
     * @param tipo Tipo de la ruta ("U" para usuario, "P" para predeterminada)
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la plantilla para la vista de "hacer ruta"
     */
    @GetMapping("/hacerRuta")
    public String hacerRuta(@RequestParam Integer id, @RequestParam String tipo, Model model) {
    	// Obtener el usuario autenticado
        UsuarioDTO usuario = detallesUsuarioServicio.obtenerUsuarioActual();
        
        List<PuntoDeInteresDTO> puntos;
     // Verificar el tipo de ruta y obtener los datos correspondientes
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
        
        // Pasar los datos al modelo para la vista
        model.addAttribute("puntos", puntos);
        model.addAttribute("usuario", usuario); 
        return "hacerRuta";
    } 
}
