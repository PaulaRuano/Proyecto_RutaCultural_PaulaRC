// Función para convertir minutos en horas y minutos
function formatearDuracion(minutos) {
// Verifica si el valor es un número válido mayor que 0
	if (isNaN(minutos) || minutos <= 0) return "Duración no disponible";
		  
	// Calcula la cantidad de horas
    const horas = Math.floor(minutos / 60);
	// Calcula los minutos restantes después de convertir a horas
    const minutosRestantes = Math.round(minutos % 60);
		  
	// Devuelve la duración formateada según las horas y minutos
    if (horas > 0) {
         return `${horas} hora${horas > 1 ? 's' : ''} ${minutosRestantes > 0 ? minutosRestantes + ' minuto' + (minutosRestantes > 1 ? 's' : '') : ''}`;
    } else {
         return `${minutosRestantes} minuto${minutosRestantes > 1 ? 's' : ''}`;
    }
}

// Función para convertir metros en kilómetros con dos decimales
function formatearDistancia(metros) {
	// Verifica si el valor es un número válido mayor que 0
     if (isNaN(metros) || metros <= 0) return "Distancia no disponible";
	 
	 // Convierte los metros a kilómetros dividiendo entre 1000
     const kilometros = metros / 1000;
	 // Devuelve la distancia en kilómetros formateada con dos decimales
     return `${kilometros.toFixed(2)} km`;
}

// Evento que se ejecuta cuando el contenido de la página ha terminado de cargar
document.addEventListener("DOMContentLoaded", () => {
    // Formatea todas las duraciones en los elementos con la clase `duracion`
    document.querySelectorAll('.duracion').forEach(el => {
          const minutos = parseFloat(el.dataset.minutos); // Leer atributo `data-minutos`
          el.textContent = formatearDuracion(minutos); // Aplicar el formato y lo mostrar en el elemento
     });

       // Formatea todas las distancias en los elementos con la clase `distancia`
     document.querySelectorAll('.distancia').forEach(el => {
          const metros = parseFloat(el.dataset.metros); // Leer atributo `data-metros`
          el.textContent = formatearDistancia(metros); // Aplicar el formato y lo mostrar en el elemento
     });
});