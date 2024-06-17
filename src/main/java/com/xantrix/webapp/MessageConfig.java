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

import java.util.Locale;

@Configuration
public class MessageConfig extends ResourceBundleMessageSource
{
    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator()
    {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());

        return bean;
    }

    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();

        resource.setBasename("classpath:messages");
        resource.setDefaultEncoding("UTF-8");

        return resource;
    }

    @Bean
    public LocaleResolver localeResolver()
    {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//        Utile per ricavare automaticamente la lingua del client
        sessionLocaleResolver.setDefaultLocale( LocaleContextHolder.getLocale() );

//        Dato che abbiamo solo il file it forziamo la lingua italiana
//        sessionLocaleResolver.setDefaultLocale( new Locale("it") );

        return sessionLocaleResolver;
    }
}
