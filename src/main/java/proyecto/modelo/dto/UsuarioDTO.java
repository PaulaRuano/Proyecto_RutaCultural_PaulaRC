package proyecto.modelo.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para el usuario
 * Contiene los atributos de usuario
 * 
 * @author Paula Ruano
 */
@Entity
@Getter
@Setter
@NoArgsConstructor		  // Constructor vacío
@AllArgsConstructor       // Constructor con todos los atributos
@RequiredArgsConstructor  // Constructor sin id
@Table(name = "usuario") //  Nombre de la tabla
public class UsuarioDTO {
	//Atributos
	
		/** 
		 * Identificador único del usuario.
		 */
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY) // clave generada automáticamente, autoincremental
		private int id;
		
		/**
		 * Nombre de usuario
		 */
		@NonNull
		private String usuario;
		
		/**
		 * Contraseña del usuario
		 * Debe tener al menos 6 caracteres
		 */
		@Size(min = 6, max = 700)
		@NonNull
		private String contrasenia;
		
		/**
		 * Correo electrónico del usuario 
		 * Debe tener un formato válido
		 * 
		 * @see utiles.Validaciones -> esFormatoCorreoValido(String correo)
		 */
		@Email
		@NonNull
		private String correo;
		
		/**
		 * Rol del usuario
		 * Cuando se crea el usuario, el rol por defecto es cliente
		 */
		@NonNull
	    private String rol = "cliente";
		
		/**
		 * Boolean para la baja de un usuario
		 */
		@NonNull
	    private boolean baja = false;	
}
