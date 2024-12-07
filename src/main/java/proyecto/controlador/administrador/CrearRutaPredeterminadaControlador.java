package proyecto.controlador.administrador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.transaction.Transactional;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.servicio.PuntoDeInteresServicio;
import proyecto.servicio.RutaPredeterminadaServicio;

@Controller
public class CrearRutaPredeterminadaControlador {

    private final PuntoDeInteresServicio puntoDeInteresServicio;
    private final RutaPredeterminadaServicio rutaPredeterminadaServicio;

    @Autowired
    public CrearRutaPredeterminadaControlador(PuntoDeInteresServicio puntoDeInteresServicio, 
                                      RutaPredeterminadaServicio rutaPredeterminadaServicio) {
        this.puntoDeInteresServicio = puntoDeInteresServicio;
        this.rutaPredeterminadaServicio = rutaPredeterminadaServicio;
    }
    
    @PostMapping("/crearRutaPredeterminada")
    @PreAuthorize("hasRole('ADMIN')")
    public String procesarFormulario(@RequestParam String nombreRuta,
                                      @RequestParam List<String> puntoInteresIds,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            // Validar y convertir los IDs
            List<Long> puntoIds = new ArrayList<>();
            for (String id : puntoInteresIds) {
                if (id == null || id.trim().isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Los campos de ID no pueden estar vacíos.");
                    return "redirect:/administradorVista"; // Opcionalmente puedes redirigir
                }
                try {
                    puntoIds.add(Long.parseLong(id)); // Intentar convertir a Long
                } catch (NumberFormatException e) {
                    model.addAttribute("error", "Todos los IDs de puntos de interés deben ser números, no caractéres.");
                    model.addAttribute("nombreRuta", nombreRuta); // Mantener los valores ingresados
                    model.addAttribute("puntoInteresIds", puntoInteresIds);
                    return "administradorVista"; // Volver a la vista con el error
                }
            }

            // Obtener o crear los puntos de interés
            List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerOCrearPuntosDeInteres(puntoIds);

            // Validar que se obtuvieron todos los puntos necesarios
            if (puntos.size() != puntoIds.size()) {
                model.addAttribute("error", "Algunos puntos de interés no pudieron ser procesados.");
                return "administradorVista";
            }

            // Calcular distancia y duración
            Map<String, Object> calculos = rutaPredeterminadaServicio.calcularDistanciaYDuracion(puntos);
            double duracion = (int) calculos.get("duration") / 60.0; // Convertir a minutos
            int distancia = (int) calculos.get("distance"); // En metros

            // Crear la ruta predeterminada
            rutaPredeterminadaServicio.crearRutaPredeterminada(
                    nombreRuta, puntos, duracion, distancia, puntos.get(0).getLocalidad()
            );

            redirectAttributes.addFlashAttribute("success", "Ruta creada con éxito.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear la ruta: " + e.getMessage());
            return "administradorVista";
        }

        return "redirect:/administradorVista";
    }
}