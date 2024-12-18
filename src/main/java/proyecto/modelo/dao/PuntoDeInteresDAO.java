package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.modelo.dto.PuntoDeInteresDTO;
/**
 * Interfaz DAO para los puntos de interés
 * Proporciona los prototipos de los métodos para acceder y gestionar datos relacionados con PuntoDeInteresDTO
 * Extiende JpaRepository para proporcionar las operaciones CRUD
 * 
 * @author Paula Ruano
 */
public interface PuntoDeInteresDAO extends JpaRepository<PuntoDeInteresDTO, Long> {
    
}
