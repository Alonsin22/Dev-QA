package com.ejemplo.crudnosqldemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(DatoDuplicadoException.class)
    public ResponseEntity manejarDatoDuplicado(DatoDuplicadoException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> erroresCampos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erroresCampos.put(error.getField(), error.getDefaultMessage());
        }
        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "Error de validación en los datos enviados",
                erroresCampos
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity manejarErrorGeneral(Exception ex) {
        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor",
                null
        );
    }

    private ResponseEntity construirRespuesta(
            HttpStatus status,
            String mensaje,
            Map<String, String> detalles
    ) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", Instant.now().toString());
        cuerpo.put("status", status.value());
        cuerpo.put("error", status.getReasonPhrase());
        cuerpo.put("message", mensaje);
        if (detalles != null && !detalles.isEmpty()) {
            cuerpo.put("details", detalles);
        }
        return ResponseEntity.status(status).body(cuerpo);
    }
}