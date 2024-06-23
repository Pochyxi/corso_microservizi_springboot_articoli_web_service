package com.xantrix.webapp.security;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

// Annotazione Lombok per abilitare il logging
@Log
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    // Nome del realm utilizzato per l'autenticazione
    private static final String REALM = "REAME";

    // Metodo chiamato quando un'eccezione di autenticazione si verifica
    @Override
    @SneakyThrows
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) {

        // Messaggio di errore da restituire al client
        String ErrMsg = "Userid e/o Password non validi!";

        // Log dell'errore di sicurezza
        log.warning("Errore di sicurezza: " + authException.getMessage());

        // Imposta lo stato della risposta a 401 (Non autorizzato)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Aggiunge l'intestazione WWW-Authenticate alla risposta
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() );

        // Scrive il messaggio di errore nella risposta
        PrintWriter writer = response.getWriter();
        writer.println(ErrMsg);
    }

    // Metodo chiamato dopo l'inizializzazione delle proprietà
    @Override
    @SneakyThrows
    public void afterPropertiesSet() {
        // Imposta il nome del realm
        setRealmName(REALM);
        super.afterPropertiesSet();
    }
}
