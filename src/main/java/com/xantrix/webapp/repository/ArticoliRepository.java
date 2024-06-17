package com.xantrix.webapp.repository;


import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.entities.Barcode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ArticoliRepository extends PagingAndSortingRepository<Articoli, String>{
    Articoli findByCodArt(String codArt);

    @Query("SELECT a FROM Articoli a JOIN a.barcode b WHERE b.barcode = :ean ORDER BY b.barcode ASC")
    Articoli selByEan( @Param( "ean" ) String ean );

    List<Articoli> findByDescrizioneLike( String descrizione, Pageable pageable);

    @Query(value = "SELECT * FROM ARTICOLI WHERE DESCRIZIONE LIKE :desArt", nativeQuery = true)
    List<Articoli> selByDescrizioneLike( @Param ( "desArt" ) String descrizione);

    @Query(value = "SELECT * FROM ARTICOLI WHERE DESCRIZIONE LIKE :desArt", nativeQuery = true)
    List<Articoli> selByDescrizioneLike( @Param( "desArt" ) String descrizione, Pageable pageable );
}
