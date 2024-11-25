package proyecto.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPuntoInteresDTO;

public interface RutaPuntoInteresDAO extends JpaRepository<RutaPuntoInteresDTO, Integer> {
    // Puedes agregar métodos personalizados si necesitas búsquedas específicas
	@Query("SELECT rpi.puntoInteres FROM RutaPuntoInteresDTO rpi WHERE rpi.ruta.id = :rutaId")
    List<PuntoDeInteresDTO> findPuntosInteresByRutaId(@Param("rutaId") Integer rutaId);

}
