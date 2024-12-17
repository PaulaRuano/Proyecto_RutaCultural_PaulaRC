package proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "ruta")    //  Nombre de la tabla
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class RutaDTO {
	/** 
	 * Identificador único de la ruta.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)// clave generada automáticamente, autoincremental
	private int id;

	/**
	 * Nombre de la ruta	
	 */	
	@Column(nullable = false)
	private String nombre;

	/**
	 * Duracion en minutos
	 */	
	@NonNull
	@Column(nullable = false)
	private Double duracion;

	/**
	 * Distancia en metros
	 */	
	@Column(nullable = false)
	private int distancia;

	/** Tipo de ruta ("P" para predeterminada, "U" para usuario) */
	@Column(nullable = false, length = 1) 
	private String tipo;

	/** Municipio al que pertenece la ruta */
	@Column(nullable = false)
	private String municipio;
}