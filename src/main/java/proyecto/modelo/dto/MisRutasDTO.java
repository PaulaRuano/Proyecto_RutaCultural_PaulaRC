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
@Table(name = "mis_rutas")
@NoArgsConstructor
@AllArgsConstructor
public class MisRutasDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RutaDTO ruta; // Relación con la tabla `ruta`

    @ManyToOne
    @JoinColumn( nullable = false)
    private UsuarioDTO usuario; // Relación con la tabla `usuario`

    @Column(nullable = false)
    private LocalDate fecha; // Fecha en la que se añade a "Mis Rutas"
}