package proyecto.modelo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.modelo.dto.MiRutaDTO;
import proyecto.modelo.dto.RutaPredeterminadaDTO;
import proyecto.modelo.dto.UsuarioDTO;
@Repository
public interface MiRutaDAO extends JpaRepository<MiRutaDTO, Long> {

    @Query("SELECT m.ruta FROM MiRutaDTO m WHERE m.usuario = :usuario AND m.ruta.tipo = 'P'")
    List<RutaPredeterminadaDTO> findRutasPredeterminadasByUsuario(@Param("usuario") UsuarioDTO usuario);

    @Query("SELECT COUNT(m) > 0 FROM MiRutaDTO m WHERE m.ruta = :ruta AND m.usuario = :usuario")
    boolean existsByRutaAndUsuario(@Param("ruta") RutaPredeterminadaDTO ruta, @Param("usuario") UsuarioDTO usuario);
    
    @Query("SELECT m FROM MiRutaDTO m WHERE m.usuario = :usuario AND m.ruta = :ruta")
    Optional<MiRutaDTO> findByUsuarioAndRuta(@Param("usuario") UsuarioDTO usuario, @Param("ruta") RutaPredeterminadaDTO ruta);

}