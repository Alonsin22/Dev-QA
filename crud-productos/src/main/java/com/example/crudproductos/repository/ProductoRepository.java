package com.example.crudproductos.repository;

import com.example.crudproductos.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductoRepository extends MongoRepository<Producto, String> {
    boolean existsByNombreIgnoreCase(String nombre);
}