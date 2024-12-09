package proyecto.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import proyecto.modelo.dto.RutaUsuarioDTO;
import proyecto.modelo.dto.UsuarioDTO;

 @Repository
public interface RutaUsuarioDAO extends JpaRepository<RutaUsuarioDTO, Integer>{
	 List<RutaUsuarioDTO> findByUsuario(UsuarioDTO usuario);
	  // Método para eliminar relaciones basadas en el ID de la ruta
	    @Modifying
	    @Query("DELETE FROM RutaPuntoInteresDTO rpi WHERE rpi.ruta.id = :rutaId")
	    void deleteByRutaId(@Param("rutaId") int rutaId);
}
