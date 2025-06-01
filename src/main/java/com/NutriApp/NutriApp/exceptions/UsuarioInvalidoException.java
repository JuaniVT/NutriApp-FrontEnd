package com.NutriApp.NutriApp.exceptions;

public class UsuarioInvalidoException extends RuntimeException{
    public UsuarioInvalidoException(String mesagge) {
        super(mesagge);
    }
}
