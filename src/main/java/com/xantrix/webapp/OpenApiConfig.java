package com.xantrix.webapp;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    //http://localhost:8080/swagger-ui.html

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Articoli Web Service API")
                        .description("Spring Boot RESTful service for Articoli")
                        .termsOfService("terms")
                        .contact(new Contact()
                                .email("adienerlopez@gmail.com")
                                .name("Adiener Lopez"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .version("1.0")
                );
    }
}
