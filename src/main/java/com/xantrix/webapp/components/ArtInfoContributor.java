package com.xantrix.webapp.components;

import com.xantrix.webapp.repository.ArticoliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import java.util.Map;


@Component
public class ArtInfoContributor implements InfoContributor {

    // Rotta da contattare per visualizzare le informazioni aggiuntive
    // http://localhost:{la tua porta}/actuator/info

    private final ArticoliRepository articoliRepository;

    @Autowired
    public ArtInfoContributor( ArticoliRepository articoliRepository ) {
        this.articoliRepository = articoliRepository;
    }

    @Override
    public void contribute( Info.Builder builder ) {
        long quantitaArticoli = articoliRepository.findAll().size();

        //OGGETTO PRINCIPALE INFO
        Map<String, Object> info = new HashMap<>();

        //OGGETTO INFORMAZIONI ARTICOLI
        Map<String, Object> artMap = new HashMap<>();
        artMap.put("Quantità Articoli", quantitaArticoli);

        //OGGETTO INFORMAZIONI ASSISTENZA
        Map<String, Object> assistenza = new HashMap<>();
        assistenza.put( "Nome", "Adiener Lopez Velazquez" );
        assistenza.put( "Email", "adienerlopez@gmail.com");
        assistenza.put("Telefono", "02-12345678");
        assistenza.put( "orario", "9-20" );

        //OGGETTO INFORMAZIONI NOME APP
        Map<String, Object> nome = new HashMap<>();
        nome.put("Nome app", "Articoli Web Service - Versione Con authorizzazione JWT");

        //AGGIUNTA DEI SINGOLI OGGETTI ALL'OGGETTO PRINCIPALE INFO
        info.put( "Articoli", artMap);
        info.put( "Assistenza", assistenza);
        info.put("Nome", nome);

        builder.withDetail("Info", info);
    }
}
