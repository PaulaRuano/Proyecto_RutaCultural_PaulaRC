package proyecto.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AutenticacionExitosa autenticacionExitosa) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/home", "registrar", "/registro", "/api/**", "/listaRutasPredeterminadas", 
						"/rutasPreder", "/api/ruta-predeterminada", "/crearRutaUsuario/**","/detalleRutaPredeterminada/**","/css/**", "/json/**", "/js/**", "/img/**")
				.permitAll() // Permite acceso sin autenticación
				.requestMatchers("/administradorVista", "/crearRutaPredeterminada","/cerrarSesionAdmin").hasRole("ADMIN") // Acceso solo para administradores
				.requestMatchers("/confirmarRutaUsuario", "/guardarRutaUsuario", "/cerrarSesion", "/audio/**", "/listaMisRutas", "/guardarEnMisRutas","/hacerRuta","/listaRutasRealizadas", "/rutasRealizadas/guardar","/perfilUsuario/**", "/cambiarContrasenia", "/cambiarCorreo", "/cambiarNombre")
				.authenticated() // Solo usuarios autenticados
				.anyRequest().hasRole("CLIENTE") // Todas las demás solicitudes requieren rol CLIENTE
				)
		.formLogin(form -> form
				.loginPage("/login") // Ruta de tu formulario de inicio de sesión personalizado
				.loginProcessingUrl("/iniciar_Sesion")
				.usernameParameter("correo") // Cambia el nombre del parámetro para el usuario
				.passwordParameter("contrasenia") // Cambia el nombre del parámetro para la contraseña
				 .successHandler(autenticacionExitosa)  // Redirige a /home después de un inicio de sesión exitoso
				.permitAll()
				)
		.logout(logout -> logout.permitAll()); // Permitir el cierre de sesión para todos

		return http.build();
	}

	@Bean
	public PasswordEncoder encriptarContrasenia() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}