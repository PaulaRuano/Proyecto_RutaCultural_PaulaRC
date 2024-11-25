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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ruta_usuario") 
public class RutaUsuarioDTO extends RutaDTO{

    /** Relación con el usuario (clave foránea) */
    @NonNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Clave foránea a la tabla Usuario
    private UsuarioDTO usuario;
    
    @NonNull
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    /** Constructor que inicializa 'tipo' a "U" */
    public RutaUsuarioDTO(String nombre, Double duracion, int distancia, String municipio, UsuarioDTO usuario) {
        super(nombre, duracion, distancia, "U", municipio); // Inicializa 'tipo' a "U"
        this.usuario = usuario;
    }
    
    
}
