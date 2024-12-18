package proyecto.modelo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.UsuarioDTO;
/**
 * Interfaz DAO para el usuario
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con UsuarioDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
public interface UsuarioDAO extends JpaRepository<UsuarioDTO, Integer>{
	/**
	 * Busca un usuario por su correo electrónico
	 * 
	 * @param correo Correo del usuario
	 * @return Optional que contiene el UsuarioDTO si se encuentra
	 * 
	 * @see DetallesUsuariosServicio
	 * @see UsuarioServicio
	 */
	Optional<UsuarioDTO> findBycorreo(String correo);	
}