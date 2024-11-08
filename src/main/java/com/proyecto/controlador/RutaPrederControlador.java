package com.proyecto.controlador;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.modelo.dto.PuntoDeInteresDTO;
import com.proyecto.servicio.PuntoDeInteresServicio;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RutaPrederControlador {

	private final PuntoDeInteresServicio puntoDeInteresServicio;

	@Autowired
	public RutaPrederControlador(PuntoDeInteresServicio puntoDeInteresServicio) {
		this.puntoDeInteresServicio = puntoDeInteresServicio;
	}

	@GetMapping("/rutasPreder")
	public String mostrarRutas(Model model) {
		// Lógica para mostrar rutas predeterminadas
		return "rutasPreder";
	}

	@GetMapping("/api/ruta-predeterminada")
	public ResponseEntity<List<PuntoDeInteresDTO>> obtenerRutaPredeterminada(@RequestParam List<Long> ids) {
	    log.info("Recibiendo IDs: {}", ids);
	    try {
	        List<PuntoDeInteresDTO> puntos = puntoDeInteresServicio.obtenerPuntosPorIds(ids);
	        return ResponseEntity.ok(puntos);
	    } catch (Exception e) {
	        log.error("Error al procesar la ruta predeterminada: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


	}

