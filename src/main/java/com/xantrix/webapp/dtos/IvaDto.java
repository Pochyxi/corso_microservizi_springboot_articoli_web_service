package com.xantrix.webapp.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class IvaDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int idIva;

    private String descrizione;

    private int aliquota;
}
