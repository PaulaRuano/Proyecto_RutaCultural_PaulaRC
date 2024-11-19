package proyecto.modelo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.UsuarioDTO;
/**
 * Interfaz DAO para el usuario
 * Contiene los prototipos de los métodos que usa UsuarioDTO
 * 
 * @author Paula Ruano
 */
public interface UsuarioDAO extends JpaRepository<UsuarioDTO, Integer>{
	 // Método para buscar un usuario por su id
	/** 
	 * @return UsuarioDTO
	 */
	Optional<UsuarioDTO> findById(int id);

	boolean existsByCorreo(String correo);
	
	Optional<UsuarioDTO> findBycorreo(String correo);
	
}