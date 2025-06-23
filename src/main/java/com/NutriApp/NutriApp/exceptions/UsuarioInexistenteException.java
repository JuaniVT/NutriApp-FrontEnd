package com.NutriApp.NutriApp.exceptions;

public class UsuarioInexistenteException extends RuntimeException {
    public UsuarioInexistenteException(String message) {
        super(message);
    }
}
