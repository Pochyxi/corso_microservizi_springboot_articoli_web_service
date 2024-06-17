package com.xantrix.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "IVA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Iva {
    @Id
    @Column(name = "IDIVA")
    private int idIva;

    @Column(name = "DESCRIZIONE")
    private String descrizione;

    @Column(name = "ALIQUOTA")
    private int aliquota;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "iva")
    @JsonBackReference
    private Set<Articoli> articoli = new HashSet<>();
}
