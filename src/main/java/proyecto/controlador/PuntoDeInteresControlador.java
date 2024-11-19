package proyecto.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.servicio.PuntoDeInteresServicio;

@RestController
@RequestMapping("/api/")  
@Slf4j
public class PuntoDeInteresControlador {

    @Autowired
    private final PuntoDeInteresServicio puntoDeInteresServicio;

    @Autowired
    public PuntoDeInteresControlador(PuntoDeInteresServicio puntoDeInteresServicio) {
        this.puntoDeInteresServicio = puntoDeInteresServicio;
    }

    @GetMapping("/puntos-de-interes")
    public ResponseEntity<List<PuntoDeInteresDTO>> listarPuntosDeInteres() {
        try {
            List<PuntoDeInteresDTO> puntosDeInteres = puntoDeInteresServicio.obtenerPuntosDeInteres();
            return ResponseEntity.ok(puntosDeInteres);
        } catch (Exception e) {
            log.error("Error al listar puntos de interés: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/puntos-de-interes/{id}")
    public ResponseEntity<PuntoDeInteresDTO> obtenerPuntoDeInteresPorId(@PathVariable Long id) {
        try {
            PuntoDeInteresDTO punto = puntoDeInteresServicio.obtenerPuntoPorId(id);
            if (punto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(punto);
        } catch (Exception e) {
            log.error("Error al obtener el punto de interés con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}