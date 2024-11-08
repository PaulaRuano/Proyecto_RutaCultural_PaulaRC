package com.proyecto.modelo.dto;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)// clave generada automáticamente, autoincremental
	private int id;
	
	/**
	 * Nombre de usuario
	 * No puede estar en blanco.
	 */
	@NotBlank(message = "El nombre de usuario es obligatorio")
	@NonNull
	private String usuario;
	
	/**
	 * Contraseña del usuario
	 * No puede estar en blanco.
	 * Bebe tener al menos 6 caracteres.
	 */
	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	@NonNull
	private String contrasenia;
	
	/**
	 * Correo electrónico del usuario 
	 * debe tener un formato válido.
	 */
	@Email(message = "Correo no válido. Formato correcto: nombre@dominio.com")
	@NonNull
	private String correo;
	
}
