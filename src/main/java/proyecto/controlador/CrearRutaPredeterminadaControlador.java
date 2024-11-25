package proyecto.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
                                      @RequestParam List<Long> puntoInteresIds,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Validar el nombre de la ruta
            if (nombreRuta == null || nombreRuta.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre de la ruta no puede estar vacío.");
                return "redirect:/administradorVista";
            }

            // Validar que haya al menos 4 puntos de interés
            if (puntoInteresIds.size() < 4) {
                redirectAttributes.addFlashAttribute("error", "Debe haber al menos 4 puntos de interés.");
                return "redirect:/administradorVista";
            }

            // Obtener o crear los puntos de interés
            List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerOCrearPuntosDeInteres(puntoInteresIds);

            // Validar que se obtuvieron todos los puntos necesarios
            if (puntos.size() != puntoInteresIds.size()) {
                redirectAttributes.addFlashAttribute("error", "Algunos puntos de interés no pudieron ser procesados.");
                return "redirect:/administradorVista";
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
            redirectAttributes.addFlashAttribute("error", "Error al crear la ruta: " + e.getMessage());
        }

        return "redirect:/administradorVista";
    }

}
