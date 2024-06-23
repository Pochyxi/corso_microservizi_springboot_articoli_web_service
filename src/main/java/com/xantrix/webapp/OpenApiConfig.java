package com.xantrix.webapp;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Indica che questa classe è una configurazione Spring
@Configuration
public class OpenApiConfig {
    // URL per accedere all'interfaccia Swagger UI
    // http://localhost:5051/swagger-ui/index.html

    // Configura un bean per OpenAPI
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Articoli Web Service API") // Titolo dell'API
                        .description("Spring Boot RESTful service for Articoli") // Descrizione dell'API
                        .termsOfService("terms") // Termini di servizio
                        .contact(new Contact()
                                .email("adienerlopez@gmail.com") // Email di contatto
                                .name("Adiener Lopez")) // Nome di contatto
                        .license(new License()
                                .name("Apache 2.0") // Nome della licenza
                                .url("http://springdoc.org")) // URL della licenza
                        .version("1.0") // Versione dell'API
                );
    }
}
