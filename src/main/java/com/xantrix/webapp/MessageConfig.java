package com.xantrix.webapp;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

// Indica che questa classe è una configurazione Spring
@Configuration
public class MessageConfig extends ResourceBundleMessageSource {

    // Configura il bean per il validatore locale
    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource()); // Imposta la sorgente dei messaggi di validazione

        return bean;
    }

    // Configura il bean per la sorgente dei messaggi
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:messages"); // Imposta il percorso base per i file dei messaggi
        resource.setDefaultEncoding("UTF-8"); // Imposta la codifica predefinita

        return resource;
    }

    // Configura il bean per il risolutore di locale
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        // Utile per ricavare automaticamente la lingua del client
        sessionLocaleResolver.setDefaultLocale(LocaleContextHolder.getLocale());

        // Dato che abbiamo solo il file it, forziamo la lingua italiana
        // sessionLocaleResolver.setDefaultLocale(new Locale("it"));

        return sessionLocaleResolver;
    }
}
