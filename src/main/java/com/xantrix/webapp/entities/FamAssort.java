package com.xantrix.webapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FAMASSORT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamAssort {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private int id;

    @Column(name = "DESCRIZIONE")
    private String descrizione;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "famAssort")
    private Set<Articoli> articoli = new HashSet<>();

}
