package com.xantrix.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FiltersCorsConfig implements WebMvcConfigurer {

    /**
     * Configura le politiche CORS (Cross-Origin Resource Sharing) per l'applicazione.
     *
     * @return Un configuratore WebMvcConfigurer che definisce le regole CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")                 // Applica queste regole CORS a tutti i percorsi
                        .allowedOrigins("*")               // Consente richieste da qualsiasi origine
                        .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS", "HEAD", "PATCH")  // Metodi HTTP consentiti
                        .allowedHeaders("*")               // Consente tutti gli headers nelle richieste
                        .exposedHeaders("header1", "header2", "Authorization", "Content-Type")  // Headers esposti nelle risposte
                        .allowCredentials(false)           // Non consente l'invio di credenziali (es. cookies)
                        .maxAge(3600);                     // Durata massima della cache delle pre-flight request (in secondi)
            }
        };
    }
}
