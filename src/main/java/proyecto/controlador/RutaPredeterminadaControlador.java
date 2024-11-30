package proyecto.controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;
@Controller
public class RutaPredeterminadaControlador {

    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;  
    private final PuntoDeInteresServicio puntoDeInteresServicio;

    @Autowired
    public RutaPredeterminadaControlador(RutaPredeterminadaServicio rutaPredeterminadaServicio, PuntoDeInteresServicio puntoDeInteresServicio) {
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
        this.puntoDeInteresServicio = puntoDeInteresServicio;
    }
    @GetMapping("/listaRutasPredeterminadas")
    public String mostrarRutas(Model model) {
        // Obtener todas las rutas predeterminadas
        List<RutaPredeterminadaDTO> rutas = rutaPredeterminadaServicio.obtenerTodasLasRutas();

        // Crear una lista para pasar a la vista
        List<Map<String, Object>> rutasConImagen = new ArrayList<>();

        for (RutaPredeterminadaDTO ruta : rutas) {
            Map<String, Object> rutaConImagen = new HashMap<>();
            rutaConImagen.put("ruta", ruta);

            // Obtener los IDs de los puntos relacionados con la ruta
            List<Long> idsPuntos = rutaPredeterminadaServicio.obtenerIdsPuntosDeRuta(ruta.getId());
            if (!idsPuntos.isEmpty()) {
                // Obtener el primer punto de interés
                PuntoDeInteresDTO primerPunto = puntoDeInteresServicio.obtenerPuntosPorIdsDesdeBBDD(idsPuntos).stream()
                        .findFirst()
                        .orElse(null);

                // Generar la URL de la imagen
                if (primerPunto != null) {
                    rutaConImagen.put("imagen", "/img/puntosInteres/" + primerPunto.getId() + ".jpg");
                } else {
                    rutaConImagen.put("imagen", "/img/ImgComunPi.png"); // Imagen por defecto
                }
            } else {
                rutaConImagen.put("imagen", "/img/ImgComunPi.png"); // Imagen por defecto si no hay IDs
            }

            rutasConImagen.add(rutaConImagen);
        }

        model.addAttribute("rutas", rutasConImagen);

        return "listaRutasPredeterminadas"; // Nombre del archivo HTML
    }

}
