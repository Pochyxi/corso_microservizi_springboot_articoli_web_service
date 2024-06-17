package com.xantrix.webapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Questa classe rappresenta una risposta di errore che può essere utilizzata
 * in combinazione con l'eccezione NotFoundException per fornire informazioni
 * dettagliate sull'errore.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    /**
     * La data in cui si è verificato l'errore.
     * -- GETTER --
     * Restituisce la data in cui si è verificato l'errore.
     * <p>
     * <p>
     * -- SETTER --
     * Imposta la data in cui si è verificato l'errore.
     *
     * @return la data dell'errore
     * @param date la data dell'errore
     */
    private LocalDate date;

    /**
     * Un codice numerico che identifica il tipo di errore.
     * -- SETTER --
     * Imposta il codice numerico che identifica il tipo di errore.
     * <p>
     * <p>
     * -- GETTER --
     * Restituisce il codice numerico che identifica il tipo di errore.
     *
     * @param code il codice di errore
     * @return il codice di errore
     */
    private int code;

    /**
     * Un messaggio di testo che descrive l'errore in dettaglio.
     */
    private String message;
}

