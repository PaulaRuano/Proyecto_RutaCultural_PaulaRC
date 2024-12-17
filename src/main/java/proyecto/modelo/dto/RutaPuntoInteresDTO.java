package proyecto.modelo.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ruta_punto_interes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaPuntoInteresDTO {
	/** Identificador único de la asociación entre ruta y punto de interés */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relación con la tabla ruta (clave foránea) */
    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private RutaDTO ruta;

    /** Relación con la tabla punto_de_interes (clave foránea) */
    @ManyToOne
    @JoinColumn(name = "punto_id", nullable = false)
    private PuntoDeInteresDTO puntoInteres;
}
