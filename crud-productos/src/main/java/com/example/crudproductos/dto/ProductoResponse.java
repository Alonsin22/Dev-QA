package com.example.crudproductos.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        String id,
        String nombre,
        String categoria,
        BigDecimal precio,
        Integer stock
) {
}