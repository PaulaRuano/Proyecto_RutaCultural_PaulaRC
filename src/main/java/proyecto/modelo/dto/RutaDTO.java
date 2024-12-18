package proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para la ruta
 * Contiene los atributos de las rutas
 * Es una clase abstracta
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
@RequiredArgsConstructor // Constructor sin id
@Table(name = "ruta") // Nombre de la tabla
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de herencia para unir tablas 
public abstract class RutaDTO {
	/** 
	 * Identificador único de la ruta.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Clave generada automáticamente, autoincremental
	private int id;

	/**
	 * Nombre de la ruta
	 * No puede ser null	
	 */	
	@NonNull
	@Column(nullable = false)
	private String nombre;

	/**
	 * Duracion en minutos
	 * No puede ser null
	 */	
	@NonNull
	@Column(nullable = false)
	private Double duracion;

	/**
	 * Distancia en metros
	 * No puede ser null
	 */	
	@NonNull
	@Column(nullable = false)
	private int distancia;

	/** 
	 * Tipo de ruta ("P" para predeterminada, "U" para usuario) 
	 * No puede ser null
	 */
	@NonNull
	@Column(nullable = false, length = 1) 
	private String tipo;

	/** 
	 * Municipio al que pertenece la ruta
	 * No puede ser null
	 */
	@NonNull
	@Column(nullable = false)
	private String municipio;
}