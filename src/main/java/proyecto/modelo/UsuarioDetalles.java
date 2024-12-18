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
public class UsuarioDetalles implements UserDetails { //
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
}
