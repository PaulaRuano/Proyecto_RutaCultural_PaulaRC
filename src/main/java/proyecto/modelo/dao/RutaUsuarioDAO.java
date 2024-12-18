package proyecto.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Interfaz DAO para las rutas creadas por usuarios
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con RutaUsuarioDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
@Repository
public interface RutaUsuarioDAO extends JpaRepository<RutaUsuarioDTO, Integer>{
	/**
	 * Busca todas las rutas creadas por un usuario específico
	 * 
	 * @param usuario Usuario que creó las rutas
	 * @return Lista de rutas creadas por el usuario
	 * 
	 * @see RutaUsuarioServicio
	 */
	List<RutaUsuarioDTO> findByUsuario(UsuarioDTO usuario);
}
