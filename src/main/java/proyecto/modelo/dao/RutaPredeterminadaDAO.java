package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.RutaPredeterminadaDTO;
/**
 * Interfaz DAO para las rutas predeterminadas
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con RutaPredeterminadaDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
public interface RutaPredeterminadaDAO extends JpaRepository<RutaPredeterminadaDTO, Integer>{
}
