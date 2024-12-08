package proyecto.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.RutaPuntoInteresDAO;
import proyecto.modelo.dao.RutaUsuarioDAO;
import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;

@Service
public class RutaUsuarioServicio {

    private final RutaUsuarioDAO rutaUsuarioDAO;
    private final RutaPuntoInteresDAO rutaPuntoInteresDAO;

    @Autowired
    public RutaUsuarioServicio(RutaUsuarioDAO rutaUsuarioDAO,  RutaPuntoInteresDAO rutaPuntoInteresDAO) {
        this.rutaUsuarioDAO = rutaUsuarioDAO;
        this.rutaPuntoInteresDAO = rutaPuntoInteresDAO;
    }

    @Transactional
    public RutaUsuarioDTO crearRutaUsuario(String nombre, Double duracion, int distancia, 
                                           String municipio, UsuarioDTO usuario) {
        RutaUsuarioDTO nuevaRuta = new RutaUsuarioDTO(nombre, duracion, distancia, municipio, usuario);
        nuevaRuta.setFechaCreacion(LocalDate.now()); // Fecha actual
        return rutaUsuarioDAO.save(nuevaRuta); // Guardar la ruta
    }
    
    public List<RutaUsuarioDTO> obtenerRutasPorUsuario(UsuarioDTO usuario) {
        return rutaUsuarioDAO.findByUsuario(usuario);
    }
    
    public List<Long> obtenerIdsPuntosDeRuta(Integer rutaId) {
        return rutaPuntoInteresDAO.findPuntosInteresByRutaId(rutaId)
                                  .stream()
                                  .map(PuntoDeInteresDTO::getId)
                                  .collect(Collectors.toList());
    }
    
    public Optional<RutaUsuarioDTO> obtenerRutaPorId(Integer id) {
        return rutaUsuarioDAO.findById(id);
    }
    
    @Transactional
    public void eliminarRutaUsuario(int id) {
        // Verificar si la ruta existe
        RutaUsuarioDTO ruta = rutaUsuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("La ruta con ID " + id + " no existe."));

        // Eliminar relaciones en la tabla intermedia
        rutaPuntoInteresDAO.deleteByRutaId(id);

        // Eliminar la ruta
        rutaUsuarioDAO.deleteById(id);
    }
}
