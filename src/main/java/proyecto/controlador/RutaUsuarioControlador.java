package proyecto.controlador;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.servicio.RutaUsuarioServicio;
import proyecto.servicio.PuntoDeInteresServicio;

@Controller
public class RutaUsuarioControlador {
	private final PuntoDeInteresServicio puntoDeInteresServicio;
	  private final RutaUsuarioServicio rutaUsuarioServicio;

    @Autowired
    public RutaUsuarioControlador(PuntoDeInteresServicio puntoDeInteresServicio, RutaUsuarioServicio rutaUsuarioServicio) {
        this.puntoDeInteresServicio = puntoDeInteresServicio;
        this.rutaUsuarioServicio = rutaUsuarioServicio;
    }

    @GetMapping("/crearRutaUsuario")
    public String mostrarPuntos(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10; // Número de puntos por página
        List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosDeInteresPaginados(page, pageSize);

    
        boolean hasNextPage = puntos.size() == pageSize; // Verifica si hay más puntos disponibles

        model.addAttribute("puntos", puntos);
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNextPage", hasNextPage);
        return "crearRutaUsuario";
    }
    
    @ModelAttribute("puntosSeleccionados")
    public List<PuntoDeInteresDTO> puntosSeleccionados() {
        return new ArrayList<>();
    }

    @PostMapping("/guardarPuntosSeleccionados")
    @ResponseBody
    public String guardarPuntosSeleccionados(@RequestBody List<PuntoDeInteresDTO> puntos,
                                              @ModelAttribute("puntosSeleccionados") List<PuntoDeInteresDTO> puntosSeleccionados) {
        puntosSeleccionados.clear();
        puntosSeleccionados.addAll(puntos);
        return "Puntos seleccionados guardados en sesión";
    }

}
