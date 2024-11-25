package proyecto.modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ruta_predeterminada")
public class RutaPredeterminadaDTO extends RutaDTO{
	
	  public RutaPredeterminadaDTO(String nombre, Double duracion, int distancia, String municipio) {
	        super(nombre, duracion, distancia, "P", municipio); 
	    }
	  public RutaPredeterminadaDTO() {
	        super(); 
	    }
}
