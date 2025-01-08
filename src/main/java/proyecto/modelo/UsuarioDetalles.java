package proyecto.modelo;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Clase que implementa la interfaz UserDetails de Spring Security
 * 
 * Adapta la clase personalizada UsuarioDTO al formato requerido por Spring Security
 * para gestionar la autenticación y autorización de usuarios en la aplicación
 * 
 * @author Paula Ruano
 */
@Getter // Genera automáticamente métodos getter
@RequiredArgsConstructor
public class UsuarioDetalles implements UserDetails {

    /** 
     * Objeto UsuarioDTO que contiene la información del usuario autenticado
     */
    private final UsuarioDTO usuario;

    /**
     * Devuelve la autoridad (rol) del usuario en formato que entiende Spring Security
     * 
     * @return Colección con una única autoridad basada en el rol del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {       
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()));
    }

    /** 
     * Devuelve la contraseña del usuario 
     * 
     * @return La contraseña del usuario
     */
    @Override
    public String getPassword() {
        return usuario.getContrasenia();
    }

    /** 
     * Devuelve el nombre de usuario, en este caso el correo electrónico del usuario 
     * 
     * @return El correo electrónico del usuario como nombre de usuario
     */
    @Override
    public String getUsername() {
        return usuario.getCorreo();
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     * 
     * @return true si la cuenta no ha expirado, false en caso contrario
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Ajusta según tus necesidades
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     * 
     * @return true si la cuenta no está bloqueada, false en caso contrario
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // Ajusta según tus necesidades
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     * 
     * @return true si las credenciales no han expirado, false en caso contrario
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Ajusta según tus necesidades
    }

    /**
     * Indica si la cuenta del usuario está habilitada.
     * 
     * @return true si la cuenta está habilitada, false en caso contrario
     */
    @Override
    public boolean isEnabled() {
        return true; // Puedes personalizar esto basándote en el estado del usuario
    }
}
