package com.ejemplo.crudnosqldemo.repository;

import com.ejemplo.crudnosqldemo.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductoRepository extends MongoRepository<Producto, String> {

    boolean existsByCodigo(String codigo);

    Optional<Producto> findByCodigo(String codigo);
}