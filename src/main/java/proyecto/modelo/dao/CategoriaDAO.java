package proyecto.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.modelo.dto.CategoriaDTO;

import java.util.Optional;

public interface CategoriaDAO extends JpaRepository<CategoriaDTO, Long> {

    Optional<CategoriaDTO> findByNombreCategoria(String nombreCategoria);
}
