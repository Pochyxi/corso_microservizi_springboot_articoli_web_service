package com.xantrix.webapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateException extends Exception
{
    private static final long serialVersionUID = 1L;

    private String messaggio;

    public DuplicateException(String messaggio)
    {
        super(messaggio);
        this.messaggio = messaggio;
    }

    public DuplicateException()
    {
        super();
    }
}
