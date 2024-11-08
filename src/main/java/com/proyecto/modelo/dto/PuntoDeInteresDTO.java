package com.proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Clase DTO para el usuario.
 * Contiene los atributos de usuario.
 * 
 * @author Paula Ruano
 */
@Entity
@Getter
@Setter
@NoArgsConstructor		  // Constructor vacío
@AllArgsConstructor       // Constructor con todos los atributos
@Table(name = "puntoDeInteres") //  Nombre de la tabla
public class PuntoDeInteresDTO {
	//Atributos
	
	/** 
	 * Identificador único del punto de interés
	 * No puede ser null
	 */
	@Id // Clave primaria	
	private Long id;
	
	/**
	 * Nombre del organismo
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
	 * Tipo/categoria del punto de interés
	 * No puede ser null
	 */	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoLugar tipo;
}
