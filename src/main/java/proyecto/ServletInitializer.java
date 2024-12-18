package proyecto;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Clase que permite inicializar la aplicación en un contenedor de servlets tradicional
 * 
 * La clase extiende de SpringBootServletInitializer, lo que hace que la aplicación
 * pueda desplegarse como un archivo WAR en servidores externos (como Tomcat)
 * 
 * Esta clase es necesaria si la aplicación se ejecutará en un servidor web clásico
 * en lugar de utilizar el servidor integrado de Spring Boot
 * 
 * @author Paula Ruano
 */
public class ServletInitializer extends SpringBootServletInitializer {
	/**
	 * Configura la aplicación al sobrescribir el método configure
	 * 
	 * Este método vincula la clase principal de la aplicación, 
	 * ProyectoDawPaulaRuanoCastilloApplication con el contenedor de servlets
	 * 
	 * @param application El constructor de la aplicación
	 * @return SpringApplicationBuilder La instancia configurada del SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProyectoDawPaulaRuanoCastilloApplication.class);
	}
}