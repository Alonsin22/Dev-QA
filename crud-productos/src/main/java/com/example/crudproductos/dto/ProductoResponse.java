package com.example.crudproductos.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        String id,
        String nombre,
        BigDecimal precio,
        Integer stock
) {
}