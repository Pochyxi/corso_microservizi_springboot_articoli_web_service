package com.xantrix.webapp.tests.ControllerTests;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SuppressWarnings("LossyEncoding") // Suppress warnings related to lossy encoding
@ContextConfiguration(classes = Application.class) // Configure the context for the application
@SpringBootTest // Indicates that this is a Spring Boot test
@TestMethodOrder(OrderAnnotation.class) // Order the tests according to the @Order annotation
public class SelectArtTest {

	// MockMvc object to perform HTTP requests in tests
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac; // Web application context

	@BeforeEach // Method to be executed before each test
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac) // Configure MockMvc with the web application context
				.build();
	}

	// JSON data for an article
	String JsonData =
			"{\n" +
					"    \"codArt\": \"50056665\",\n" +
					"    \"descrizione\": \"ARTICOLO DI TEST\",\n" +
					"    \"um\": \"PZ\",\n" +
					"    \"codStat\": \"123\",\n" +
					"    \"pzCart\": 6,\n" +
					"    \"pesoNetto\": 1.75,\n" +
					"    \"idStatoArt\": \"1\",\n" +
					"    \"dataCreazione\": \"2024-06-15\",\n" +
					"    \"barcode\": [\n" +
					"        {\n" +
					"            \"barcode\": \"8008490000023\",\n" +
					"            \"idTipoArt\": \"CP\"\n" +
					"        }\n" +
					"    ],\n" +
					"    \"famAssort\": {\n" +
					"        \"id\": 10,\n" +
					"        \"descrizione\": \"DROGHERIA CHIMICA\"\n" +
					"    },\n" +
					"    \"ingredienti\": null,\n" +
					"    \"iva\": {\n" +
					"        \"idIva\": 22,\n" +
					"        \"descrizione\": \"IVA RIVENDITA 22%\",\n" +
					"        \"aliquota\": 22\n" +
					"    }\n" +
					"}";

	@Test
	@Order(1) // Order in which this test should be executed
	public void listArtByEan() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/8008490000023")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // Expect HTTP status 200
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				// Validate various fields in the JSON response
				.andExpect(jsonPath("$.codArt").exists())
				.andExpect(jsonPath("$.codArt").value("50056665"))
				.andExpect(jsonPath("$.descrizione").exists())
				.andExpect(jsonPath("$.descrizione").value("ARTICOLO DI TEST"))
				.andExpect(jsonPath("$.um").exists())
				.andExpect(jsonPath("$.um").value("PZ"))
				.andExpect(jsonPath("$.codStat").exists())
				.andExpect(jsonPath("$.codStat").value("123"))
				.andExpect(jsonPath("$.pzCart").exists())
				.andExpect(jsonPath("$.pzCart").value("6"))
				.andExpect(jsonPath("$.pesoNetto").exists())
				.andExpect(jsonPath("$.pesoNetto").value("1.75"))
				.andExpect(jsonPath("$.idStatoArt").exists())
				.andExpect(jsonPath("$.idStatoArt").value("1"))
				.andExpect(jsonPath("$.dataCreazione").exists())
				.andExpect(jsonPath("$.dataCreazione").value("2024-06-15"))
				// Validate barcode fields
				.andExpect(jsonPath("$.barcode[0].barcode").exists())
				.andExpect(jsonPath("$.barcode[0].barcode").value("8008490000023"))
				.andExpect(jsonPath("$.barcode[0].idTipoArt").exists())
				.andExpect(jsonPath("$.barcode[0].idTipoArt").value("CP"))
				// Validate famAssort fields
				.andExpect(jsonPath("$.famAssort.id").exists())
				.andExpect(jsonPath("$.famAssort.id").value("10"))
				.andExpect(jsonPath("$.famAssort.descrizione").exists())
				.andExpect(jsonPath("$.famAssort.descrizione").value("DROGHERIA CHIMICA"))
				// Validate ingredienti fields
				.andExpect(jsonPath("$.ingredienti").isEmpty())
				// Validate iva fields
				.andExpect(jsonPath("$.iva.idIva").exists())
				.andExpect(jsonPath("$.iva.idIva").value("22"))
				.andExpect(jsonPath("$.iva.descrizione").exists())
				.andExpect(jsonPath("$.iva.descrizione").value("IVA RIVENDITA 22%"))
				.andExpect(jsonPath("$.iva.aliquota").exists())
				.andExpect(jsonPath("$.iva.aliquota").value("22"))
				.andDo(print()); // Print the request and response details
	}

    @Test
	@Order(2) // Order in which this test should be executed
	public void ErrlistArtByEan() throws Exception {
        // Barcode for testing error scenarios
        String barcode = "8008490002138";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/" + barcode )
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()) // Expect HTTP status 404
				.andExpect(jsonPath("$.code").value(404)) // Validate error code
				.andExpect(jsonPath("$.message").value("L'articolo con barcode " + barcode + " non è stato trovato!")) // Validate error message
				.andDo(print()); // Print the request and response details
	}

	@Test
	@Order(3) // Order in which this test should be executed
	public void listArtByCodArt() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/50056665")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // Expect HTTP status 200
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) // Validate that the JSON response matches JsonData
				.andReturn();
	}

    @Test
	@Order(4) // Order in which this test should be executed
	public void errlistArtByCodArt() throws Exception {
        // CodArt for testing error scenarios
        String codArt = "002000301b";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/" + codArt )
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()) // Expect HTTP status 404
				.andExpect(jsonPath("$.code").value(404)) // Validate error code
				.andExpect(jsonPath("$.message").value("L'articolo con codice " + codArt + " non è stato trovato!")) // Validate error message
				.andDo(print()); // Print the request and response details
	}

	// JSON data for a list of articles
	private final String JsonData2 = "[" + JsonData + "]";

	@Test
	@Order(5) // Order in which this test should be executed
	public void listArtByDesc() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/Articolo di Test")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // Expect HTTP status 200
				.andExpect(jsonPath("$", hasSize(1))) // Expect the JSON array to have a size of 1
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData2)) // Validate that the JSON response matches JsonData2
				.andReturn();
	}

	@Test
	@Order(6) // Order in which this test should be executed
	public void errlistArtByDesc() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/ACQUA ULIVETO 15 LTb")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()) // Expect HTTP status 404
				.andExpect(jsonPath("$.code").value(404)) // Validate error code
				.andExpect(jsonPath("$.message").value("L'articolo con descrizione ACQUA ULIVETO 15 LTb non è stato trovato!")) // Validate error message
				.andDo(print()); // Print the request and response details
	}
}
