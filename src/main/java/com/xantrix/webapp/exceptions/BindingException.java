package com.xantrix.webapp.exceptions;

// Classe BindingException che estende la classe Exception per gestire le eccezioni specifiche di binding
public class BindingException extends Exception
{
    // serialVersionUID è utilizzato per la serializzazione della classe
    private static final long serialVersionUID = 1L;

    // Costruttore che accetta un messaggio di errore come parametro
    public BindingException(String messaggio) {
        // Chiama il costruttore della superclasse (Exception) con il messaggio di errore
        super(messaggio);
        // Assegna il messaggio al campo della classe
        // Campo per memorizzare il messaggio di errore
    }

    // Costruttore senza parametri
    public BindingException() {
        // Chiama il costruttore senza parametri della superclasse (Exception)
        super();
    }
}
