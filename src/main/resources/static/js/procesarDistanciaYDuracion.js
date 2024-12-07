// Función para convertir minutos en horas y minutos
      function formatearDuracion(minutos) {
          if (isNaN(minutos) || minutos <= 0) return "Duración no disponible";

          const horas = Math.floor(minutos / 60);
          const minutosRestantes = Math.round(minutos % 60);

          if (horas > 0) {
              return `${horas} hora${horas > 1 ? 's' : ''} ${minutosRestantes > 0 ? minutosRestantes + ' minuto' + (minutosRestantes > 1 ? 's' : '') : ''}`;
          } else {
              return `${minutosRestantes} minuto${minutosRestantes > 1 ? 's' : ''}`;
          }
      }

      // Función para convertir metros en kilómetros
      function formatearDistancia(metros) {
          if (isNaN(metros) || metros <= 0) return "Distancia no disponible";

          const kilometros = metros / 1000;
          return `${kilometros.toFixed(2)} km`;
      }

      // Aplicar formateo dinámicamente
      document.addEventListener("DOMContentLoaded", () => {
          // Formatear todas las duraciones
          document.querySelectorAll('.duracion').forEach(el => {
              const minutos = parseFloat(el.dataset.minutos); // Leer atributo `data-minutos`
              el.textContent = formatearDuracion(minutos);
          });

          // Formatear todas las distancias
          document.querySelectorAll('.distancia').forEach(el => {
              const metros = parseFloat(el.dataset.metros); // Leer atributo `data-metros`
              el.textContent = formatearDistancia(metros);
          });
      });