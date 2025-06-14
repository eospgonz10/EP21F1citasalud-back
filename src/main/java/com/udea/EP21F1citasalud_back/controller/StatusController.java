package com.udea.EP21F1citasalud_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para verificar el estado de la API
 */
@RestController
@RequestMapping("/api/status")
@Tag(name = "API Status", description = "Endpoints para verificar el estado de la API")
public class StatusController {

    /**
     * Verifica que la API esté funcionando
     * @return Mensaje de estado
     */
    @GetMapping
    @Operation(summary = "Verificar estado", description = "Permite verificar que la API esté funcionando correctamente")
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok("La API está funcionando correctamente - " + java.time.LocalDateTime.now());
    }
}
