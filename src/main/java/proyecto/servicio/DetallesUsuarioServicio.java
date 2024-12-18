package proyecto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import proyecto.modelo.UsuarioDetalles;
import proyecto.modelo.dao.UsuarioDAO;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Servicio que implementa la interfaz UserDetailsService de Spring Security
 * 
 * @author Paula Ruano
 */
@Service
public class DetallesUsuarioServicio implements UserDetailsService {
	// Variable locale de la clase
	private final UsuarioDAO usuarioDAO;

	// Constructor de la clase
	@Autowired
	public DetallesUsuarioServicio(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
	
	/**
	 * Carga los detalles del usuario utilizando su correo electrónico
	 * 
	 * Este método es utilizado por Spring Security durante el proceso de autenticación
	 * 
	 * @param correo Correo electrónico del usuario a buscar
	 * @return UserDetails Los detalles del usuario encontrados
	 * @throws UsernameNotFoundException Si no se encuentra un usuario con el correo proporcionado
	 */
	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		// Buscar el usuario por correo electrónico
		UsuarioDTO usuario = usuarioDAO.findBycorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
		
		// Retornar los detalles del usuario adaptados a Spring Security
		return new UsuarioDetalles(usuario);
	}

	/**
	 * Obtiene el usuario actualmente autenticado en el contexto de seguridad
	 * 
	 * @return UsuarioDTO El usuario autenticado en la sesión actual
	 * @throws UsernameNotFoundException Si no se encuentra el usuario autenticado
	 * 
	 * @see ConfirmarRutaUsuarioControlador
	 * @see HacerRutaControlador
	 * @see MiRutaControlador
	 */
	public UsuarioDTO obtenerUsuarioActual() {
		// Obtener el correo del usuario autenticado desde el contexto de seguridad
		String correo = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Buscar y retornar el usuario en la base de datos
		return usuarioDAO.findBycorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
	}
	
}