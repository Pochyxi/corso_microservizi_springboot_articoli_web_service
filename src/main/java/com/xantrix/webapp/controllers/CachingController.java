package com.xantrix.webapp.controllers;

import com.xantrix.webapp.services.ArticoliService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CachingController {

    private final ArticoliService articoliService;

    @GetMapping("clearAllCaches")
    public void clearAllCaches() {
        articoliService.CleanCaches();
    }
}
