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
 * Clase UsuarioDetalles
 *  Implementa la interfaz UserDetails de Spring Security
 * adapta tu clase personalizada UsuarioDTO al formato que entiende Spring Security.
 * @author Paula Ruano
 */
@Getter
@RequiredArgsConstructor
public class UsuarioDetalles implements UserDetails { //

    private final UsuarioDTO usuario;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Ajustar los roles del usuario
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return usuario.getContrasenia();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
