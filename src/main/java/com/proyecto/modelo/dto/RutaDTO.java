package com.proyecto.modelo.dto;

import jakarta.persistence.Column;
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
@RequiredArgsConstructor  // Constructor sin id
@Table(name = "ruta")    //  Nombre de la tabla
public class RutaDTO {
	//Atributos

	/** 
	 * Identificador único del usuario.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)// clave generada automáticamente, autoincremental
	private int id;

	/**
	 * Nombre de la ruta	
	 */	
	@NonNull
	@Column(nullable = false)
	private String nombre;
	
	/**
	 * String con los id de los puntos de interés
	 */
	@NonNull
	@Column(nullable = false)
	private String puntosInteres;

	/**
	 * Duracion en minutos
	 */	
	@NonNull
	@Column(nullable = false)
	private Double duracion;
	
	/**
	 * Distancia en metros
	 */	
	@NonNull
	@Column(nullable = false)
	private int distancia;
}
