package com.xantrix.webapp.tests.SecurityTests;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.utility.JsonData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
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
public class TestSecurityRoleUser {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac; // Contesto dell'applicazione web

	@BeforeEach // Metodo da eseguire prima di ogni test
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(wac) // Configura MockMvc con il contesto dell'applicazione web
				.defaultRequest(get("/")
						.with(user("Nicola").roles("USER"))) // Imposta il contesto di sicurezza per l'utente Nicola con ruolo USER
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
	public void testErrRoleInsArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData.GetTestArtData()) // Dati JSON per il test
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()) // Verifica che la risposta sia 403 Forbidden
				.andDo(print());
	}
}
