package proyecto.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.modelo.dao.CategoriaDAO;
import proyecto.modelo.dto.CategoriaDTO;

@Service
public class CategoriaServicio {

    private final CategoriaDAO categoriaDAO;

    @Autowired
    public CategoriaServicio(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Busca una categoría por coincidencia parcial en el nombre.
     * Si no se encuentra, retorna la categoría "Otros".
     */
    public CategoriaDTO obtenerCategoriaPorNombre(String nombreOrganismo) {
        if (nombreOrganismo == null || nombreOrganismo.isEmpty()) {
            return categoriaDAO.findByNombreCategoria("Otros")
                    .orElseThrow(() -> new RuntimeException("Categoría 'Otros' no encontrada"));
        }

        // Busca palabras clave en el nombre del organismo
        Optional<CategoriaDTO> categoria = categoriaDAO.findAll().stream()
                .filter(cat -> nombreOrganismo.toLowerCase().contains(cat.getNombreCategoria().toLowerCase()))
                .findFirst();

        return categoria.orElseGet(() -> categoriaDAO.findByNombreCategoria("Otros")
                .orElseThrow(() -> new RuntimeException("Categoría 'Otros' no encontrada")));
    }

}