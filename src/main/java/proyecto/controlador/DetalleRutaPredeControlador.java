package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;

import java.util.List;

@Controller
@RequestMapping("/detalleRutaPredeterminada")
public class DetalleRutaPredeControlador {

    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;
    private final PuntoDeInteresServicio puntoDeInteresServicio;

    @Autowired
    public DetalleRutaPredeControlador(RutaPredeterminadaServicio rutaPredeterminadaServicio, PuntoDeInteresServicio puntoDeInteresServicio) {
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
        this.puntoDeInteresServicio = puntoDeInteresServicio;
    }

    @GetMapping("/{id}")
    public String mostrarDetalle(@PathVariable("id") int id, Model model) {
        // Manejar el Optional devuelto por obtenerRutaPorId
        RutaPredeterminadaDTO ruta = rutaPredeterminadaServicio.obtenerRutaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ruta con ID " + id + " no encontrada."));

        // Obtener el primer punto de interés relacionado con la ruta
        List<Long> idsPuntos = rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(id);
        PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
                .findFirst()
                .orElse(null);

        // Generar la URL de la imagen del primer punto
        String imagenRuta = (primerPunto != null) ? "/img/puntosInteres/" + primerPunto.getId() + ".jpg" : "/img/ImgComunPI.png";

        // Pasar datos a la vista
        model.addAttribute("ruta", ruta);
        model.addAttribute("imagen", imagenRuta);

        return "detalleRutaPredeterminada"; // Nombre del archivo HTML
    }

}
