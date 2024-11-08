package com.proyecto.controlador;

import com.proyecto.modelo.dto.UsuarioDTO;
import com.proyecto.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginControlador {

    private final UsuarioServicio usuarioServicio;

    @Autowired
    public LoginControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    // Mostrar el formulario de login/registro
    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) { //objeto para pasar datos a la vista
        model.addAttribute("error", null); // sin mensaje de error por defecto        
        return "login"; // vista que devuelve
    }

    // Manejo por POST de las solicitudes del formulario de inicio de sesión
    @PostMapping("/iniciar_Sesion")
    public String iniciarSesion(@RequestParam String correo, 
                                @RequestParam String contrasenia, 
                                Model model) {
    	
        // Llama al servicio para verificar la contraseña
        if (usuarioServicio.verificarContrasenia(correo, contrasenia)) {
            return "redirect:/home"; // Redirigir a home si los datos son correctos
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login"; 
        }
    }
    
    // Manejo del formulario de registro
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {    	       
        if (usuarioServicio.buscarPorCorreo(usuarioDTO.getCorreo()).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado");
            return "login";  
        } else {
            usuarioServicio.crearUsuario(usuarioDTO);
            return "redirect:/home"; 
        }
    }

}
