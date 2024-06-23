package com.xantrix.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// Abilita la configurazione della sicurezza web
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // Nome del realm utilizzato per l'autenticazione
    private static final String REALM = "REAME";

    // Definisce un bean per l'encoder delle password utilizzando BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Definisce un bean per il servizio UserDetailsService che gestisce gli utenti in memoria
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // Crea un costruttore di utenti
        User.UserBuilder users = User.builder();

        // Crea un gestore degli utenti in memoria
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // Crea un utente admin con ruolo ADMIN
        manager.createUser(users
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .roles("ADMIN")
                .build()
        );

        // Crea un utente normale con ruolo USER
        manager.createUser(users
                .username("user")
                .password(new BCryptPasswordEncoder().encode("user"))
                .roles("USER")
                .build()
        );

        return manager;
    }

    // Definisce le URL protette solo per gli admin
    private static final String[] ADMIN_MATCHER = {"/api/articoli/inserisci/**", "/api/articoli/elimina/**", "/api/articoli/modifica/**"};

    // Configura la sicurezza HTTP
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disabilita la protezione CSRF
                .authorizeRequests()
//                .antMatchers(USER_MATCHER).hasAnyRole("USER", "ADMIN")
                .antMatchers(ADMIN_MATCHER).hasRole("ADMIN") // Permette l'accesso agli admin solo alle URL specificate
                .anyRequest().authenticated() // Richiede autenticazione per ogni altra richiesta
                .and()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint()) // Configura l'autenticazione Basic con il realm specificato
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Configura la gestione della sessione come stateless
    }

    // Definisce un bean per il punto di ingresso dell'autenticazione
    @Bean
    public AuthEntryPoint getBasicAuthEntryPoint() {
        return new AuthEntryPoint();
    }

    // Configura la sicurezza web per ignorare le richieste OPTIONS
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
