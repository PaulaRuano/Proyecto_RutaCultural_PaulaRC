package proyecto.controlador;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/puntos")
    public ResponseEntity<List<PuntoDeInteresDTO>> obtenerPuntosPorMunicipio(
            @RequestParam String municipio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio
                    .obtenerPuntosDeInteresPorMunicipioPaginados(municipio, page, size);
            return ResponseEntity.ok(puntos);
        } catch (Exception e) {
            log.error("Error al obtener puntos por municipio: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/municipios")
    public ResponseEntity<List<String>> obtenerMunicipiosPorPrefijo(@RequestParam String sugerencia) {
        try {
            log.info("Buscando municipios con prefijo: {}", sugerencia); // Agregar log
            List<String> municipios = puntoDeInteresServicio.obtenerMunicipiosPorPrefijo(sugerencia);
            log.info("Municipios encontrados: {}", municipios); // Agregar log
            return ResponseEntity.ok(municipios);
        } catch (Exception e) {
            log.error("Error al obtener municipios por prefijo: ", e); // Agregar log del error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}