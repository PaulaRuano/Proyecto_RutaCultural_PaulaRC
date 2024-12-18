package proyecto.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.modelo.UsuarioDetalles;
import proyecto.modelo.dao.RutaPredeterminadaDAO;
import proyecto.modelo.dao.RutaUsuarioDAO;
import proyecto.modelo.dao.UsuarioDAO;
import proyecto.modelo.dto.RutaDTO;
import proyecto.modelo.dto.RutaRealizadaDTO;
import proyecto.modelo.dto.UsuarioDTO;
import proyecto.servicio.RutaRealizadaServicio;

/**
 * Controlador para gestionar las rutas realizadas por los usuarios
 * 
 * Permite mostrar las rutas realizadas y registrar nuevas rutas realizadas por un usuario
 * 
 * @author Paula Ruano
 */
@Controller
public class RutaRealizadaControlador {
	// Variables locales
	private final RutaRealizadaServicio rutaRealizadaServicio;
	private final RutaUsuarioDAO rutaUsuarioDAO;
	private final RutaPredeterminadaDAO rutaPredeterminadaDAO;
	private final UsuarioDAO usuarioDAO;

	// Controlador de la clase
	@Autowired
	public RutaRealizadaControlador(RutaRealizadaServicio rutaRealizadaServicio,
			RutaUsuarioDAO rutaUsuarioDAO,
			RutaPredeterminadaDAO rutaPredeterminadaDAO,
			UsuarioDAO usuarioDAO) {
		this.rutaRealizadaServicio = rutaRealizadaServicio;
		this.rutaUsuarioDAO = rutaUsuarioDAO;
		this.rutaPredeterminadaDAO = rutaPredeterminadaDAO;
		this.usuarioDAO = usuarioDAO;
	}

	/**
	 * Muestra las rutas realizadas por el usuario autenticado
	 * 
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la plantilla de la vista para mostrar las rutas realizadas
	 */
	@GetMapping("/listaRutasRealizadas")
	public String mostrarRutasRealizadas(Model model) {
		// Obtener el usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsuarioDetalles usuarioDetalles = (UsuarioDetalles) authentication.getPrincipal();

		// Obtener el ID del usuario autenticado
		int usuarioId = usuarioDetalles.getUsuario().getId();

		// Obtener las rutas realizadas por el usuario
		List<RutaRealizadaDTO> rutasRealizadas = rutaRealizadaServicio.obtenerRutasRealizadasPorUsuario(usuarioId);

		model.addAttribute("rutasRealizadas", rutasRealizadas);
		return "listaRutasRealizadas";
	}

	/**
	 * Registra una nueva ruta realizada por un usuario
	 * 
	 * @param rutaId ID de la ruta realizada
	 * @param usuarioId ID del usuario que realizó la ruta
	 * @param fecha Fecha en la que se realizó la ruta
	 * @param tiempo Tiempo tomado para realizar la ruta en segundos
	 * @param model Modelo para pasar datos a la vista
	 * @return Nombre de la vista o redirección tras guardar la ruta
	 */
	@PostMapping("/rutasRealizadas/guardar")
	public String guardarRutaRealizada(
			@RequestParam Integer rutaId,
			@RequestParam Integer usuarioId,
			@RequestParam String fecha,
			@RequestParam Integer tiempo,
			Model model
			) {
		try {
			// Buscar la ruta y el usuario asociados
			Optional<RutaDTO> rutaOpt = rutaUsuarioDAO.findById(rutaId)
					.map(ruta -> (RutaDTO) ruta)
					.or(() -> rutaPredeterminadaDAO.findById(rutaId).map(ruta -> (RutaDTO) ruta));

			RutaDTO ruta = rutaOpt.orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada."));
			UsuarioDTO usuario = usuarioDAO.findById(usuarioId)
					.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

			// Crear y guardar la ruta realizada
			RutaRealizadaDTO rutaRealizada = new RutaRealizadaDTO();
			rutaRealizada.setRuta(ruta);
			rutaRealizada.setUsuario(usuario);
			rutaRealizada.setFecha(LocalDate.parse(fecha));
			rutaRealizada.setTiempo(tiempo);
			rutaRealizadaServicio.crearRutaRealizada(rutaRealizada);

			// Cargar las rutas realizadas en el modelo
			List<RutaRealizadaDTO> rutasRealizadas = rutaRealizadaServicio.obtenerRutasRealizadasPorUsuario(usuarioId);
			model.addAttribute("rutasRealizadas", rutasRealizadas);

			return "listaRutasRealizadas";
		} catch (Exception e) {
			model.addAttribute("error", "Error al guardar la ruta: " + e.getMessage());
			return "hacerRuta";
		}
	}

}