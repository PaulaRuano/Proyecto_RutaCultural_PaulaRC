package proyecto.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dao.RutaRealizadaDAO;
import proyecto.modelo.dao.RutaUsuarioDAO;
import proyecto.modelo.dto.RutaDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.RutaRealizadaDTO;
import proyecto.modelo.dto.RutaUsuarioDTO;

@Service
public class RutaRealizadaServicio {
	  private final RutaRealizadaDAO rutaRealizadaDAO;
	    private final RutaUsuarioDAO rutaUsuarioDAO;
	    private final RutaPredeterminadaDAO rutaPredeterminadaDAO;

	    @Autowired
	    public RutaRealizadaServicio(RutaRealizadaDAO rutaRealizadaDAO, 
	                                 RutaUsuarioDAO rutaUsuarioDAO,
	                                 RutaPredeterminadaDAO rutaPredeterminadaDAO) {
	        this.rutaRealizadaDAO = rutaRealizadaDAO;
	        this.rutaUsuarioDAO = rutaUsuarioDAO;
	        this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
	    }

	    /**
	     * Crea y guarda una nueva ruta realizada en la base de datos.
	     *
	     * @param rutaRealizadaDTO el DTO de la ruta realizada a guardar
	     * @return la ruta realizada guardada en la base de datos
	     */

	    @Transactional
	    public void crearRutaRealizada(RutaRealizadaDTO rutaRealizada) {	    	
	        rutaRealizadaDAO.save(rutaRealizada); // Guardar directamente el objeto recibido
	    }

	    public List<Map<String, Object>> obtenerRutasConTitulos() {
	        List<RutaRealizadaDTO> rutasRealizadas = rutaRealizadaDAO.findAll();

	        return rutasRealizadas.stream().map(rutaRealizada -> {
	            Map<String, Object> rutaMap = new HashMap<>();
	            RutaDTO ruta = rutaRealizada.getRuta();

	            String titulo = null;
	            if (ruta instanceof RutaUsuarioDTO) {
	                titulo = ((RutaUsuarioDTO) ruta).getNombre();
	            } else if (ruta instanceof RutaPredeterminadaDTO) {
	                titulo = ((RutaPredeterminadaDTO) ruta).getNombre();
	            } else {
	                titulo = "Título desconocido";
	            }

	            rutaMap.put("titulo", titulo);
	            rutaMap.put("fecha", rutaRealizada.getFecha());
	            rutaMap.put("tiempo", rutaRealizada.getTiempo());

	            return rutaMap;
	        }).toList();
	    }
	    

	    public List<RutaRealizadaDTO> obtenerRutasRealizadasPorUsuario(int usuarioId) {
	        return rutaRealizadaDAO.findAll().stream()
	                .filter(ruta -> ruta.getUsuario().getId() == usuarioId)
	                .toList();
	    }

}
