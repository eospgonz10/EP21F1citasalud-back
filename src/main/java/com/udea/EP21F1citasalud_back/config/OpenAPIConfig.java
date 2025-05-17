package com.udea.EP21F1citasalud_back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        server.setDescription("Servidor configurado");

        return new OpenAPI()
                .info(information)
                .servers(List.of(server));
    }
}