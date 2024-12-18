package proyecto.utiles;

/**
 * Interfaz de utilidades para convertir (parsear) valores de tipo String a tipos numéricos.
 * 
 * Proporciona métodos estáticos para convertir cadenas de texto en números, manejando 
 * posibles errores de formato y retornando valores por defecto en caso de fallo.
 * 
 * @author Paula Ruano
 */
public interface Parsear {
	/**
	 * Convierte una cadena de texto en un número de tipo Double.
	 * 
	 * Si la cadena no tiene un formato válido para ser convertida en Double,
	 * retorna 0.0 como valor por defecto.
	 * 
	 * @param value La cadena de texto a convertir
	 * @return El valor convertido a Double, o 0.0 si ocurre un error de formato
	 * 
	 * @see PuntoDeInteresServicio
	 */
	static Double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	/**
	 * Convierte una cadena de texto en un número de tipo Long.
	 * 
	 * Si la cadena no tiene un formato válido para ser convertida en Long,
	 * retorna 0L como valor por defecto.
	 * 
	 * @param value La cadena de texto a convertir
	 * @return El valor convertido a Long, o 0L si ocurre un error de formato
	 * 
	 * @see PuntoDeInteresServicio
	 */
	static Long parseLong(String value) {
		try {
			return Long.parseLong(value); 
		} catch (NumberFormatException e) {		      
			return 0L; 
		}
	}
}
