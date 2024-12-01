package proyecto.servicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.MiRutaDAO;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dto.MiRutaDTO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;


@Service
public class MisRutasServicio {
	  private final MiRutaDAO misRutasDAO;
	    private final RutaPredeterminadaDAO rutaPredeterminadaDAO;
	    private final RutaPuntoInteresDAO rutaPuntoInteresDAO; // Nuevo DAO

	    @Autowired
	    public MisRutasServicio(MiRutaDAO misRutasDAO, RutaPredeterminadaDAO rutaPredeterminadaDAO, RutaPuntoInteresDAO rutaPuntoInteresDAO) {
	        this.misRutasDAO = misRutasDAO;
	        this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
	        this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
	    }
	    
    @Transactional
    public void guardarMisRutas(Integer rutaId, UsuarioDTO usuario) {
        // Verificar que la ruta predeterminada existe
        RutaPredeterminadaDTO ruta = rutaPredeterminadaDAO.findById(rutaId)
                .orElseThrow(() -> new IllegalArgumentException("La ruta predeterminada no existe."));

        // Verificar si ya está en Mis Rutas
        if (misRutasDAO.existsByRutaAndUsuario(ruta, usuario)) {
            throw new IllegalStateException("Esta ruta ya está guardada en 'Mis Rutas'.");
        }

        // Crear y guardar la relación en Mis Rutas
        MiRutaDTO misRutas = new MiRutaDTO();
        misRutas.setRuta(ruta);
        misRutas.setUsuario(usuario);
        misRutas.setFecha(LocalDate.now());

        misRutasDAO.save(misRutas);
    }

    public List<Map<String, Object>> obtenerRutasPredeterminadasConDetalles(UsuarioDTO usuario) {
        // Obtener rutas predeterminadas
        List<RutaPredeterminadaDTO> rutas = misRutasDAO.findRutasPredeterminadasByUsuario(usuario);

        // Enriquecer con fecha de MiRutaDTO
        return rutas.stream().map(ruta -> {
            Map<String, Object> rutaMap = new HashMap<>();
            rutaMap.put("ruta", ruta);

            // Obtener fecha de la relación MiRutaDTO
            LocalDate fechaAdicion = misRutasDAO
                .findByUsuarioAndRuta(usuario, ruta)
                .map(MiRutaDTO::getFecha)
                .orElse(null);
            rutaMap.put("fecha", fechaAdicion);

            // Obtener imagen del primer punto de la ruta
            List<Long> idsPuntos = obtenerIdsPuntosDeRuta(ruta.getId());
            if (!idsPuntos.isEmpty()) {
                rutaMap.put("imagen", "/img/puntosInteres/" + idsPuntos.get(0) + ".jpg");
            } else {
                rutaMap.put("imagen", "/img/ImgComunPi.png"); // Imagen por defecto
            }
            return rutaMap;
        }).toList();
    }

    
    public List<Long> obtenerIdsPuntosDeRuta(Integer rutaId) {
        return rutaPuntoInteresDAO.findPuntosInteresByRutaId(rutaId)
                                  .stream()
                                  .map(PuntoDeInteresDTO::getId) // Obtener los IDs de los puntos
                                  .collect(Collectors.toList());
    }
}