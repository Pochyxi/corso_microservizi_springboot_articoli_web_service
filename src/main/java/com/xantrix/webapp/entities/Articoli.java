package com.xantrix.webapp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xantrix.webapp.validation.CodArt;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ARTICOLI")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Articoli implements Serializable
{
    private static final long serialVersionUID = 2515013238876787603L;

    @Id
    @Column(name = "CODART")
    @Size(min = 5, max = 20, message = "{Size.Articoli.codArt.Validation}")
    @CodArt
    private String codArt;

    @Column(name = "DESCRIZIONE")
    @Size(min = 6, max = 80, message = "{Size.Articoli.descrizione.Validation}")
    private String descrizione;

    @Column(name = "UM")
    private String um;

    @Column(name = "CODSTAT")
    @NotBlank(message = "{NotBlank.Articoli.codStat.Validation}")
    private String codStat;

    @Column(name = "PZCART")
    @Max( message = "{Max.Articoli.pzCart.Validation}", value = 99)
    private Integer pzCart;

    @Column(name = "PESONETTO")
    @Min( message = "{Min.Articoli.pesoNetto.Validation}", value = (long) 0.01)
    private double pesoNetto;

    @Column(name = "IDSTATOART")
    private String idStatoArt;

    @Temporal( TemporalType.DATE )
    @Column(name = "DATACREAZIONE")
    private Date dataCreaz;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "articolo", orphanRemoval = true)
    @JsonManagedReference
    private Set<Barcode> barcode = new HashSet<>();

    @OneToOne(mappedBy = "articolo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ingredienti ingredienti;

    @ManyToOne
    @JoinColumn(name = "IDIVA", referencedColumnName = "idIva")
    @NotNull(message = "{NotNull.Articoli.iva.Validation}")
    private Iva iva;

    @ManyToOne
    @JoinColumn(name = "IDFAMASS", referencedColumnName = "id")
    private FamAssort famAssort;
}
