package proyecto.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.modelo.dto.MisRutasDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;
@Repository
public interface MisRutasDAO extends JpaRepository<MisRutasDTO, Long> {

    @Query("SELECT m.ruta FROM MisRutasDTO m WHERE m.usuario = :usuario AND m.ruta.tipo = 'P'")
    List<RutaPredeterminadaDTO> findRutasPredeterminadasByUsuario(@Param("usuario") UsuarioDTO usuario);

    @Query("SELECT COUNT(m) > 0 FROM MisRutasDTO m WHERE m.ruta = :ruta AND m.usuario = :usuario")
    boolean existsByRutaAndUsuario(@Param("ruta") RutaPredeterminadaDTO ruta, @Param("usuario") UsuarioDTO usuario);
}