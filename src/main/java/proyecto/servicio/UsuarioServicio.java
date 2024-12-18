package proyecto.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.modelo.dao.UsuarioDAO;
import proyecto.modelo.dto.UsuarioDTO;

/**
 * Servicio para gestionar la lógica relacionada con los usuarios
 * 
 * @author Paula Ruano
 */
@Service
public class UsuarioServicio {
	// Variables locales de la clase
	private final UsuarioDAO usuarioDAO;
	private final PasswordEncoder contraseniaEncriptada;

	// Constructor de la clase
	@Autowired
	public UsuarioServicio(UsuarioDAO usuarioDAO, PasswordEncoder contraseniaEncriptada) {
		this.usuarioDAO = usuarioDAO;
		this.contraseniaEncriptada = contraseniaEncriptada;
	}

	/** 
	 * Crea un nuevo usuario en la base de datos con rol predeterminado y contraseña encriptada     
	 * Si el rol no está definido, se asigna el rol predeterminado "cliente"
	 * 
	 * @param usuarioDTO Objeto UsuarioDTO con los datos del usuario a crear
	 * @return UsuarioDTO El objeto del usuario creado y guardado en la base de datos
	 */
	@Transactional
	public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) { 
		// Verificar que el usuario no tenga un rol asignado
		if (usuarioDTO.getRol() == null || usuarioDTO.getRol().isEmpty()) {
			// Asignar el rol predeterminado "cliente" si no tiene rol definido
			usuarioDTO.setRol("cliente"); 
		}

		// Encriptar contraseña antes de crear el registro
		usuarioDTO.setContrasenia(contraseniaEncriptada.encode(usuarioDTO.getContrasenia()));

		// Guardar el usuario en la base de datos
		UsuarioDTO savedUsuario = usuarioDAO.save(usuarioDTO); //save crea el registro

		// Retornar el objeto del usuario registrado
		return savedUsuario; 
	}

	/** 
	 * Busca un usuario en la base de datos por su correo electrónico
	 * 
	 * @param correo Correo electrónico del usuario a buscar
	 * @return Optional<UsuarioDTO> Un Optional que contiene el usuario encontrado, si existe
	 * 
	 * @see RegistrarControlador
	 */
	public Optional<UsuarioDTO> buscarPorCorreo(String correo) {  
		 // Llama al método del DAO para buscar un usuario por su correo electrónico
		return usuarioDAO.findBycorreo(correo);
	}
}