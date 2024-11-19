package proyecto.utiles;

public interface Validaciones {
	/**
     * Valida que un texto no exceda una longitud máxima
     *
     * @param texto 
     * @param maxCaracteres 
     * @return boolean
     */
	static boolean validarLongitud(String texto, int minCaracteres, int maxCaracteres) {
	    return texto != null && texto.length() >= minCaracteres && texto.length() <= maxCaracteres;
	}
    
	 /**
     * Valida que un texto tenga contenido
     *
     * @param texto
     * @return boolean
     */
    static boolean validarNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    

    /**
     * Valida si un correo tiene el formato correcto
     *
     * Formato: mínimo 1 carácter + "@" + mínimo 1 carácter + "." + mínimo 2 caracteres
     *
     * @param correo 
     * @return true 
     */
    static boolean esFormatoCorreoValido(String correo) {
    	// Verificar si el correo es nulo o vacío 
        if (correo == null || correo.isEmpty()) {
            return false; 
        }

        /* Expresión regular: uno o más carcateres que no sea "@" +  "@" +
           uno o más carcateres que no sea "@" + "." + dos o más caracteres alfabéticos */
        String regex = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
        return correo.matches(regex);
    }
}
