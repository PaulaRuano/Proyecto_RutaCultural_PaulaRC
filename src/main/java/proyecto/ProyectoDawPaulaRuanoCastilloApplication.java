package proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Clase principal de la aplicación Spring Boot
 * 
 * Esta clase es el punto de entrada de la aplicación y contiene el método 
 * main que inicia la ejecución del proyecto 
 * 
 * La anotación @SpringBootApplication habilita:
 * 
 * - La configuración automática de Spring Boot
 * - El escaneo de componentes (component scanning) en el paquete base
 * - La habilitación de configuraciones adicionales
 * 
 * @author Paula Ruano
 */
@SpringBootApplication
public class ProyectoDawPaulaRuanoCastilloApplication {
	/**
	 * Método principal
	 * Inicia la ejecución de la aplicación Spring Boot
	 * 
	 * Utiliza run para arrancar el contexto de la aplicación Spring 
	 * y configurarla automáticamente
	 * 
	 * @param args Argumentos pasados a la aplicación durante su ejecución.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProyectoDawPaulaRuanoCastilloApplication.class, args);
	}
}