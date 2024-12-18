package proyecto.utiles;

/**
 * Interfaz de utilidades para validaciones de texto.
 * 
 * Contiene métodos estáticos para validar la longitud, contenido y formato de textos,
 * como correos electrónicos.
 * 
 * @author Paula Ruano
 */
public interface Validaciones {
	/**
	 * Valida que un texto no exceda una longitud máxima ni sea menor a una longitud mínima.
	 *
	 * @param texto Texto a validar
	 * @param minCaracteres Longitud mínima permitida
	 * @param maxCaracteres Longitud máxima permitida
	 * @return true si el texto cumple con la longitud especificada, false en caso contrario
	 * 
	 * @see RegistrarControlador
	 */
	static boolean validarLongitud(String texto, int minCaracteres, int maxCaracteres) {
		return texto != null && texto.length() >= minCaracteres && texto.length() <= maxCaracteres;
	}

	/**
	 * Valida que un texto no esté vacío ni sea nulo.
	 *
	 * @param texto Texto a validar
	 * @return true si el texto no es nulo ni está vacío, false en caso contrario
	 * 
	 * @see RegistrarControlador
	 */
	static boolean validarNoVacio(String texto) {
		return texto != null && !texto.trim().isEmpty();
	}

	/**
	 * Valida si un correo electrónico tiene el formato correcto.
	 *
	 * El formato válido es: mínimo 1 carácter + "@" + mínimo 1 carácter + "." + mínimo 2 caracteres alfabéticos.
	 *
	 * @param correo Correo electrónico a validar
	 * @return true si el correo cumple con el formato correcto, false en caso contrario
	 * 
	 * @see RegistrarControlador
	 */
	static boolean esFormatoCorreoValido(String correo) {
		// Verificar si el correo es nulo o vacío 
		if (correo == null || correo.isEmpty()) {
			return false; 
		}

		/* 
		 * Expresión regular para validar el formato del correo electrónico:
		 * - ^[^@]+       : Inicio, seguido de uno o más caracteres que no sean "@"
		 * - @            : El símbolo "@" obligatorio
		 * - [^@]+        : Uno o más caracteres que no sean "@"
		 * - \\.[a-zA-Z]{2,}$ : Un punto "." seguido de al menos dos caracteres alfabéticos
		 */
		String regex = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
		return correo.matches(regex);
	}
}
