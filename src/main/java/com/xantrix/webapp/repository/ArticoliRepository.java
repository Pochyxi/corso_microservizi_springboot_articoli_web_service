package com.xantrix.webapp.repository;


import com.xantrix.webapp.entities.Articoli;
import io.micrometer.core.lang.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Interfaccia del repository per la gestione delle operazioni di persistenza degli articoli
public interface ArticoliRepository extends PagingAndSortingRepository<Articoli, String> {

    @NonNull
    List<Articoli> findAll();

    // Metodo per trovare un articolo in base al codice articolo
    Articoli findByCodArt(String codArt);

    // Query personalizzata per trovare un articolo in base al barcode
    @Query("SELECT a FROM Articoli a JOIN a.barcode b WHERE b.barcode = :ean ORDER BY b.barcode ASC")
    Articoli selByEan(@Param("ean") String ean);

    // Metodo per trovare articoli con una descrizione simile, supporta la paginazione
    List<Articoli> findByDescrizioneLike(String descrizione, Pageable pageable);

    // Query nativa per trovare articoli con una descrizione simile
    @Query(value = "SELECT * FROM ARTICOLI WHERE DESCRIZIONE LIKE :desArt", nativeQuery = true)
    List<Articoli> selByDescrizioneLike(@Param("desArt") String descrizione);

    // Query nativa per trovare articoli con una descrizione simile, supporta la paginazione
    @Query(value = "SELECT * FROM ARTICOLI WHERE DESCRIZIONE LIKE :desArt", nativeQuery = true)
    List<Articoli> selByDescrizioneLike(@Param("desArt") String descrizione, Pageable pageable);
}
