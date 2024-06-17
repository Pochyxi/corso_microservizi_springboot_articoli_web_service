package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entities.Articoli;
import com.xantrix.webapp.entities.Barcode;
import com.xantrix.webapp.repository.ArticoliRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@CacheConfig(cacheNames = { "articoli" })
@Log
public class ArticoliServiceImpl implements ArticoliService {


    private final ArticoliRepository articoliRepository;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;

    private void EvictCache( Set<Barcode> Ean ) {
        Ean.forEach( barcode -> {
            log.info( "Eliminazione cache barcode " + barcode.getBarcode() );
            Objects.requireNonNull( cacheManager.getCache( "barcode" ) ).evict( barcode.getBarcode() );
        } );

    }

    @Override
    @Cacheable
    public List<ArticoliDto> SelByDescrizione( String descrizione, Pageable pageable ) {
        List<Articoli> articoli;

        String filter = "%" + descrizione.toUpperCase() + "%";

        if( pageable != null ) {
            articoli = articoliRepository.selByDescrizioneLike( filter, pageable );
        } else {
            articoli = articoliRepository.selByDescrizioneLike( filter );
        }


        List<ArticoliDto> retVal = articoli
                .stream()
                .map( source -> modelMapper.map( source, ArticoliDto.class ) )
                .collect( Collectors.toList() );

        return retVal;
    }

    @Override
    @Transactional
    @Cacheable(value = "articolo", key = "#codArt", sync = true)
    public ArticoliDto SelByCodArt( String codArt ) {
        Articoli articoli = this.SelByCodArt2( codArt );

        return this.ConvertToDto( articoli );
    }

    @Override
    public Articoli SelByCodArt2( String codArt ) {
        return articoliRepository.findByCodArt( codArt );
    }

    private ArticoliDto ConvertToDto( Articoli articoli ) {
        ArticoliDto articoliDto = null;

        if( articoli != null ) {
            articoliDto = modelMapper.map( articoli, ArticoliDto.class );
        }

        return articoliDto;
    }

    @Override
    @Transactional
    @Cacheable(value = "articolo", key = "#barcode", sync = true)
    public ArticoliDto SelByBarcode( String barcode ) {
        Articoli articoli = articoliRepository.selByEan( barcode );

        return this.ConvertToDto( articoli );
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "articoli", allEntries = true),
//            @CacheEvict( value = "barcode", key = "#articolo.barcode[0].barcode"),
            @CacheEvict(value = "articolo", key = "#articolo.codArt")
    })
    public void InsArticolo( Articoli articolo ) {
        articoliRepository.save( articolo );
        EvictCache( articolo.getBarcode() );
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "articoli", allEntries = true),
//            @CacheEvict( value = "barcode", key = "#articolo.barcode[0].barcode"),
            @CacheEvict(value = "articolo", key = "#articolo.codArt")
    })
    public void DelArticolo( Articoli articolo ) {
        articoliRepository.delete( articolo );
    }

    @Override
    public void CleanCaches() {
        Collection<String> items = cacheManager.getCacheNames();

        items.forEach( item -> {
            log.info( "Cancellazione cache " + item );
            Objects.requireNonNull( cacheManager.getCache( item ) ).clear();
        } );
    }
}
