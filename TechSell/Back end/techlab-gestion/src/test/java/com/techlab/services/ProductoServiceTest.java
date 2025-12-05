package com.techlab.services;

import com.techlab.productos.Producto;
import com.techlab.repos.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductoServiceTest {
    @Test
    void agregarYListar() {
        ProductoRepository repo = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService(repo);
        Producto p = new Producto(); p.setNombre("Huevos"); p.setPrecio(10); p.setStock(5);
        Mockito.when(repo.save(p)).thenReturn(p);
        Mockito.when(repo.findAll()).thenReturn(List.of(p));
        assertEquals("Huevos", service.agregar(p).getNombre());
        assertEquals(1, service.listar().size());
    }

    @Test
    void buscarPorId() {
        ProductoRepository repo = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService(repo);
        Producto p = new Producto(); p.setNombre("Leche"); p.setPrecio(5); p.setStock(10);
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(p));
        assertEquals("Leche", service.buscar(1L).getNombre());
    }
}
