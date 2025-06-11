package com.NutriApp.NutriApp.exceptions;

public class UsuarioExistente extends RuntimeException {
    public UsuarioExistente(String message) {
        super(message);
    }
}
