package com.xantrix.webapp.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro per l'autorizzazione basata su JWT token.
 * Viene eseguito una volta per ogni richiesta.
 */
@Component
@Log
public class JwtTokenAuthorizationOncePerRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${sicurezza.header}")
    private String tokenHeader;

    /**
     * Esegue il filtro interno per l'autorizzazione JWT.
     *
     * @param request La richiesta HTTP
     * @param response La risposta HTTP
     * @param chain Il FilterChain per l'esecuzione di filtri aggiuntivi
     * @throws ServletException Se si verifica un errore durante l'elaborazione della richiesta
     * @throws IOException Se si verifica un errore di I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info(String.format("Authentication Request For '{%s}'", request.getRequestURL()));

        final String requestTokenHeader = request.getHeader(this.tokenHeader);

        log.warning("Token: " + requestTokenHeader);

        String username = null;
        String jwtToken = null;

        // Estrae il token JWT dall'header della richiesta
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.severe("IMPOSSIBILE OTTENERE LA USERID");
            } catch (ExpiredJwtException e) {
                log.warning("TOKEN SCADUTO");
            }
        } else {
            log.warning("TOKEN NON VALIDO");
        }

        log.warning(String.format("JWT_TOKEN_USERNAME_VALUE '{%s}'", username));

        // Valida il token e configura l'autenticazione di Spring Security
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
