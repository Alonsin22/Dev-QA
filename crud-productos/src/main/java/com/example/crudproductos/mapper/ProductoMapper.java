package com.example.crudproductos.mapper;

import com.example.crudproductos.dto.ProductoRequest;
import com.example.crudproductos.dto.ProductoResponse;
import com.example.crudproductos.model.Producto; // Ajusta el paquete de tu entidad
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        return producto;
    }

    public void updateEntity(Producto producto, ProductoRequest request) {
        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}