package com.xantrix.webapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.exceptions.BindingException;
import com.xantrix.webapp.exceptions.DuplicateException;
import com.xantrix.webapp.exceptions.NotFoundException;
import com.xantrix.webapp.services.ArticoliService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/articoli")
@Tag(name = "Articoli API", description = "Articoli API")
@Log
@RequiredArgsConstructor
public class ArticoliController {
    private final ArticoliService articoliService;
    private final ResourceBundleMessageSource errMessage;

    @Operation(
            summary = "Ricerca Articolo per BARCODE",
            description = "Restituisce un articolo in base al suo barcode in formato JSON",
            tags = { "ArticoliDto" }
    )
    @ApiResponses(value =
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Articolo Trovato",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ArticoliDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Articolo Non Trovato"),
                    @ApiResponse(responseCode = "403", description = "Non sei AUTORIZZATO a visualizzare l'articolo"),
                    @ApiResponse(responseCode = "401", description = "Non sei AUTENTICATO!")
            })
    @GetMapping(value = "/cerca/barcode/{barcode}", produces = "application/json")
    // Avendo aggiunto la dipendenza Lombok, non è più necessario scrivere il codice per gestire le eccezioni
    @SneakyThrows
    public ResponseEntity<ArticoliDto> listArtByEan( @Parameter(description = "Il barcode univoco dell'articolo", required = true)
                                                     @PathVariable("barcode") String barcorde
    ) {
        log.info( String.format( "****** Otteniamo l'articolo con barcode %s ******", barcorde ) );

        ArticoliDto articolo = articoliService.SelByBarcode( barcorde );

        if( articolo == null ) {
            String ErrMsg = String.format( "L'articolo con barcode %s non è stato trovato!", barcorde );

            log.warning( ErrMsg );

            throw new NotFoundException( ErrMsg );
        }


        return new ResponseEntity<>( articolo, HttpStatus.OK );

    }

    @GetMapping(value = "/cerca/codice/{codart}", produces = "application/json")
    // Avendo aggiunto la dipendenza Lombok, non è più necessario scrivere il codice per gestire le eccezioni
    @SneakyThrows
    public ResponseEntity<ArticoliDto> listArtByCodArt( @PathVariable("codart") String codArt ) {
        log.info( String.format( "****** Otteniamo l'articolo con codice %s ******", codArt ) );

        ArticoliDto articolo = articoliService.SelByCodArt( codArt );

        if( articolo == null ) {
            String ErrMsg = String.format( "L'articolo con codice %s non è stato trovato!", codArt );

            log.warning( ErrMsg );

            throw new NotFoundException( ErrMsg );
        }

        return new ResponseEntity<>( articolo, HttpStatus.OK );
    }

    @GetMapping(value = "/cerca/descrizione/{filter}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<ArticoliDto>> listArtByDesc( @PathVariable("filter") String filter ) {
        log.info( String.format( "****** Otteniamo l'articolo con descrizione %s ******", filter ) );

        List<ArticoliDto> articolo = articoliService.SelByDescrizione( filter, null );

        if( articolo.isEmpty() ) {
            String ErrMsg = String.format( "L'articolo con descrizione %s non è stato trovato!", filter );

            log.warning( ErrMsg );

            throw new NotFoundException( ErrMsg );
        }

        return new ResponseEntity<>( articolo, HttpStatus.OK );
    }

    @PostMapping(value = "/inserisci", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<InfoMsg> createArt( @Valid @RequestBody Articoli articolo, BindingResult bindingResult ) {
        log.info( "****** Creazione di un nuovo articolo ******" );

        if( bindingResult.hasErrors() ) {
            String errMsg = errMessage.getMessage(
                    Objects.requireNonNull( bindingResult.getFieldError() ),
                    // Attualmente inutile dato che forziamo it
                    LocaleContextHolder.getLocale()
            );

            log.warning( errMsg );

            throw new BindingException( errMsg );
        }

        // Per la modifica bisogna disabilitare
        ArticoliDto checkArt = articoliService.SelByCodArt( articolo.getCodArt() );

        if( checkArt != null ) {
            String errMsg = String.format( "Articolo %s presente in anagrafica!", articolo.getCodArt() );

            log.warning( errMsg );

            throw new DuplicateException( errMsg );
        }

        articoliService.InsArticolo( articolo );

        return new ResponseEntity<>(
                new InfoMsg(
                        LocalDate.now(),
                        "Inserimento Articolo Eseguito con successo!"
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping(value = "/modifica", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<InfoMsg> updateArt( @Valid @RequestBody Articoli articolo, BindingResult bindingResult ) {
        log.info( "****** Modifica di un articolo ******" );

        if( bindingResult.hasErrors() ) {
            String errMsg = errMessage.getMessage(
                    Objects.requireNonNull( bindingResult.getFieldError() ),
                    // Attualmente inutile dato che forziamo it
                    LocaleContextHolder.getLocale()
            );

            log.warning( errMsg );

            throw new BindingException( errMsg );
        }

        // Per la modifica bisogna disabilitare
        ArticoliDto checkArt = articoliService.SelByCodArt( articolo.getCodArt() );

        if( checkArt == null ) {
            String errMsg = String.format( "Articolo %s non presente in anagrafica!", articolo.getCodArt() );

            log.warning( errMsg );

            throw new NotFoundException( errMsg );
        }

        articoliService.InsArticolo( articolo );

        return new ResponseEntity<>(
                new InfoMsg(
                        LocalDate.now(),
                        "Modifica Articolo Eseguita con successo!"
                ),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(value = "/elimina/{codart}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<ObjectNode> deleteArt( @PathVariable("codart") String codArt ) {
        log.info( String.format( "****** Eliminazione dell'articolo con codice %s ******", codArt ) );

        Articoli articolo = articoliService.SelByCodArt2( codArt );

        if( articolo == null ) {
            String errMsg = String.format( "Articolo %s non presente in anagrafica!", codArt );

            log.warning( errMsg );

            throw new NotFoundException( errMsg );
        }

        articoliService.DelArticolo( articolo );

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put( "code", HttpStatus.OK.toString() );
        responseNode.put( "message", String.format( "Eliminazione Articolo %s Eseguita Con Successo", codArt ) );

        return new ResponseEntity<>( responseNode, HttpStatus.OK );
    }
}
