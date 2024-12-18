package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.RutaRealizadaDTO;
/**
 * Interfaz DAO para las rutas realizadas
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con RutaRealizadaDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
public interface RutaRealizadaDAO extends JpaRepository<RutaRealizadaDTO, Integer>{
	
}
