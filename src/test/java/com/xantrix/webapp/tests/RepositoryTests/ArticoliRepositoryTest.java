package com.xantrix.webapp.tests.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
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


@SpringBootTest()
@TestMethodOrder(OrderAnnotation.class)
public class ArticoliRepositoryTest
{
	
	@Autowired
	private ArticoliRepository articoliRepository;
	
	@Test
	@Order(1)
	public void TestInsArticolo()
	{
		Date date = new Date();
		
		//CLASSE ENTITY ARTICOLI
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
		
		//CLASSE ENTITY FAMASSORT
		FamAssort famAssort = new FamAssort();
		famAssort.setId(1);
		articolo.setFamAssort(famAssort);
		
		//CLASSE ENTITY BARCODE
		Set<Barcode> EAN = new HashSet<>();
		EAN.add(new Barcode("12345678", "CP", articolo));
		articolo.setBarcode(EAN);
		
		//CLASSE ENTITY IVA
		Iva iva = new Iva();
		iva.setIdIva(22);
		articolo.setIva(iva);
		
		//CLASSE ENTITY ingredienti
		Ingredienti ingredienti = new Ingredienti();
		ingredienti.setCodArt("50056665500");
		ingredienti.setInfo("Test inserimento ingredienti");
		
		articoliRepository.save(articolo);
		
		assertThat(articoliRepository.findByCodArt("50056665500"))
			.extracting(Articoli::getDescrizione)
			.isEqualTo("ACQUA ULIVETO 15 LT");
	}

	@Test
	@Order(2)
	public void TestfindByDescrizioneLike()
	{
		List<Articoli> items = articoliRepository.selByDescrizioneLike("ACQUA ULIVETO%");
		assertEquals(3, items.size());
	}
	
	@Test
	@Order(3)
	public void TestfindByDescrizioneLikePage()
	{
		List<Articoli> items = articoliRepository.findByDescrizioneLike("ACQUA%",PageRequest.of(0, 10));
		assertEquals(10, items.size());
	}

	
	@Test
	@Order(4)
	public void TestfindByCodArt() throws Exception
	{
		assertThat(articoliRepository.findByCodArt("50056665500"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
				
	}

	@Test
	@Order( 5 )
	public void TestFindByBarcode()
	{
		assertThat(articoliRepository.selByEan("12345678"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
	}
	

	@Test
	@Order(5)
//	@Disabled
	public void TestDelArt() throws Exception
	{
		
		articoliRepository.delete(articoliRepository.findByCodArt("50056665500"));
		
		assertThat(articoliRepository.findByCodArt("50056665500")).isNull();
				
	}
}
