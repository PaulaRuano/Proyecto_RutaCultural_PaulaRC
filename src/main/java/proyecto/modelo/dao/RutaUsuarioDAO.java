package proyecto.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;
 @Repository
public interface RutaUsuarioDAO extends JpaRepository<RutaUsuarioDTO, Integer>{
	 List<RutaUsuarioDTO> findByUsuario(UsuarioDTO usuario);
}
