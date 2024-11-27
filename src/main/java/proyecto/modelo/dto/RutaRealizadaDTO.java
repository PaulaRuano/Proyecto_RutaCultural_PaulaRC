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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_ruta", nullable = false)
	private RutaDTO ruta; // Relación con la tabla `ruta`

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioDTO usuario; // Relación con la tabla `usuario`

	@Column(nullable = false)
	private LocalDate fecha; // Fecha en la que se añade a "Mis Rutas"

	@NonNull
	@Column(nullable = false) // Tiempo en minutos o segundos (según lo definido)
	private Integer tiempo;

}
