package proyecto.modelo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.modelo.dto.MiRutaDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Interfaz DAO para las rutas guardadas por los usuarios (Mis Rutas)
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con MiRutaDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
@Repository
public interface MiRutaDAO extends JpaRepository<MiRutaDTO, Long> {

	/**
	 * Busca las rutas predeterminadas guardadas por un usuario específico
	 * 
	 * @param usuario Usuario que guarda las rutas
	 * @return Lista de rutas predeterminadas asociadas al usuario
	 * 
	 * @see MisRutasServicio
	 */
	@Query("SELECT m.ruta FROM MiRutaDTO m WHERE m.usuario = :usuario AND m.ruta.tipo = 'P'")
	List<RutaPredeterminadaDTO> findRutasPredeterminadasByUsuario(@Param("usuario") UsuarioDTO usuario);

	/**
	 * Comprueba si existe una relación entre una ruta predeterminada y un usuario
	 * 
	 * @param ruta Ruta predeterminada
	 * @param usuario Usuario que guarda la ruta
	 * @return true si existe la relación, false en caso contrario
	 * 
	 * @see MisRutasServicio
	 */
	@Query("SELECT COUNT(m) > 0 FROM MiRutaDTO m WHERE m.ruta = :ruta AND m.usuario = :usuario")
	boolean existsByRutaAndUsuario(@Param("ruta") RutaPredeterminadaDTO ruta, @Param("usuario") UsuarioDTO usuario);  

	/**
	 * Busca una relación específica entre un usuario y una ruta predeterminada
	 * 
	 * @param usuario Usuario que guarda la ruta
	 * @param ruta Ruta predeterminada
	 * @return Optional que contiene la relación si existe
	 * 
	 * @see MisRutasServicio
	 */
	@Query("SELECT m FROM MiRutaDTO m WHERE m.usuario = :usuario AND m.ruta = :ruta")
	Optional<MiRutaDTO> findByUsuarioAndRuta(@Param("usuario") UsuarioDTO usuario, @Param("ruta") RutaPredeterminadaDTO ruta);   
}