package com.NutriApp.NutriApp.exceptions.Handlers;

import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.exceptions.UsuarioExistente;
import com.NutriApp.NutriApp.exceptions.UsuarioInvalidoException;
import jakarta.mail.SendFailedException;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler{

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

    @ExceptionHandler
    public ResponseEntity<String> manejarTipoNoSoportado (HttpMediaTypeNotSupportedException ex){
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("El contenido no es soportado = " +ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> manejarMailException (SMTPAddressFailedException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El correo no se pudo mandar = " +ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> manejarAccessDeniedException (AuthorizationDeniedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage() + " = permisos insuficientes");
    }

    @ExceptionHandler
    public ResponseEntity<String> manejarNoResourceFoundException (NoResourceFoundException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No existe la ruta especificada");
    }

    @ExceptionHandler
    public ResponseEntity<String> manejarMissingServletRequestParameterException (MissingServletRequestParameterException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se requiere el parametro = " +ex.getParameterName());
    }



}

