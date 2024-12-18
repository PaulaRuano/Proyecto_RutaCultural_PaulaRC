package proyecto.modelo.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para las rutas que crea el usuario
 * Contiene los atributos de las rutas de usuario
 * Hereda de la clase abstracta RutaDTO
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@Table(name = "ruta_usuario") // Nombre de la tabla
public class RutaUsuarioDTO extends RutaDTO{

	/** 
	 * Usuario que ha creado la ruta
	 * Relación con el usuario (clave foránea) 
	 * 
	 * @see UsuarioDTO
	 */
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false) 
	private UsuarioDTO usuario;

	/** 
	 * Fecha de creación de la ruta 
	 * No puede ser null
	 */
	@Column(nullable = false)
	private LocalDate fechaCreacion;

	/** 
	 * Constructor que inicializa el 'tipo' de la ruta a "U"
	 * 
	 * @see RutaDTO
	 */
	public RutaUsuarioDTO(String nombre, Double duracion, int distancia, String municipio, UsuarioDTO usuario) {
		super(nombre, duracion, distancia, "U", municipio); 
		this.usuario = usuario;
	}
}
