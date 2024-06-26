package com.xantrix.webapp.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import java.net.URI;
import java.net.URISyntaxException;

@Service("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserConfig config;

    @Override
    public UserDetails loadUserByUsername( String userId ) throws UsernameNotFoundException {

        String ErrMsg = "";

        if( userId == null || userId.length()  < 2 ) {
            ErrMsg = "Nome utente assente o non valido";

            logger.warn(ErrMsg);

            throw new UsernameNotFoundException(ErrMsg);
        }

        Utenti utente = this.GetHttpValue(userId);

        if( utente == null ) {
            ErrMsg = String.format("Utente %s non Trovato!!", userId);

            logger.warn(ErrMsg);

            throw new UsernameNotFoundException(ErrMsg);
        }

        User.UserBuilder builder = null;
        builder = org.springframework.security.core.userdetails.User.withUsername(utente.getUserId());
        builder.disabled((!utente.getAttivo().equals( "Si" )));
        builder.password(utente.getPassword());

        String[] profili = utente.getRuoli()
                .stream().map(a -> "ROLE_" + a).toArray(String[]::new);

        builder.authorities(profili);

        return builder.build();
    }

    private Utenti GetHttpValue(String UserId) {
        URI url = null;

        try {
            String SrvUrl = config.getSrvUrl();

            url = new URI(SrvUrl + UserId);
        } catch( URISyntaxException e ) {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add( new BasicAuthenticationInterceptor(config.getUserId(), config.getPassword()) );

        Utenti utente = null;

        try {
            utente = restTemplate.getForObject(url, Utenti.class);
        } catch( Exception e ) {
            String ErrMsg = "Connessone al Server: " + url.toString() + " Fallita!!";
            logger.warn(ErrMsg);
        }

        return utente;
    }
}
