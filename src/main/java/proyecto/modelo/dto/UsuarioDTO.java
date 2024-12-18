package proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para el usuario
 * Contiene los atributos de usuario
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
@Table(name = "usuario") // Nombre de la tabla
public class UsuarioDTO {
	/** 
	 * Identificador único del usuario.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Clave generada automáticamente, autoincremental
	private int id;

	/**
	 * Nombre de usuario
	 * No puede ser null
	 */
	@Column(nullable = false)
	private String usuario;

	/**
	 * Contraseña del usuario
	 * Debe tener al menos 6 caracteres
	 * No puede ser null
	 */
	@Size(min = 6, max = 700)
	@Column(nullable = false)
	private String contrasenia;

	/**
	 * Correo electrónico del usuario 
	 * Debe tener un formato válido
	 * No puede ser null
	 * 
	 * @see utiles.Validaciones -> esFormatoCorreoValido(String correo)
	 */
	@Email
	@Column(nullable = false)
	private String correo;

	/**
	 * Rol del usuario
	 * Cuando se crea el usuario, el rol por defecto es cliente
	 * No puede ser null
	 */
	@Column(nullable = false)
	private String rol = "cliente";

	/**
	 * Boolean para la baja de un usuario
	 * No puede ser null
	 */
	@Column(nullable = false)
	private boolean baja = false;	
}
