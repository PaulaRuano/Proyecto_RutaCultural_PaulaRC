package proyecto.modelo.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase DTO para las rutas predeterminadas
 * Contiene los atributos de las rutas predeterminadas
 * Hereda de la clase abstracta RutaDTO
 * 
 * @author Paula Ruano
 */
@Entity // Clase entidad
@Getter // Genera automáticamente métodos getter
@Setter // Genera automáticamente métodos setter
@NoArgsConstructor // Constructor vacío
@Table(name = "ruta_predeterminada") // Nombre de la tabla
public class RutaPredeterminadaDTO extends RutaDTO{
	/** 
	 * Constructor que inicializa el 'tipo' de la ruta a "P" 
	 * 
	 * @see RutaDTO
	 */
	public RutaPredeterminadaDTO(String nombre, Double duracion, int distancia, String municipio) {
		super(nombre, duracion, distancia, "P", municipio); 
	}
}
