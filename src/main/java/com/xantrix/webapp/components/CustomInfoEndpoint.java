package com.xantrix.webapp.components;

import com.xantrix.webapp.repository.ArticoliRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "customInfo")
@Data
public class CustomInfoEndpoint {

    private final ArticoliRepository articoliRepository;

    @Autowired
    public CustomInfoEndpoint(ArticoliRepository articoliRepository) {
        this.articoliRepository = articoliRepository;
    }

    @ReadOperation
    public Map<String, Object> customInfo() {
        long articoliCount = articoliRepository.count();

        Map<String, Object> artInfo = new HashMap<>();
        artInfo.put("Quantità Articoli", articoliCount);

        return artInfo;
    }

    @ReadOperation
    public String customEndpointByName( @Selector String name ) {
        return String.format("Eseguito metodo GET con parametro: %s", name);
    }

    @WriteOperation
    public String writeOperation(@Selector String name ) {
        return String.format("Eseguito metodo POST con parametro: %s", name);
    }

    @DeleteOperation
    public String deleteOperation(@Selector String name ) {
        return String.format("Eseguito metodo DELETE con parametro: %s", name);
    }
}
