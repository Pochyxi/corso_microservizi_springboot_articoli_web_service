package com.xantrix.webapp.tests.SecurityTests;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.xantrix.webapp.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest // Indica che si tratta di un test Spring Boot
@ContextConfiguration(classes = Application.class) // Configura il contesto per l'applicazione
@TestMethodOrder(OrderAnnotation.class) // Ordina i test secondo l'annotazione @Order
public class TestSecurity {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac; // Contesto dell'applicazione web

    @BeforeEach // Metodo da eseguire prima di ogni test
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac) // Configura MockMvc con il contesto dell'applicazione web
                .apply(springSecurity()) // Attiva la sicurezza
                .build();
    }

    private final String ApiBaseUrl = "/api/articoli"; // Base URL per l'API degli articoli

    @Order(1)
    @Test
    @WithAnonymousUser // Simula una richiesta anonima
    public void testSecurityErrlistArt() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/002000301")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // Verifica che la risposta sia 401 Unauthorized
                .andReturn();
    }

    @Order(2)
    @Test
    @WithMockUser(username = "Nicola", roles = {"USER"}) // Simula una richiesta con un utente autenticato con ruolo USER
    public void testSecurityErrlistArt2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/5002000301")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica che la risposta sia 200 OK
                .andReturn();
    }
}