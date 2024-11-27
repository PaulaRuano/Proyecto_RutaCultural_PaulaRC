package proyecto.servicio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.MiRutaDAO;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dto.MiRutaDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;


@Service
public class MisRutasServicio {

    private final MiRutaDAO misRutasDAO;
    private final RutaPredeterminadaDAO rutaPredeterminadaDAO;

    @Autowired
    public MisRutasServicio(MiRutaDAO misRutasDAO, RutaPredeterminadaDAO rutaPredeterminadaDAO) {
        this.misRutasDAO = misRutasDAO;
        this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
    }
    
    public List<RutaPredeterminadaDTO> obtenerRutasPredeterminadasPorUsuario(UsuarioDTO usuario) {
        return misRutasDAO.findRutasPredeterminadasByUsuario(usuario);
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
}