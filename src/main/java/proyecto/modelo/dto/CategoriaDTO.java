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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor		  // Constructor vacío
@AllArgsConstructor       // Constructor con todos los atributos
@RequiredArgsConstructor  // Constructor sin id
@Table(name = "categoria") //  Nombre de la tabla
public class CategoriaDTO {
	
	/** 
	 * Identificador único de la categoria
	 * No puede ser null
	 */
	@Id // Clave primaria	
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	/**
	 * Nombre de la categoría
	 * No puede ser null
	 */
	@NonNull
	@Column(nullable = false)
	private String nombreCategoria;
	
}
