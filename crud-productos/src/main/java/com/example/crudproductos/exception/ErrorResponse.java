package com.example.crudproductos.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String mensaje;
    private Map<String, String> detallesValidacion;

    public ErrorResponse(Instant timestamp, int status, String error, String mensaje,
                         Map<String, String> detallesValidacion) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.detallesValidacion = detallesValidacion;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Map<String, String> getDetallesValidacion() {
        return detallesValidacion;
    }
}