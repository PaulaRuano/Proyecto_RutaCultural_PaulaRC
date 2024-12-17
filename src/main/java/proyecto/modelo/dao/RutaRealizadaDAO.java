package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import proyecto.modelo.dto.RutaRealizadaDTO;

public interface RutaRealizadaDAO extends JpaRepository<RutaRealizadaDTO, Integer>{
	
}
