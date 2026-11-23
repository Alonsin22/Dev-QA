package com.example.crudproductos.controller;

import com.example.crudproductos.exception.DatoDuplicadoException;
import com.example.crudproductos.exception.GlobalExceptionHandler;
import com.example.crudproductos.exception.RecursoNoEncontradoException;
import com.example.crudproductos.model.Producto;
import com.example.crudproductos.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductoControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        productoEjemplo = new Producto(
                "101",
                "Mouse inalámbrico",
                "Accesorios",
                new BigDecimal("450.00"),
                25
        );
    }

    @Test
    void postProducto_valido_retorna201() throws Exception {
        when(productoService.crear(any(Producto.class))).thenReturn(productoEjemplo);

        String jsonBody = """
                {
                  "nombre": "Mouse inalámbrico",
                  "categoria": "Accesorios",
                  "precio": 450.00,
                  "stock": 25
                }
                """;

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("101"))
                .andExpect(jsonPath("$.nombre").value("Mouse inalámbrico"));
    }

    @Test
    void postProducto_invalido_retorna400() throws Exception {
        String jsonInvalido = """
                {
                  "nombre": "",
                  "categoria": "",
                  "precio": -10,
                  "stock": -1
                }
                """;

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void postProducto_duplicado_retorna409() throws Exception {
        when(productoService.crear(any(Producto.class)))
                .thenThrow(new DatoDuplicadoException("Ya existe un producto registrado con el nombre: Mouse inalámbrico"));

        String jsonBody = """
                {
                  "nombre": "Mouse inalámbrico",
                  "categoria": "Accesorios",
                  "precio": 450.00,
                  "stock": 25
                }
                """;

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getTodos_retorna200() throws Exception {
        when(productoService.listarTodos()).thenReturn(List.of(productoEjemplo));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse inalámbrico"));
    }

    @Test
    void getPorId_inexistente_retorna404() throws Exception {
        when(productoService.obtenerPorId("999"))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado con id: 999"));

        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void putProducto_existente_retorna200() throws Exception {
        when(productoService.actualizar(eq("101"), any(Producto.class))).thenReturn(productoEjemplo);

        String jsonBody = """
                {
                  "nombre": "Mouse inalámbrico",
                  "categoria": "Accesorios",
                  "precio": 450.00,
                  "stock": 25
                }
                """;

        mockMvc.perform(put("/api/productos/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("101"));
    }

    @Test
    void deleteProducto_existente_retorna204() throws Exception {
        doNothing().when(productoService).eliminar("101");

        mockMvc.perform(delete("/api/productos/101"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProducto_inexistente_retorna404() throws Exception {
        doThrow(new RecursoNoEncontradoException("Producto no encontrado con id: 999"))
                .when(productoService).eliminar("999");

        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }
}