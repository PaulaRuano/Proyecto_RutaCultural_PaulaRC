package proyecto.controlador;

import java.util.Arrays;
import java.util.List;
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
public class RutasPredeterminadasControlador {

    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;

    @Autowired
    public RutasPredeterminadasControlador(RutaPredeterminadaServicio rutaPredeterminadaServicio) {
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
    }

    @GetMapping("/listaRutasPredeterminadas")
    public String mostrarRutas(Model model) {
        // Obtener todas las rutas predeterminadas
        List<RutaPredeterminadaDTO> rutas = rutaPredeterminadaServicio.obtenerTodasLasRutas();

        // Pasar las rutas al modelo para que la vista las pueda renderizar
        model.addAttribute("rutas", rutas);

        // Retornar la vista
        return "listaRutasPredeterminadas"; // Nombre del archivo HTML
    }
}
