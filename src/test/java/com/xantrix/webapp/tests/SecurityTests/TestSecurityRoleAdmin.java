package com.xantrix.webapp.tests.SecurityTests;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.utility.JsonData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@SpringBootTest // Indica che si tratta di un test Spring Boot
@ContextConfiguration(classes = Application.class) // Configura il contesto per l'applicazione
@TestMethodOrder(OrderAnnotation.class) // Ordina i test secondo l'annotazione @Order
public class TestSecurityRoleAdmin {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac; // Contesto dell'applicazione web

    @BeforeEach // Metodo da eseguire prima di ogni test
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac) // Configura MockMvc con il contesto dell'applicazione web
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(user("Admin").roles("ADMIN"))) // Imposta il contesto di sicurezza per l'utente Admin con ruolo ADMIN
                .apply(springSecurity()) // Attiva la sicurezza
                .build();
    }

    private final String ApiBaseUrl = "/api/articoli"; // Base URL per l'API degli articoli

    @Order(1)
    @Test
    public void testListArtByCodArt() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/5002000301")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica che la risposta sia 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonData.GetArtData())) // Verifica che la risposta JSON sia corretta
                .andReturn();
    }

    @Order(2)
    @Test
    public void testInsArticolo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData.GetTestArtData()) // Dati JSON per il test
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // Verifica che la risposta sia 201 Created
                .andExpect(jsonPath("$.data").value(LocalDate.now().toString())) // Verifica che la data nella risposta sia quella attuale
                .andExpect(jsonPath("$.message").value("Inserimento Articolo Eseguito con successo!")) // Verifica il messaggio di successo
                .andDo(print());
    }

    @Order(3)
    @Test
    public void testDelArticolo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/elimina/500123111")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica che la risposta sia 200 OK
                .andExpect(jsonPath("$.code").value("200 OK")) // Verifica il codice di successo nella risposta
                .andExpect(jsonPath("$.message").value("Eliminazione Articolo 500123111 Eseguita Con Successo")) // Verifica il messaggio di successo
                .andDo(print());
    }
}