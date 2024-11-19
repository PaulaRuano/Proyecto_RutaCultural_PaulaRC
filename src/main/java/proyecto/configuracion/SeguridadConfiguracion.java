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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/home", "registrar","/registro", "/api/puntos-de-interes/**", "/rutasPreder", 
						"/api/ruta-predeterminada","/css/**", "/json/**","/js/**", "/img/**").permitAll() // Permite acceso sin autenticación
				.requestMatchers("/perfilUsuario/**", "/cambiarContrasenia", "cambiarCorreo","cambiarNombre").authenticated() // Solo usuarios autenticados
				.anyRequest().hasRole("CLIENTE")
				)
		.formLogin(form -> form
				.loginPage("/login") // Ruta de tu formulario de inicio de sesión personalizado
				.loginProcessingUrl("/iniciar_Sesion")
				.usernameParameter("correo") // Cambia el nombre del parámetro para el usuario
				.passwordParameter("contrasenia") // Cambia el nombre del parámetro para la contraseña
				.defaultSuccessUrl("/home", true) // Redirige a /home después de un inicio de sesión exitoso
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