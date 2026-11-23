package com.example.crudproductos.service;

import com.example.crudproductos.exception.DatoDuplicadoException;
import com.example.crudproductos.exception.RecursoNoEncontradoException;
import com.example.crudproductos.model.Producto;
import com.example.crudproductos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoBase;

    @BeforeEach
    void setUp() {
        productoBase = new Producto(
                "1",
                "Teclado Mecánico",
                "Periféricos",
                new BigDecimal("1200.00"),
                15
        );
    }

    @Test
    void crearProducto_exitoso() {
        when(productoRepository.existsByNombreIgnoreCase("Teclado Mecánico")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoBase);

        Producto resultado = productoService.crear(productoBase);

        assertNotNull(resultado);
        assertEquals("Teclado Mecánico", resultado.getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crearProducto_duplicado_lanzaExcepcion() {
        when(productoRepository.existsByNombreIgnoreCase("Teclado Mecánico")).thenReturn(true);

        assertThrows(DatoDuplicadoException.class, () -> productoService.crear(productoBase));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void listarTodos_retornaLista() {
        when(productoRepository.findAll()).thenReturn(List.of(productoBase));

        List<Producto> lista = productoService.listarTodos();

        assertEquals(1, lista.size());
        assertEquals("Teclado Mecánico", lista.get(0).getNombre());
    }

    @Test
    void obtenerPorId_existente_retornaProducto() {
        when(productoRepository.findById("1")).thenReturn(Optional.of(productoBase));

        Producto encontrado = productoService.obtenerPorId("1");

        assertEquals("1", encontrado.getId());
    }

    @Test
    void obtenerPorId_inexistente_lanzaExcepcion() {
        when(productoRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> productoService.obtenerPorId("99"));
    }

    @Test
    void actualizarProducto_exitoso() {
        Producto cambios = new Producto(
                null,
                "Teclado Gamer",
                "Periféricos",
                new BigDecimal("1500.00"),
                10
        );

        when(productoRepository.findById("1")).thenReturn(Optional.of(productoBase));
        when(productoRepository.existsByNombreIgnoreCase("Teclado Gamer")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto actualizado = productoService.actualizar("1", cambios);

        assertEquals("Teclado Gamer", actualizado.getNombre());
        assertEquals(new BigDecimal("1500.00"), actualizado.getPrecio());
        assertEquals(10, actualizado.getStock());
    }

    @Test
    void eliminarProducto_existente_eliminaCorrectamente() {
        when(productoRepository.findById("1")).thenReturn(Optional.of(productoBase));

        productoService.eliminar("1");

        verify(productoRepository).deleteById("1");
    }

    @Test
    void eliminarProducto_inexistente_lanzaExcepcion() {
        when(productoRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> productoService.eliminar("99"));
        verify(productoRepository, never()).deleteById(any());
    }
    @Test
    void actualizarProducto_mismoNombre_actualizaSinError() {
        Producto cambiosMismoNombre = new Producto(
                null,
                "teclado mecánico",
                "Periféricos",
                new BigDecimal("1350.00"),
                20
        );

        when(productoRepository.findById("1")).thenReturn(Optional.of(productoBase));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto actualizado = productoService.actualizar("1", cambiosMismoNombre);

        assertEquals("teclado mecánico", actualizado.getNombre());
        assertEquals(new BigDecimal("1350.00"), actualizado.getPrecio());
        assertEquals(20, actualizado.getStock());
    }

    @Test
    void actualizarProducto_nombreDuplicado_lanzaExcepcion() {
        Producto cambiosDuplicados = new Producto(
                null,
                "Mouse Gamer",
                "Periféricos",
                new BigDecimal("1500.00"),
                10
        );

        when(productoRepository.findById("1")).thenReturn(Optional.of(productoBase));
        when(productoRepository.existsByNombreIgnoreCase("Mouse Gamer")).thenReturn(true);

        assertThrows(DatoDuplicadoException.class, () -> productoService.actualizar("1", cambiosDuplicados));
        verify(productoRepository, never()).save(any(Producto.class));
    }
}