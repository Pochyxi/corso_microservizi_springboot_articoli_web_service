package com.xantrix.webapp.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BARCODE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Barcode implements Serializable
{
    private static final long serialVersionUID = -8634105265497887129L;

    @Id
    @Column(name = "BARCODE")
    private String barcode;

    @Column(name = "IDTIPOART")
    private String idTipoArt;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "CODART", referencedColumnName = "codArt")
    @JsonBackReference
    private Articoli articolo;

}
