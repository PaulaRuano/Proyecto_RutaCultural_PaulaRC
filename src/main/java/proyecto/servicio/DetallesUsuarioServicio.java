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

@Service
public class DetallesUsuarioServicio implements UserDetailsService {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    public DetallesUsuarioServicio(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        UsuarioDTO usuario = usuarioDAO.findBycorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
        return new UsuarioDetalles(usuario);
    }
    
    public UsuarioDTO obtenerUsuarioActual() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioDAO.findBycorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
    }
}