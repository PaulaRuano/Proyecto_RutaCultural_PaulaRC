package proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para el usuario.
 * Contiene los atributos de usuario.
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter
@Setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
@Table(name = "punto_de_interes") // Nombre de la tabla
public class PuntoDeInteresDTO {
	/** 
	 * Identificador único del punto de interés
	 * No puede ser null
	 */
	@Id 
	private Long id;

	/**
	 * Nombre del organismo del punto de interés
	 * No puede ser null
	 */
	@Column(nullable = false)
	private String nombreOrganismo;

	/**
	 * Calle del punto de interés
	 * No puede ser null
	 */	
	@Column(nullable = false)
	private String calle;

	/**
	 * Localidad del punto de interés
	 * No puede ser null
	 */	
	@Column(nullable = false)
	private String localidad;

	/**
	 * Latitud del punto de interés
	 * No puede ser null
	 */	
	@Column(nullable = false)
	private Double latitud;

	/**
	 * Longitud del punto de interés
	 * No puede ser null
	 */	
	@Column(nullable = false)
	private Double longitud;

	/**
	 * Categoria del punto de interés
	 * Relación con la tabla categoria (clave foránea)
	 * No puede ser null
	 * 
	 * @see CategoriaDTO
	 */
	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	private CategoriaDTO categoria;

}