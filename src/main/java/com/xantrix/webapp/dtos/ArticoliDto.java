package com.xantrix.webapp.dtos;

import com.xantrix.webapp.entities.Ingredienti;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ArticoliDto implements Serializable
{

    private static final long serialVersionUID = -8822233445272993996L;

    private String codArt;
    private String descrizione;
    private String um;
    private String codStat;
    private Integer pzCart;
    private double pesoNetto;
    private String idStatoArt;
    private Date dataCreazione;
    private double prezzo = 0;

    private Set<BarcodeDto> barcode = new HashSet<>();
//    private IngredientiDto ingredienti;
    private CategoriaDto famAssort;
    private IvaDto iva;
    private IngredientiDto ingredienti;
}
