package proyecto.utiles;

public interface Parsear {
	static Double parseDouble(String value) {
	     try {
	         return Double.parseDouble(value);
	     } catch (NumberFormatException e) {
	         return 0.0;
	     }
	 }
	 
	 static Long parseLong(String value) {
		    try {
		        return Long.parseLong(value); 
		    } catch (NumberFormatException e) {		      
		        return 0L; 
		    }
		}
}
