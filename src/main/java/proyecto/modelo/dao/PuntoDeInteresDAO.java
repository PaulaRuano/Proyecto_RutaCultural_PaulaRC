package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.modelo.dto.PuntoDeInteresDTO;

public interface PuntoDeInteresDAO extends JpaRepository<PuntoDeInteresDTO, Long> {
    // Puedes agregar métodos personalizados si es necesario
}
