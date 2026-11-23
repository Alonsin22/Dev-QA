package com.example.crudproductos.service;

import com.example.crudproductos.dto.ProductoRequest;
import com.example.crudproductos.dto.ProductoResponse;
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

    public ProductoResponse crear(ProductoRequest request) {
        if (productoRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new DatoDuplicadoException(
                    "Ya existe un producto registrado con el nombre: " + request.nombre()
            );
        }

        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setCategoria(request.categoria());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());

        Producto guardado = productoRepository.save(producto);
        return toResponse(guardado);
    }

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductoResponse obtenerPorId(String id) {
        return toResponse(buscarEntidadPorId(id));
    }

    public ProductoResponse actualizar(String id, ProductoRequest datosActualizados) {
        Producto existente = buscarEntidadPorId(id);

        if (!existente.getNombre().equalsIgnoreCase(datosActualizados.nombre())
                && productoRepository.existsByNombreIgnoreCase(datosActualizados.nombre())) {
            throw new DatoDuplicadoException(
                    "Ya existe otro producto con el nombre: " + datosActualizados.nombre()
            );
        }

        existente.setNombre(datosActualizados.nombre());
        existente.setCategoria(datosActualizados.categoria());
        existente.setPrecio(datosActualizados.precio());
        existente.setStock(datosActualizados.stock());

        Producto actualizado = productoRepository.save(existente);
        return toResponse(actualizado);
    }

    public void eliminar(String id) {
        Producto existente = buscarEntidadPorId(id);
        productoRepository.deleteById(existente.getId());
    }

    private Producto buscarEntidadPorId(String id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id
                ));
    }

    private ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}