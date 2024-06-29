package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticoliService
{
     List<ArticoliDto> SelByDescrizione( String descrizione, Pageable pageable);

     ArticoliDto SelByCodArt(String codArt);

     Articoli SelByCodArt2(String codArt);

     ArticoliDto SelByBarcode(String barcode);

     void InsArticolo( Articoli articolo);

     void DelArticolo( Articoli articolo );

     void CleanCaches();
}
