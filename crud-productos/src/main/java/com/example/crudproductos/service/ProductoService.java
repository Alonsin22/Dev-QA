package com.example.crudproductos.service;

import com.example.crudproductos.exception.DatoDuplicadoException;
import com.example.crudproductos.exception.RecursoNoEncontradoException;
import com.example.crudproductos.model.Producto;
import com.example.crudproductos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crear(Producto producto) {
        if (productoRepository.existsByNombreIgnoreCase(producto.getNombre())) {
            throw new DatoDuplicadoException(
                    "Ya existe un producto registrado con el nombre: " + producto.getNombre()
            );
        }
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(String id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id
                ));
    }

    public Producto actualizar(String id, Producto datosActualizados) {
        Producto existente = obtenerPorId(id);

        if (!existente.getNombre().equalsIgnoreCase(datosActualizados.getNombre())
                && productoRepository.existsByNombreIgnoreCase(datosActualizados.getNombre())) {
            throw new DatoDuplicadoException(
                    "Ya existe otro producto con el nombre: " + datosActualizados.getNombre()
            );
        }

        existente.setNombre(datosActualizados.getNombre());
        existente.setCategoria(datosActualizados.getCategoria());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setStock(datosActualizados.getStock());

        return productoRepository.save(existente);
    }

    public void eliminar(String id) {
        Producto existente = obtenerPorId(id);
        productoRepository.deleteById(existente.getId());
    }
}