package com.xantrix.webapp.tests.ControllerTests;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ContextConfiguration(classes = Application.class) // Configura il contesto dell'applicazione per i test
@SpringBootTest // Indica che si tratta di un test Spring Boot
@TestMethodOrder(OrderAnnotation.class) // Ordina i test secondo l'annotazione @Order
public class InsertArtTest {

	// Oggetto MockMvc per eseguire le richieste HTTP nei test
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac; // Contesto dell'applicazione web

	@Autowired
	ArticoliRepository articoliRepository; // Repository degli articoli

	@BeforeEach // Metodo da eseguire prima di ogni test
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac) // Configura MockMvc con il contesto dell'applicazione web
				.build();
	}

	// Dati JSON per l'inserimento di un nuovo articolo
	private final String JsonData =
			"{\r\n"
					+ "    \"codArt\": \"5001233434\",\r\n"
					+ "    \"descrizione\": \"Articolo Unit Test Inserimento\",\r\n"
					+ "    \"um\": \"PZ\",\r\n"
					+ "    \"codStat\": \"TESTART\",\r\n"
					+ "    \"pzCart\": 6,\r\n"
					+ "    \"pesoNetto\": 1.75,\r\n"
					+ "    \"idStatoArt\": \"1\",\r\n"
					+ "    \"dataCreaz\": \"2019-05-14\",\r\n"
					+ "    \"barcode\": [\r\n"
					+ "        {\r\n"
					+ "            \"barcode\": \"12345678\",\r\n"
					+ "            \"idTipoArt\": \"CP\"\r\n"
					+ "        }\r\n"
					+ "    ],\r\n"
					+ "    \"ingredienti\": {\r\n"
					+ "        \"codArt\" : \"5001233434\",\r\n"
					+ "        \"info\" : \"TEST INGREDIENTI\"\r\n"
					+ "    },\r\n"
					+ "    \"iva\": {\r\n"
					+ "        \"idIva\": 22\r\n"
					+ "    },\r\n"
					+ "    \"famAssort\": {\r\n"
					+ "        \"id\": 1\r\n"
					+ "    }\r\n"
					+ "}";

	@Test
	@Order(1) // Indica l'ordine in cui deve essere eseguito questo test
	public void testInsArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci") // Esegue una richiesta POST per inserire un articolo
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()) // Verifica che la risposta sia 201 Created
				.andExpect(jsonPath("$.data").value(LocalDate.now().toString())) // Verifica che la data nella risposta sia quella attuale
				.andExpect(jsonPath("$.message").value("Inserimento Articolo Eseguito con successo!")) // Verifica il messaggio di successo
				.andDo(print());

		assertThat(articoliRepository.findByCodArt("5001233434")) // Verifica che l'articolo sia stato effettivamente inserito nel repository
				.extracting(Articoli::getDescrizione)
				.isEqualTo("Articolo Unit Test Inserimento");
	}

	@Test
	@Order(2) // Indica l'ordine in cui deve essere eseguito questo test
	public void testErrInsArticolo1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci") // Esegue una richiesta POST con un articolo duplicato
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable()) // Verifica che la risposta sia 406 Not Acceptable
				.andExpect(jsonPath("$.code").value(406)) // Verifica il codice di errore nella risposta
				.andExpect(jsonPath("$.message").value("Articolo 5001233434 presente in anagrafica!")) // Verifica il messaggio di errore
				.andDo(print());
	}

	// Dati JSON per l'inserimento di un articolo con un errore di validazione (descrizione assente)
	String ErrJsonData =
			"{\r\n"
					+ "    \"codArt\": \"5001233434\",\r\n"
					+ "    \"descrizione\": \"\",\r\n" //<-- Descrizione Assente
					+ "    \"um\": \"PZ\",\r\n"
					+ "    \"codStat\": \"TESTART\",\r\n"
					+ "    \"pzCart\": 6,\r\n"
					+ "    \"pesoNetto\": 1.75,\r\n"
					+ "    \"idStatoArt\": \"1\",\r\n"
					+ "    \"dataCreaz\": \"2019-05-14\",\r\n"
					+ "    \"barcode\": [\r\n"
					+ "        {\r\n"
					+ "            \"barcode\": \"12345678\",\r\n"
					+ "            \"idTipoArt\": \"CP\"\r\n"
					+ "        }\r\n"
					+ "    ],\r\n"
					+ "    \"ingredienti\": {\r\n"
					+ "        \"codArt\" : \"123Test\",\r\n"
					+ "        \"info\" : \"TEST INGREDIENTI\"\r\n"
					+ "    },\r\n"
					+ "    \"iva\": {\r\n"
					+ "        \"idIva\": 22\r\n"
					+ "    },\r\n"
					+ "    \"famAssort\": {\r\n"
					+ "        \"id\": 1\r\n"
					+ "    }\r\n"
					+ "}";

	@Test
	@Order(3) // Indica l'ordine in cui deve essere eseguito questo test
	public void testErrInsArticolo2() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci") // Esegue una richiesta POST con un articolo con errore di validazione
						.contentType(MediaType.APPLICATION_JSON)
						.content(ErrJsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()) // Verifica che la risposta sia 400 Bad Request
				.andExpect(jsonPath("$.code").value(400)) // Verifica il codice di errore nella risposta
				.andExpect(jsonPath("$.message").value("Il campo Descrizione deve avere un numero di caratteri compreso tra 6 e 80")) // Verifica il messaggio di errore
				.andDo(print());
	}

    @Test
	@Order(4) // Indica l'ordine in cui deve essere eseguito questo test
	public void testUpdArticolo() throws Exception {
        // Dati JSON per la modifica di un articolo
        String jsonDataMod = "{\r\n"
                + "    \"codArt\": \"5001233434\",\r\n"
                + "    \"descrizione\": \"Articolo Unit Test Modifica\",\r\n"
                + "    \"um\": \"PZ\",\r\n"
                + "    \"codStat\": \"TESTART\",\r\n"
                + "    \"pzCart\": 6,\r\n"
                + "    \"pesoNetto\": 1.75,\r\n"
                + "    \"idStatoArt\": \"1\",\r\n"
                + "    \"dataCreaz\": \"2019-05-14\",\r\n"
                + "    \"barcode\": [\r\n"
                + "        {\r\n"
                + "            \"barcode\": \"12345678\",\r\n"
                + "            \"idTipoArt\": \"CP\"\r\n"
                + "        }\r\n"
                + "    ],\r\n"
                + "    \"ingredienti\": {\r\n"
                + "        \"codArt\" : \"123Test\",\r\n"
                + "        \"info\" : \"TEST INGREDIENTI\"\r\n"
                + "    },\r\n"
                + "    \"iva\": {\r\n"
                + "        \"idIva\": 22\r\n"
                + "    },\r\n"
                + "    \"famAssort\": {\r\n"
                + "        \"id\": 1\r\n"
                + "    }\r\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/articoli/modifica") // Esegue una richiesta PUT per modificare un articolo
						.contentType(MediaType.APPLICATION_JSON)
						.content( jsonDataMod )
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()) // Verifica che la risposta sia 201 Created
				.andExpect(jsonPath("$.data").value(LocalDate.now().toString())) // Verifica che la data nella risposta sia quella attuale
				.andExpect(jsonPath("$.message").value("Modifica Articolo Eseguita con successo!")) // Verifica il messaggio di successo
				.andDo(print());

		assertThat(articoliRepository.findByCodArt("5001233434")) // Verifica che l'articolo sia stato effettivamente modificato nel repository
				.extracting(Articoli::getDescrizione)
				.isEqualTo("Articolo Unit Test Modifica");
	}

	@Test
	@Order(5) // Indica l'ordine in cui deve essere eseguito questo test
	public void testDelArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/articoli/elimina/5001233434") // Esegue una richiesta DELETE per eliminare un articolo
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // Verifica che la risposta sia 200 OK
				.andExpect(jsonPath("$.code").value("200 OK")) // Verifica il codice di successo nella risposta
				.andExpect(jsonPath("$.message").value("Eliminazione Articolo 5001233434 Eseguita Con Successo")) // Verifica il messaggio di successo
				.andDo(print());
	}
}
