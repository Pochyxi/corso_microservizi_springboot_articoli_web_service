package com.xantrix.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INGREDIENTI")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ingredienti implements Serializable
{
    private static final long serialVersionUID = -630937663606801928L;

    @Id
    @Column(name = "CODART")
    private String codArt;

    @Column(name = "INFO")
    private String info;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private Articoli articolo;

}
