package com.proyecto.servicio;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.proyecto.modelo.dao.UsuarioDAO;
import com.proyecto.modelo.dto.UsuarioDTO;

import jakarta.transaction.Transactional;
/**
 * Clase servicio para el usuario
 * Contiene los atributos de usuario
 * 
 * @author Paula Ruano
 */
@Service
public class UsuarioServicio {

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder contraseniaEncriptada;

    @Autowired
    public UsuarioServicio(UsuarioDAO usuarioDAO, PasswordEncoder contraseniaEncriptada) {
        this.usuarioDAO = usuarioDAO;
        this.contraseniaEncriptada = contraseniaEncriptada;
    }
    
    /** 
     * Método que recibe un usuario y crea un registro en la basde de datos
	 * @return UsuarioDTO
	 * @param UsuarioDTO
	 */
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) { 
    	//encriptar contraseña antes de crear el registro
        usuarioDTO.setContrasenia(contraseniaEncriptada.encode(usuarioDTO.getContrasenia()));
        UsuarioDTO savedUsuario = usuarioDAO.save(usuarioDTO); //save crea el registro       
        return savedUsuario; //retorna el objeto registrado
    }
    
    /** 
     * Método que busca en la base de datos un usuario cuyo correo coincida con el correo pasado como parámetro
	 * @return Optional<UsuarioDTO>
	 * @param String correo
	 */
    public Optional<UsuarioDTO> buscarPorCorreo(String correo) {    
        return usuarioDAO.findBycorreo(correo);
    }
    
    /** 
     * Método que verifica si la contraseña ingresada coincide con la almacenada en la base de datos para un correo específico.
	 * @return boolean
	 * @param String correo, String contraseniaIngresada
	 */
    public boolean verificarContrasenia(String correo, String contraseniaIngresada) {      

        Optional<UsuarioDTO> usuarioOpt = usuarioDAO.findBycorreo(correo);
        
        if (usuarioOpt.isPresent()) {
            UsuarioDTO usuario = usuarioOpt.get();            
            boolean matches = contraseniaEncriptada.matches(contraseniaIngresada, usuario.getContrasenia());
                        return matches;
        } else {
            System.out.println("Usuario no encontrado con el correo: " + correo);
        }
        return false;
    }
}
