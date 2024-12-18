package proyecto.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import proyecto.modelo.dto.PuntoDeInteresDTO;
import proyecto.modelo.dto.RutaPuntoInteresDTO;

/**
 * Interfaz DAO para la tabla que relaciona rutas y puntos de interés
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con RutaPuntoInteresDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
public interface RutaPuntoInteresDAO extends JpaRepository<RutaPuntoInteresDTO, Integer> {

	/**
	 * Busca todos los puntos de interés asociados a una ruta específica
	 * 
	 * @param rutaId ID de la ruta
	 * @return Lista de puntos de interés asociados a la ruta
	 * 
	 * @see MisRutasServicio
	 * @see RutaPredeterminadaServicio
	 * @see RutaUsuarioServicio
	 */
	@Query("SELECT rpi.puntoInteres FROM RutaPuntoInteresDTO rpi WHERE rpi.ruta.id = :rutaId")
	List<PuntoDeInteresDTO> findPuntosInteresByRutaId(@Param("rutaId") Integer rutaId);

	/**
	 * Elimina todas las relaciones de puntos de interés asociadas a una ruta específica
	 * 
	 * @param rutaId ID de la ruta
	 * 
	 * @see RutaUsuarioServicio
	 */
	@Modifying
	@Query("DELETE FROM RutaPuntoInteresDTO rpi WHERE rpi.ruta.id = :rutaId")
	void deleteByRutaId(@Param("rutaId") int rutaId);
}
