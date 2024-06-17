package com.xantrix.webapp.exceptions;

public class BindingException extends Exception
{
    private static final long serialVersionUID = 1L;

    private String messaggio;

    public BindingException(String messaggio)
    {
        super(messaggio);
        this.messaggio = messaggio;
    }

    public BindingException()
    {
        super();
    }
}
