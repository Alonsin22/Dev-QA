package com.ejemplo.crudnosqldemo.service;

import com.ejemplo.crudnosqldemo.exception.DatoDuplicadoException;
import com.ejemplo.crudnosqldemo.exception.RecursoNoEncontradoException;
import com.ejemplo.crudnosqldemo.model.Producto;
import com.ejemplo.crudnosqldemo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crear(Producto producto) {
        if (producto.getNombre() != null && productoRepository.existsByNombre(producto.getNombre())) {
            throw new DatoDuplicadoException("Ya existe un producto con el nombre: " + producto.getNombre());
        }
        return productoRepository.save(producto);
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(String id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    public Producto actualizar(String id, Producto datosActualizados) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datosActualizados.getNombre());
        existente.setPrecio(datosActualizados.getPrecio());
        return productoRepository.save(existente);
    }

    public void eliminar(String id) {
        Producto existente = buscarPorId(id);
        productoRepository.delete(existente);
    }
}