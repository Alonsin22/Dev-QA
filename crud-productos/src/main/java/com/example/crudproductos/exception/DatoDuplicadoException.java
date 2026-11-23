package com.example.crudproductos.exception;

public class DatoDuplicadoException extends RuntimeException {

    public DatoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}