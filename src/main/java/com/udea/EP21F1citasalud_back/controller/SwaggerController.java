package com.udea.EP21F1citasalud_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controlador para facilitar el acceso a la documentación Swagger
 */
@Controller
public class SwaggerController {
    
      
    /**
     * Controlador para pruebas de API
     */
    @RestController
    @RequestMapping("/api/test")
    public static class TestController {
        
        /**
         * Endpoint para probar la documentación
         * @return Mensaje de prueba con la fecha y hora actual
         */
        @GetMapping
        public ResponseEntity<String> test() {
            return ResponseEntity.ok("Swagger API documentation test - " + java.time.LocalDateTime.now());
        }
    }
}
