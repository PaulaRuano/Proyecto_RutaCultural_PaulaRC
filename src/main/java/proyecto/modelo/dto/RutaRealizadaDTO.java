package proyecto.modelo.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor		  // Constructor vacío
@AllArgsConstructor       // Constructor con todos los atributos
@Table(name = "ruta_realizada")    //  Nombre de la tabla
public class RutaRealizadaDTO {
	 /** Identificador único de la ruta realizada */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	 /** Relación con la tabla ruta (clave foránea) */
	@ManyToOne
	@JoinColumn(name = "id_ruta", nullable = false)
	private RutaDTO ruta; 

	 /**Relación con la tabla usuario (clave foránea) */
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioDTO usuario; 

	 /** Fecha en la que se ha terminado la ruta */
	@Column(nullable = false)
	private LocalDate fecha; 

	 /** Tiempo tomado para realizar la ruta en segundos */
	@NonNull
	@Column(nullable = false)
	private Integer tiempo;

}
