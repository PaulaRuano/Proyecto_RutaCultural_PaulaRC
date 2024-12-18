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
 * Clase DTO para las rutas realizadas
 * Contiene los atributos de las rutas realizadas
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
@Table(name = "ruta_realizada") // Nombre de la tabla
public class RutaRealizadaDTO {
	/** 
	 * Identificador único de la ruta realizada 
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
	@JoinColumn(name = "id_ruta", nullable = false)
	private RutaDTO ruta; 

	/**
	 * Usuario que realiza la ruta
	 * Relación con la tabla usuario (clave foránea)
	 * 
	 * @see UsuarioDTO
	 */
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioDTO usuario; 

	/**
	 * Fecha en la que se ha terminado la ruta
	 * No puede ser null
	 */
	@Column(nullable = false)
	private LocalDate fecha; 

	/**
	 * Tiempo tomado para realizar la ruta en segundos
	 * No puede ser null
	 */
	@Column(nullable = false)
	private Integer tiempo;

}
