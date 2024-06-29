package com.xantrix.webapp.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Servizio personalizzato per il caricamento dei dettagli utente.
 * Implementa UserDetailsService per integrare con Spring Security.
 */
@Service("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserConfig config;

    /**
     * Carica i dettagli dell'utente dato lo username.
     *
     * @param userId Lo username dell'utente da caricare
     * @return Un oggetto UserDetails contenente i dettagli dell'utente
     * @throws UsernameNotFoundException Se l'utente non viene trovato o se lo username non è valido
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        String errMsg;

        if (userId == null || userId.length() < 2) {
            errMsg = "Nome utente assente o non valido";
            logger.warn(errMsg);
            throw new UsernameNotFoundException(errMsg);
        }

        Utenti utente = this.getHttpValue(userId);

        if (utente == null) {
            errMsg = String.format("Utente %s non Trovato!!", userId);
            logger.warn(errMsg);
            throw new UsernameNotFoundException(errMsg);
        }

        // Costruisce l'oggetto UserDetails
        return org.springframework.security.core.userdetails.User.withUsername(utente.getUserId())
                .disabled(!utente.getAttivo().equals("Si"))
                .password(utente.getPassword())
                .authorities(utente.getRuoli().stream().map(a -> "ROLE_" + a).toArray(String[]::new))
                .build();
    }

    /**
     * Recupera i dettagli dell'utente da un servizio HTTP esterno.
     *
     * @param userId Lo username dell'utente da recuperare
     * @return Un oggetto Utenti contenente i dettagli dell'utente, o null se non trovato
     */
    private Utenti getHttpValue(String userId) {
        URI url = null;

        try {
            String srvUrl = config.getSrvUrl();
            url = new URI(srvUrl + userId);
        } catch (URISyntaxException e) {
            logger.error("Errore nella creazione dell'URI", e);
            return null;
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(config.getUserId(), config.getPassword()));

        try {
            return restTemplate.getForObject(url, Utenti.class);
        } catch (Exception e) {
            String errMsg = "Connessione al Server: " + url + " Fallita!!";
            logger.warn(errMsg, e);
            return null;
        }
    }
}
