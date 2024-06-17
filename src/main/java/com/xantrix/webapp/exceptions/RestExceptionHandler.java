package com.xantrix.webapp.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Questa classe gestisce le eccezioni che si verificano nelle richieste REST
 * e restituisce una risposta appropriata nel formato JSON.
 * Estende la classe ResponseEntityExceptionHandler di Spring per gestire
 * le eccezioni in modo centralizzato.
 */
@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{

    /**
     * Questo metodo gestisce l'eccezione NotFoundException e restituisce
     * una risposta di errore nel formato JSON.
     *
     * @param ex l'eccezione NotFoundException sollevata
     * @return un oggetto ResponseEntity contenente un oggetto ErrorResponse
     *         con i dettagli dell'errore
     */
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(NotFoundException ex)
    {
        // Crea un nuovo oggetto ErrorResponse
        ErrorResponse errore = new ErrorResponse();

        // Imposta il codice di errore HTTP 404 (Not Found)
        errore.setCode(HttpStatus.NOT_FOUND.value());
        // Imposta il messaggio di errore dall'eccezione
        errore.setMessage(ex.getMessage());

        // Crea un nuovo oggetto ResponseEntity con l'oggetto ErrorResponse
        // come corpo della risposta, nuove intestazioni HTTP e lo stato HTTP 404
        return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Questo metodo gestisce l'eccezione BindingException e restituisce
     * una risposta di errore nel formato JSON.
     *
     * @param ex l'eccezione BindingException sollevata
     * @return un oggetto ResponseEntity contenente un oggetto ErrorResponse
     *         con i dettagli dell'errore
     */
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<ErrorResponse> exceptionBindingHandler(Exception ex)
    {
        // Crea un nuovo oggetto ErrorResponse
        ErrorResponse errore = new ErrorResponse();

        // Imposta il codice di errore HTTP 400 (Bad Request)
        errore.setCode(HttpStatus.BAD_REQUEST.value());
        // Imposta il messaggio di errore dall'eccezione
        errore.setMessage(ex.getMessage());

        // Crea un nuovo oggetto ResponseEntity con l'oggetto ErrorResponse
        return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> exceptionDuplicateHandler(Exception ex)
    {
        // Crea un nuovo oggetto ErrorResponse
        ErrorResponse errore = new ErrorResponse();

        // Imposta il codice di errore HTTP 406 (Not Acceptable)
        errore.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        // Imposta il messaggio di errore dall'eccezione
        errore.setMessage(ex.getMessage());

        // Crea un nuovo oggetto ResponseEntity con l'oggetto ErrorResponse
        return new ResponseEntity<>(errore, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }
}



