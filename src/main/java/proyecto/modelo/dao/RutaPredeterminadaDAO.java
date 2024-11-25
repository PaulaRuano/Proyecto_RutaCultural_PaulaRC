package proyecto.modelo.dao;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.RutaPredeterminadaDTO;

public interface RutaPredeterminadaDAO extends JpaRepository<RutaPredeterminadaDTO, Integer>{
	

}
