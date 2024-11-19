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

@Controller
public class RegistrarControlador {
	   private final UsuarioServicio usuarioServicio;

	    @Autowired
	    public RegistrarControlador(UsuarioServicio usuarioServicio) {
	        this.usuarioServicio = usuarioServicio;
	    }

	    @GetMapping("/registrar")
	    public String mostrarFormularioRegistro(Model model) {
	        // Añadir un objeto UsuarioDTO vacío al modelo
	        model.addAttribute("usuarioDTO", new UsuarioDTO());
	        return "registrar";
	    }

	    @PostMapping("/registro")
	    public String registrarUsuario(
	            @ModelAttribute UsuarioDTO usuarioDTO,
	            Model model,
	            HttpSession session) { // Agregar HttpSession como parámetro

	        boolean hasErrors = false;

	        // Validar que el nombre no esté vacío
	        if (!Validaciones.validarNoVacio(usuarioDTO.getUsuario())) {
	            model.addAttribute("nombreError", "El nombre es obligatorio.");
	            hasErrors = true;
	        }

	        // Validar que el correo no esté vacío y tenga un formato válido
	        if (!Validaciones.validarNoVacio(usuarioDTO.getCorreo())) {
	            model.addAttribute("correoError", "El correo es obligatorio.");
	            hasErrors = true;
	        } else if (!Validaciones.esFormatoCorreoValido(usuarioDTO.getCorreo())) {
	            model.addAttribute("correoError", "El correo no tiene un formato válido.");
	            hasErrors = true;
	        }

	        // Validar que la contraseña no esté vacía y tenga al menos 6 caracteres
	        if (!Validaciones.validarNoVacio(usuarioDTO.getContrasenia())) {
	            model.addAttribute("contraseniaError", "La contraseña es obligatoria.");
	            hasErrors = true;
	        } else if (!Validaciones.validarLongitud(usuarioDTO.getContrasenia(), 6, 255)) {
	            model.addAttribute("contraseniaError", "La contraseña debe tener al menos 6 caracteres.");
	            hasErrors = true;
	        }

	        // Validar si el correo ya está registrado
	        if (usuarioServicio.buscarPorCorreo(usuarioDTO.getCorreo()).isPresent()) {
	            model.addAttribute("correoError", "El correo ya está registrado.");
	            hasErrors = true;
	        }

	        // Si hay errores, volver al formulario con los mensajes de error
	        if (hasErrors) {
	            return "registrar";
	        }

	        // Invalida la sesión si existe (destruir la sesión del usuario logueado)
	        if (session != null) {
	            session.invalidate();
	        }

	        // Establecer el rol 'cliente' por defecto
	        usuarioDTO.setRol("cliente");

	        // Crear usuario si todo es válido
	        usuarioServicio.crearUsuario(usuarioDTO);

	        // Redirigir al login después del registro
	        return "redirect:/login";
	    }
}
