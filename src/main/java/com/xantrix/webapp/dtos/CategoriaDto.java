package com.xantrix.webapp.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoriaDto implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id;
    private String descrizione;
}
