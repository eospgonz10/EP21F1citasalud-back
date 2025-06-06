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
     * Redirecciona la ruta raíz a la UI de Swagger
     * @return RedirectView a la documentación de Swagger
     */
    @GetMapping("/")
    public RedirectView redirectToSwaggerUI() {
        return new RedirectView("/doc/swagger-ui/index.html");
    }
    
    /**
     * Redirecciona /doc a la UI de Swagger
     * @return RedirectView a la documentación de Swagger
     */
    @GetMapping("/doc")
    public RedirectView redirectDocToSwaggerUI() {
        return new RedirectView("/doc/swagger-ui/index.html");
    }
    
    /**
     * Acceso directo a Swagger UI
     * @return RedirectView a la documentación de Swagger
     */
    @GetMapping("/swagger")
    public RedirectView redirectSwaggerToSwaggerUI() {
        return new RedirectView("/doc/swagger-ui/index.html");
    }
    
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
