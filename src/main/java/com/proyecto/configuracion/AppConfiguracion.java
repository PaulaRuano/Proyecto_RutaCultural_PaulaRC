package com.proyecto.configuracion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class AppConfiguracion {
	
    @Bean
    public PasswordEncoder encriptarContrasenia() {
        return new BCryptPasswordEncoder();
    }
    
    // Loggs en consola
    private static final Logger log = LoggerFactory.getLogger(AppConfiguracion.class);  

    /**
     * Interceptor para registrar información de las peticiones y respuestas HTTP.
     */
    private ClientHttpRequestInterceptor interceptorDeRegistro() {
        return (request, body, execution) -> {
            log.info("URI de la petición: {}", request.getURI());
            log.info("Método HTTP: {}", request.getMethod());
            log.info("Cuerpo de la petición: {}", new String(body, StandardCharsets.UTF_8));
            ClientHttpResponse response = execution.execute(request, body);
            registrarRespuesta(response);
            return response;
        };
    }

    /**
     * Registra el cuerpo de la respuesta HTTP.
     */
    private void registrarRespuesta(ClientHttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        StringBuilder cuerpoRespuesta = new StringBuilder();
        String linea;
        while ((linea = reader.readLine()) != null) {
            cuerpoRespuesta.append(linea);
        }
        log.info("Cuerpo de la respuesta: {}", cuerpoRespuesta.toString());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
