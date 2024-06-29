package com.xantrix.webapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gestore personalizzato per le eccezioni di accesso negato.
 * Implementa AccessDeniedHandler per gestire le risposte quando l'accesso è negato.
 */
@Log
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /** ObjectMapper per la serializzazione JSON */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gestisce la risposta quando l'accesso è negato.
     *
     * @param request La richiesta HTTP che ha causato l'eccezione
     * @param response La risposta HTTP da modificare
     * @param exception L'eccezione AccessDeniedException che è stata lanciata
     * @throws IOException Se si verifica un errore di I/O durante la scrittura della risposta
     * @throws ServletException Se si verifica un errore durante la gestione della richiesta
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        // Messaggio di errore da restituire all'utente
        String errMsg = "Privilegi Insufficienti. Impossibile Proseguire.";

        // Imposta lo status HTTP a 403 Forbidden
        HttpStatus httpStatus = HttpStatus.FORBIDDEN; // 403
        response.setStatus(httpStatus.value());

        // Registra il messaggio di errore nei log
        log.warning(errMsg);

        // Nota: la seguente riga è commentata nel codice originale
        // Map<String, Object> data = new HashMap<>();

        // Scrive il messaggio di errore come stringa JSON nel corpo della risposta
        response
                .getOutputStream()
                .println(objectMapper.writeValueAsString(errMsg));
    }
}
