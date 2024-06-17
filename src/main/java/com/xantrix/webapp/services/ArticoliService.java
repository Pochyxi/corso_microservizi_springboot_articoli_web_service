package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticoliService
{
    public List<ArticoliDto> SelByDescrizione( String descrizione, Pageable pageable);

    public ArticoliDto SelByCodArt(String codArt);

    public Articoli SelByCodArt2(String codArt);

    public ArticoliDto SelByBarcode(String barcode);

    public void InsArticolo( Articoli articolo);

    void DelArticolo( Articoli articolo );

    public void CleanCaches();
}
