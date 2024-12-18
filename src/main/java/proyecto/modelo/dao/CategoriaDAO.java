package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.CategoriaDTO;

import java.util.Optional;

/**
 * Interfaz DAO para las categorías
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con CategoriaDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
public interface CategoriaDAO extends JpaRepository<CategoriaDTO, Long> {
	/**
	 * Busca una categoría por su nombre
	 * 
	 * @param nombreCategoria Nombre de la categoría
	 * @return Optional que contiene la categoría si existe
	 * 
	 * @see CategoriaServicio
	 */
	Optional<CategoriaDTO> findByNombreCategoria(String nombreCategoria);
}
