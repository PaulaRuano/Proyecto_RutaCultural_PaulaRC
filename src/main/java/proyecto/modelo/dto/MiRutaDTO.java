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
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mi_ruta")
@NoArgsConstructor
@AllArgsConstructor
public class MiRutaDTO {
	/** Identificador único de la ruta guardada */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relación con la tabla ruta (clave foránea) */
    @ManyToOne
    @JoinColumn(nullable = false)
    private RutaDTO ruta; 

    /** Relación con la tabla usuario (clave foránea) */
    @ManyToOne
    @JoinColumn( nullable = false)
    private UsuarioDTO usuario; 

    /** Fecha en la que la ruta predeterminada es añadida a "Mis Rutas" */
    @Column(nullable = false)
    private LocalDate fecha; 
}