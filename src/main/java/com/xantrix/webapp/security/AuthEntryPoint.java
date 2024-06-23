package com.xantrix.webapp.security;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Log
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    private static String REALM = "REAME";

    @Override
    @SneakyThrows
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) {

        String ErrMsg = "Userid e/o Password non validi!";

        log.warning( "Errore di sicurezza: " + authException.getMessage() );

        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        response.addHeader( "WWW-Authenticate", "Basic realm=" + getRealmName() + "" );

        PrintWriter writer = response.getWriter();
        writer.println( ErrMsg );
    }

    @Override
    @SneakyThrows
    public void afterPropertiesSet() {
        setRealmName(REALM);
        super.afterPropertiesSet();
    }
}
