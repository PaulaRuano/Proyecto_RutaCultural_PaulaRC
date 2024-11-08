package com.proyecto.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.modelo.dto.PuntoDeInteresDTO;
import com.proyecto.servicio.PuntoDeInteresServicio;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
//PuntoDeInteresControlador.java
@RestController
@RequestMapping("/api/")  
@Slf4j
public class PuntoDeInteresControlador {

    @Autowired
    private final PuntoDeInteresServicio puntoDeInteresServicio;

    @Autowired
    public PuntoDeInteresControlador(PuntoDeInteresServicio puntoDeInteresServicio) {
        this.puntoDeInteresServicio = puntoDeInteresServicio;
    }

    @GetMapping("/puntos-de-interes")
    public ResponseEntity<List<PuntoDeInteresDTO>> listarPuntosDeInteres() {
        try {
            List<PuntoDeInteresDTO> puntosDeInteres = puntoDeInteresServicio.obtenerPuntosDeInteres();
            return ResponseEntity.ok(puntosDeInteres);
        } catch (Exception e) {
            log.error("Error al listar puntos de interés: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }   
}
