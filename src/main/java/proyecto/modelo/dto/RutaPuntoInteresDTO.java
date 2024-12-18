package proyecto.modelo.dto;

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
 * Clase DTO para la tabla intermedia que relaciona rutas y puntos de interés
 * Contiene los atributos de rutas/puntos de inters
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Table(name = "ruta_punto_interes") // Nombre de la tabla
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
public class RutaPuntoInteresDTO {
	/** 
	 * Identificador único de la asociación entre ruta y punto de interés 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 * Relación con la tabla punto_de_interes (clave foránea) 
	 * 
	 * @see PuntoDeInteresDTO
	 */
	@ManyToOne
	@JoinColumn(name = "punto_id", nullable = false)
	private PuntoDeInteresDTO puntoInteres;
}
