package com.proyecto.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.proyecto.modelo.dao.UsuarioDAO;
import com.proyecto.modelo.dto.UsuarioDTO;
import com.proyecto.modelo.dto.UsuarioDetalles;

import java.util.ArrayList;

@Service
public class ServicioDetallesUsuario implements UserDetailsService {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    public ServicioDetallesUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        UsuarioDTO usuario = usuarioDAO.findBycorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
        return new UsuarioDetalles(usuario);
    }
}