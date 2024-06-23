package com.xantrix.webapp.exceptions;

import lombok.Getter;
import lombok.Setter;

// Annotazioni Lombok per generare automaticamente i metodi getter e setter
@Getter
@Setter
public class DuplicateException extends Exception
{
    // serialVersionUID è utilizzato per la serializzazione della classe
    private static final long serialVersionUID = 1L;

    // Campo per memorizzare il messaggio di errore
    private String messaggio;

    // Costruttore che accetta un messaggio di errore come parametro
    public DuplicateException(String messaggio) {
        // Chiama il costruttore della superclasse (Exception) con il messaggio di errore
        super(messaggio);
        // Assegna il messaggio al campo della classe
        this.messaggio = messaggio;
    }

    // Costruttore senza parametri
    public DuplicateException() {
        // Chiama il costruttore senza parametri della superclasse (Exception)
        super();
    }
}
