package com.xantrix.webapp.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe di eccezione personalizzata che rappresenta una situazione in cui un elemento richiesto non è stato trovato.
 * Estende la classe Exception di Java.
 */
@Getter
@Setter
public class NotFoundException extends Exception {
    /**
     * Il numero di serie univoco associato alla classe seriale per scopi di serializzazione e deserializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Il messaggio di errore predefinito da visualizzare quando viene sollevata l'eccezione.
     */
    private String messaggio = "Elemento Richiesto Non Trovato!";

    /**
     * Costruttore di default che inizializza l'eccezione con il messaggio di errore predefinito.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Costruttore che accetta un messaggio di errore personalizzato.
     *
     * @param message il messaggio di errore personalizzato da associare all'eccezione.
     */
    public NotFoundException(String message) {
        super(message);
        this.messaggio = message;
    }
}
