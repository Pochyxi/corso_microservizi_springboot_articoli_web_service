package com.xantrix.webapp;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedirectConfig {

    /**
     * Configura il server Tomcat embedded per gestire il reindirizzamento da HTTP a HTTPS.
     *
     * @return ServletWebServerFactory configurato
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                // Crea un vincolo di sicurezza per forzare l'uso di HTTPS
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");

                // Applica il vincolo di sicurezza a tutti i percorsi
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);

                // Aggiunge il vincolo di sicurezza al contesto
                context.addConstraint(securityConstraint);
            }
        };

        // Aggiunge un connettore aggiuntivo per gestire il reindirizzamento
        tomcat.addAdditionalTomcatConnectors(redirectConnector());

        return tomcat;
    }

    /**
     * Crea un connettore HTTP che reindirizza al porto HTTPS.
     *
     * @return Connector configurato per il reindirizzamento
     */
    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(5051);        // Porta HTTP
        connector.setSecure(false);
        connector.setRedirectPort(5443); // Porta HTTPS a cui reindirizzare

        return connector;
    }
}
