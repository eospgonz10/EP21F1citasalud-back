package com.udea.EP21F1citasalud_back.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;
    
    @Bean
    public OpenAPI defineOpenApi() {

        Contact myContact = new Contact();
        myContact.setName("Estiven Ospina González");

        Info information = new Info()
                .title("CitaSalud API")
                .version("1.0")
                .description("Esta API permite gestionar citas médicas y pacientes.")
                .termsOfService("https://www.udea.edu.co")
                .license(new io.swagger.v3.oas.models.info.License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org"))
                .contact(myContact);

        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Servidor de API CitaSalud");

        // Definir el esquema de seguridad JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Introduce el token JWT obtenido en la autenticación");

        // Definir requerimiento de seguridad
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(information)
                .servers(List.of(server))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}