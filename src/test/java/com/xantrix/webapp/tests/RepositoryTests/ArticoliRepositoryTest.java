package com.xantrix.webapp.tests.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
 
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.entities.Barcode;
import com.xantrix.webapp.entities.FamAssort;
import com.xantrix.webapp.entities.Ingredienti;
import com.xantrix.webapp.entities.Iva;
import com.xantrix.webapp.repository.ArticoliRepository;


@SpringBootTest // Indica che si tratta di un test Spring Boot
@TestMethodOrder(OrderAnnotation.class) // Ordina i test secondo l'annotazione @Order
public class ArticoliRepositoryTest {

	@Autowired
	private ArticoliRepository articoliRepository; // Repository degli articoli

	@Test
	@Order(1) // Indica l'ordine in cui deve essere eseguito questo test
	public void TestInsArticolo() {
		Date date = new Date();

		// Classe entity Articoli
		Articoli articolo = new Articoli(
				"50056665500",
				"ACQUA ULIVETO 15 LT",
				"PZ",
				"123",
				6,
				1.75,
				"1",
				date,
				null,
				null,
				null,
				null
		);

		// Classe entity FamAssort
		FamAssort famAssort = new FamAssort();
		famAssort.setId(1);
		articolo.setFamAssort(famAssort);

		// Classe entity Barcode
		Set<Barcode> EAN = new HashSet<>();
		EAN.add(new Barcode("12345678", "CP", articolo));
		articolo.setBarcode(EAN);

		// Classe entity Iva
		Iva iva = new Iva();
		iva.setIdIva(22);
		articolo.setIva(iva);

		// Classe entity Ingredienti
		Ingredienti ingredienti = new Ingredienti();
		ingredienti.setCodArt("50056665500");
		ingredienti.setInfo("Test inserimento ingredienti");
		articolo.setIngredienti(ingredienti);

		// Salva l'articolo nel repository
		articoliRepository.save(articolo);

		// Verifica che l'articolo sia stato effettivamente inserito nel repository
		assertThat(articoliRepository.findByCodArt("50056665500"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
	}

	@Test
	@Order(2) // Indica l'ordine in cui deve essere eseguito questo test
	public void TestfindByDescrizioneLike() {
		// Trova articoli la cui descrizione è simile a "ACQUA ULIVETO%"
		List<Articoli> items = articoliRepository.selByDescrizioneLike("ACQUA ULIVETO%");
		// Verifica che il numero di articoli trovati sia 3
		assertEquals(3, items.size());
	}

	@Test
	@Order(3) // Indica l'ordine in cui deve essere eseguito questo test
	public void TestfindByDescrizioneLikePage() {
		// Trova articoli la cui descrizione è simile a "ACQUA%", con paginazione
		List<Articoli> items = articoliRepository.findByDescrizioneLike("ACQUA%", PageRequest.of(0, 10));
		// Verifica che il numero di articoli trovati sia 10
		assertEquals(10, items.size());
	}

	@Test
	@Order(4) // Indica l'ordine in cui deve essere eseguito questo test
	public void TestfindByCodArt() {
		// Verifica che l'articolo con codice "50056665500" sia stato trovato e la descrizione sia corretta
		assertThat(articoliRepository.findByCodArt("50056665500"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
	}

	@Test
	@Order(5) // Indica l'ordine in cui deve essere eseguito questo test
	public void TestFindByBarcode() {
		// Verifica che l'articolo con barcode "12345678" sia stato trovato e la descrizione sia corretta
		assertThat(articoliRepository.selByEan("12345678"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
	}

	@Test
	@Order(6) // Indica l'ordine in cui deve essere eseguito questo test
	public void TestDelArt() {
		// Elimina l'articolo con codice "50056665500" dal repository
		articoliRepository.delete(articoliRepository.findByCodArt("50056665500"));
		// Verifica che l'articolo sia stato effettivamente eliminato
		assertThat(articoliRepository.findByCodArt("50056665500")).isNull();
	}
}
