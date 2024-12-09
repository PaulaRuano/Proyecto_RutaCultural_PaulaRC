package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;

import java.util.List;
@Controller
@RequestMapping("/detalleRuta")
public class DetalleRutaControlador {

    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;
    private final RutaUsuarioServicio rutaUsuarioServicio;
    private final PuntoDeInteresServicio puntoDeInteresServicio;

    @Autowired
    public DetalleRutaControlador(RutaPredeterminadaServicio rutaPredeterminadaServicio, 
                                  RutaUsuarioServicio rutaUsuarioServicio, 
                                  PuntoDeInteresServicio puntoDeInteresServicio) {
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
        this.rutaUsuarioServicio = rutaUsuarioServicio;
        this.puntoDeInteresServicio = puntoDeInteresServicio;
    }

    @GetMapping("/predeterminada/{id}")
    public String mostrarDetalleRutaPredeterminada(@PathVariable("id") int id,
                                                   @RequestParam(value = "origen", required = false, defaultValue = "listaRutasPredeterminadas") String origen,
                                                   Model model) {
        // Obtener la ruta predeterminada
        RutaPredeterminadaDTO ruta = rutaPredeterminadaServicio.obtenerRutaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ruta con ID " + id + " no encontrada."));

        // Obtener puntos de interés
        List<Long> idsPuntos = rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(id);
        PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
                .findFirst()
                .orElse(null);

        // Configurar la imagen
        String imagenRuta = (primerPunto != null) ? "/img/puntosInteres/" + primerPunto.getId() + ".jpg" : "/img/ImgComunPI.png";
        List<PuntoDeInteresDTO> puntosDeInteres = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos);

        // Añadir datos al modelo
        model.addAttribute("ruta", ruta);
        model.addAttribute("imagen", imagenRuta);
        model.addAttribute("puntosDeInteres", puntosDeInteres);
        model.addAttribute("origen", origen); // Pasar el origen para determinar el enlace correcto

        return "detalleRutaPredeterminada";
    }


    @GetMapping("/usuario/{id}")
    public String mostrarDetalleRutaUsuario(@PathVariable("id") int id, Model model) {
        // Nueva lógica para rutas de usuario
        RutaUsuarioDTO ruta = rutaUsuarioServicio.obtenerRutaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ruta con ID " + id + " no encontrada."));

        List<Long> idsPuntos = rutaUsuarioServicio.obtenerIdsPuntosDeRuta(id);
        PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
                .findFirst()
                .orElse(null);

        String imagenRuta = (primerPunto != null) ? "/img/puntosInteres/" + primerPunto.getId() + ".jpg" : "/img/ImgComunPI.png";
        List<PuntoDeInteresDTO> puntosDeInteres = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos);

        model.addAttribute("ruta", ruta);
        model.addAttribute("imagen", imagenRuta);
        model.addAttribute("puntosDeInteres", puntosDeInteres);

        return "detalleRutaUsuario";
    }
}
