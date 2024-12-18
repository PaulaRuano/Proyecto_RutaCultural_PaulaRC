package proyecto.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/**
 * Clase que gestiona el comportamiento después de una autenticación exitosa
 * 
 * Esta clase redirige a los usuarios a diferentes vistas según su rol 
 * después de un inicio de sesión exitoso
 * 
 * @author Paula Ruano
 */
@Configuration // Indica que una clase contiene definiciones de beans y configuraciones para Spring
@EnableWebSecurity // Habilita Spring Security
public class SeguridadConfiguracion { 
    /**
     * Configuración de la cadena de filtros de seguridad
     * 
     * Define las rutas públicas, protegidas y restringidas según los roles de usuario 
     * Configura un formulario de inicio de sesión personalizado y un handler 
     * para gestionar autenticaciones exitosas
     * 
     * @param http Objeto HttpSecurity para configurar la seguridad
     * @param autenticacionExitosa Handler que gestiona redirecciones tras inicio de sesión
     * @return SecurityFilterChain Configuración de seguridad
     * @throws Exception
     */
	@Bean // La anotación que se usa en Spring para definir métodos que crean y devuelven un bean (un componente) gestionado por el contenedor de Spring
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AutenticacionExitosa autenticacionExitosa) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/home","/login", "registrar", "/registro", "/api/**", "/listaRutasPredeterminadas", 
						"/rutasPreder", "/api/ruta-predeterminada", "/crearRutaUsuario/**","/detalleRuta/**","/css/**", "/json/**", "/js/**", "/img/**")
				.permitAll() // Permite acceso sin autenticación (anónimos y otros)
				.requestMatchers("/administradorVista", "/crearRutaPredeterminada","/cerrarSesionAdmin").hasRole("ADMIN") // Acceso solo para administradores
				.requestMatchers("/confirmarRutaUsuario", "/guardarRutaUsuario", "/cerrarSesion", "/audio/**", 
						"/listaMisRutas", "/guardarEnMisRutas","/hacerRuta","/listaRutasRealizadas", "/rutasRealizadas/guardar")
				.authenticated() // Acceso solo usuarios  a usuarios autenticados 
				.anyRequest().hasRole("CLIENTE") // Todas las demás solicitudes requieren autenticación con rol CLIENTE
				)
		.formLogin(form -> form
				.loginPage("/login") // Ruta del formulario de inicio de sesión personalizado
				.loginProcessingUrl("/iniciar_Sesion") // URL a la que se envían los datos del formulario de inicio de sesión
				.usernameParameter("correo") // Nombre del parámetro para el usuario, personalizado
				.passwordParameter("contrasenia") // Nombre del parámetro para la contraseña, personalizado
				.successHandler(autenticacionExitosa)  // Redirige a /home después de un inicio de sesión exitoso
				.permitAll()
				)
		.logout(logout -> logout.permitAll()); // Permitir el cierre de sesión 

		return http.build();
	}
	
	/**
     * Bean para encriptar contraseñas
     * 
     * Utiliza BCryptPasswordEncoder para codificar las contraseñas de los usuarios
     * 
     * @return PasswordEncoder Objeto para encriptar contraseñas
     */
	@Bean
	public PasswordEncoder encriptarContrasenia() {
		return new BCryptPasswordEncoder();
	}
	
	/**
     * Bean para realizar solicitudes HTTP externas
     * 
     * Proporciona una instancia de RestTemplate para interactuar con APIs externas
     * 
     * @return RestTemplate Instancia para realizar solicitudes REST
     */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}