package com.xantrix.webapp;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        // mappatura con metodo
        modelMapper.addMappings( articoliMapping );

        // mappatura con PropertyMap interna al metodo
        modelMapper.addMappings( new PropertyMap<Articoli, ArticoliDto>()
        {
            @Override
            protected void configure()
            {
                map().setIdStatoArt( source.getIdStatoArt() );
            }
        } );

        modelMapper.addConverter( articoliConverter );

        return modelMapper;
    }

    PropertyMap<Articoli, ArticoliDto> articoliMapping = new PropertyMap<>() {
        protected void configure() {
            map().setDataCreazione( source.getDataCreaz() );
        }
    };

    // Convertitore per eliminare gli spazi vuoti o i null
    // Verifica se la sorgente è null e in caso la sostituisce con una stringa vuota
    // Verrà eliminato qualsiasi spazio vuoto per evitare dei problemi nella gestione degli unit test
    Converter<String, String> articoliConverter = context -> context.getSource() == null ? "" : context.getSource().trim();
}
