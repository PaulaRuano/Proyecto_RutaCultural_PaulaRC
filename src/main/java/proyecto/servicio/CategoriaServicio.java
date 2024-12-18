package proyecto.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.modelo.dao.CategoriaDAO;
import proyecto.modelo.dto.CategoriaDTO;

/**
 * Servicio para gestionar la lógica relacionada con las categorías
 * 
 * @author Paula Ruano
 */
@Service
public class CategoriaServicio {
	// Variable local de la clase
	private final CategoriaDAO categoriaDAO;

	// Constructor de la clase
	@Autowired
	public CategoriaServicio(CategoriaDAO categoriaDAO) {
		this.categoriaDAO = categoriaDAO;
	}

	/**
	 * Obtiene una categoría basada en una coincidencia parcial con el nombre del organismo.
	 * 
	 * Si no se encuentra ninguna coincidencia, se retorna la categoría con nombre "Otros".
	 * 
	 * @param nombreOrganismo Nombre del organismo utilizado para buscar coincidencias
	 * @return CategoriaDTO La categoría encontrada o la categoría por defecto ("Otros")
	 * @throws RuntimeException Si la categoría por defecto "Otros" no existe en la base de datos
	 * 
	 * @see PuntoDeInteresServicio
	 */
	public CategoriaDTO obtenerCategoriaPorNombre(String nombreOrganismo) {
		// Verificar si el nombre del organismo es nulo o vacío
		if (nombreOrganismo == null || nombreOrganismo.isEmpty()) {
			 // Retornar la categoría por defecto "Otros"
			return categoriaDAO.findByNombreCategoria("Otros")
					.orElseThrow(() -> new RuntimeException("Categoría 'Otros' no encontrada"));
		}

		// Buscar coincidencias en el nombre del organismo
		Optional<CategoriaDTO> categoria = categoriaDAO.findAll().stream()
				.filter(cat -> nombreOrganismo.toLowerCase().contains(cat.getNombreCategoria().toLowerCase()))
				.findFirst();

	    // Retornar la categoría encontrada o la categoría por defecto "Otros" si no hay coincidencias
		return categoria.orElseGet(() -> categoriaDAO.findByNombreCategoria("Otros")
				.orElseThrow(() -> new RuntimeException("Categoría 'Otros' no encontrada")));
	}
}