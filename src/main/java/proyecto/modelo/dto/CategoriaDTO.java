package proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Clase DTO de las categorías de los puntos de interés
 * Contiene los atributos de categorias
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
@Table(name = "categoria") // Nombre de la tabla
public class CategoriaDTO {
	/** 
	 * Identificador único de la categoria
	 * No puede ser null
	 */
	@Id 	
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // Clave generada automáticamente, autoincremental
	private Long id;

	/**
	 * Nombre de la categoría
	 * No puede ser null
	 */
	@Column(nullable = false)
	private String nombreCategoria;
}