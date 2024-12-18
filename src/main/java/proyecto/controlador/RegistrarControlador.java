package proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import jakarta.servlet.http.HttpSession;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.UsuarioServicio;
import proyecto.utiles.Validaciones;

/**
 * Controlador para gestionar el registro de usuarios
 * 
 * Maneja las solicitudes para mostrar el formulario de registro y registrar nuevos usuarios
 * 
 * @author Paula Ruano
 */
@Controller
public class RegistrarControlador {
	// Variable local
	private final UsuarioServicio usuarioServicio;

	// Constructor de la clase
	@Autowired
	public RegistrarControlador(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	/**
	 * Muestra el formulario de registro
	 * 
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para el formulario de registro
	 */
	@GetMapping("/registrar")
	public String mostrarFormularioRegistro(Model model) {
		// Añadir un objeto UsuarioDTO vacío al modelo para enlazar con el formulario
		model.addAttribute("usuarioDTO", new UsuarioDTO());
		return "registrar";
	}

	/**
	 * Maneja el registro de un nuevo usuario
	 * 
	 * Valida los datos ingresados por el usuario, verifica que el correo no esté ya registrado
	 * y crea un nuevo usuario si todas las validaciones pasan
	 * 
	 * @param usuarioDTO Objeto con los datos del usuario a registrar
	 * @param model Modelo para pasar datos a la vista
	 * @param session Sesión HTTP actual para invalidar si existe
	 * @return Redirección al login o vuelta al formulario si hay errores
	 */
	@PostMapping("/registro")
	public String registrarUsuario(
			@ModelAttribute UsuarioDTO usuarioDTO,
			Model model,
			HttpSession session) {

		boolean tieneErrores = false;

		// Validar el nombre de usuario
		if (!Validaciones.validarNoVacio(usuarioDTO.getUsuario())) {
			model.addAttribute("nombreError", "El nombre es obligatorio.");
			tieneErrores = true;
		}

		// Validar el correo electrónico
		if (!Validaciones.validarNoVacio(usuarioDTO.getCorreo())) {
			model.addAttribute("correoError", "El correo es obligatorio.");
			tieneErrores = true;
		} else if (!Validaciones.esFormatoCorreoValido(usuarioDTO.getCorreo())) {
			model.addAttribute("correoError", "El correo no tiene un formato válido.");
			tieneErrores = true;
		}

		// Validar la contraseña 
		if (!Validaciones.validarNoVacio(usuarioDTO.getContrasenia())) {
			model.addAttribute("contraseniaError", "La contraseña es obligatoria.");
			tieneErrores = true;
		} else if (!Validaciones.validarLongitud(usuarioDTO.getContrasenia(), 6, 255)) {
			model.addAttribute("contraseniaError", "La contraseña debe tener al menos 6 caracteres.");
			tieneErrores = true;
		}

		// Verificar si el correo ya está registrado
		if (usuarioServicio.buscarPorCorreo(usuarioDTO.getCorreo()).isPresent()) {
			model.addAttribute("correoError", "El correo ya está registrado.");
			tieneErrores = true;
		}

		// Si hay errores, volver al formulario de registro
		if (tieneErrores) {
			return "registrar";
		}

		// Invalidar la sesión si existe
		if (session != null) {
			session.invalidate();
		}

		// Asignar rol predeterminado al usuario
		usuarioDTO.setRol("cliente");

		// Crear el usuario
		usuarioServicio.crearUsuario(usuarioDTO);

		// Redirigir al login después del registro exitoso
		return "redirect:/login";
	}
}
