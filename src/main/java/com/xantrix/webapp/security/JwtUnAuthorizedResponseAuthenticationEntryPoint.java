package com.xantrix.webapp.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtUnAuthorizedResponseAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    // Numero di versione per la serializzazione
    private static final long serialVersionUID = -8970718410437077606L;

    /**
     * Gestisce le richieste non autenticate.
     * Questo metodo viene chiamato quando un utente tenta di accedere a una risorsa protetta senza fornire credenziali valide.
     *
     * @param request La richiesta HTTP in arrivo
     * @param response La risposta HTTP da inviare
     * @param authException L'eccezione di autenticazione che ha causato questa chiamata
     * @throws IOException Se si verifica un errore durante l'invio della risposta
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Invia una risposta di errore 401 (Non autorizzato) con un messaggio personalizzato
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "DEVI INSERIRE UN TOKEN JWT VALIDO PER POTERTI AUTENTICARE");
    }
}
