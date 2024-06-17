package com.xantrix.webapp.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class BarcodeDto implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String barcode;

    private String idTipoArt;
}
