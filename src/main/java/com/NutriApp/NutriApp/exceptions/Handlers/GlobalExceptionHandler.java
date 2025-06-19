package com.NutriApp.NutriApp.exceptions.Handlers;

import com.NutriApp.NutriApp.exceptions.*;
import com.NutriApp.NutriApp.modelo.Dia;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Podés agregar más manejadores para otras excepciones
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + ex.getMessage());
    }

    @ExceptionHandler(PersonaInvalidaException.class)
    public ResponseEntity<String> manejarPersonaInvalida(PersonaInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DiaInvalidoException.class)
    public ResponseEntity<String> manejarPersonaInvalida(DiaInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> manejarUsuarioInvalido(UsuarioInvalidoException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> manejarAuthorityInvalida(AuthorityInvalidaException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarValidationException(MethodArgumentNotValidException exception) {
        StringBuilder mensaje = new StringBuilder("Errores de validación:\n");

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            mensaje.append("Campo: ")
                    .append(error.getField()) // el nombre del campo
                    .append(" | Valor rechazado: ")
                    .append(error.getRejectedValue()) // el valor que falló
                    .append(" | Error: ")
                    .append(error.getDefaultMessage()) // el mensaje de error
                    .append("\n");
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje.toString());
    }

    @ExceptionHandler(UsuarioExistente.class)
    public ResponseEntity<String> manejarPersonaInvalida(UsuarioExistente ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}

