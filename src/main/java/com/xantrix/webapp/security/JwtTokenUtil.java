package com.xantrix.webapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility per la gestione dei token JWT.
 */
@Component
public class JwtTokenUtil implements Serializable {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "iat";
    private static final long serialVersionUID = -3301605591108950415L;

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * Estrae lo username dal token JWT.
     *
     * @param token Il token JWT
     * @return Lo username contenuto nel token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Estrae la data di emissione dal token JWT.
     *
     * @param token Il token JWT
     * @return La data di emissione del token
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * Estrae la data di scadenza dal token JWT.
     *
     * @param token Il token JWT
     * @return La data di scadenza del token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Estrae un claim specifico dal token JWT.
     *
     * @param token Il token JWT
     * @param claimsResolver Una funzione per estrarre il claim desiderato
     * @return Il claim estratto
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims dal token JWT.
     *
     * @param token Il token JWT
     * @return Tutti i claims contenuti nel token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica se il token è scaduto.
     *
     * @param token Il token JWT
     * @return true se il token è scaduto, false altrimenti
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date(System.currentTimeMillis()));
    }

    /**
     * Valida il token JWT.
     *
     * @param token Il token JWT
     * @param userDetails I dettagli dell'utente
     * @return true se il token è valido, false altrimenti
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
