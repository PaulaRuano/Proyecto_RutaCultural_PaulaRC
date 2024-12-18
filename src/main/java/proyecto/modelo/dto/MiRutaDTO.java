package proyecto.modelo.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para las rutas predeterminadas que guardan los usuarios
 * Contiene los atributos de mis rutas
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@Table(name = "mi_ruta") // Nombre de la tabla
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
public class MiRutaDTO {
	/** 
	 * Identificador único de la ruta guardada
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Clave generada automáticamente, autoincremental
	private Long id;

	/**
	 * Relación con la tabla ruta (clave foránea) 
	 * 
	 * @see RutaDTO
	 */
	@ManyToOne
	@JoinColumn(name = "ruta_id", nullable = false)
	private RutaDTO ruta; 

	/** 
	 * Usuario que guarda la ruta
	 * Relación con la tabla usuario (clave foránea) 
	 * 
	 * @see UsuarioDTO
	 */
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private UsuarioDTO usuario; 

	/** 
	 * Fecha en la que la ruta predeterminada es añadida a "Mis Rutas" 
	 * No puede ser null
	 */
	@Column(nullable = false)
	private LocalDate fecha; 
}