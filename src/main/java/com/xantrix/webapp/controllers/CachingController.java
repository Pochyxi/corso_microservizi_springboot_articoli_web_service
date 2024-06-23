package com.xantrix.webapp.controllers;

import com.xantrix.webapp.services.ArticoliService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CachingController {

    // Il servizio ArticoliService che fornisce i metodi per interagire con gli articoli, inclusa la pulizia della cache
    private final ArticoliService articoliService;

    /**
     * Metodo per pulire tutte le cache.
     * Questo metodo viene mappato alla richiesta GET "clearAllCaches".
     * Quando chiamato, invoca il metodo CleanCaches() del servizio ArticoliService.
     */
    @GetMapping("clearAllCaches")
    public void clearAllCaches() {
        articoliService.CleanCaches();
    }
}
