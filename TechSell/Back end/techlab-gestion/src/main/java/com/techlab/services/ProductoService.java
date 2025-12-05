package com.techlab.services;

import com.techlab.excepciones.NotFoundException;
import com.techlab.productos.Producto;
import com.techlab.repos.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repo;
    public ProductoService(ProductoRepository repo) { this.repo = repo; }

    public Producto agregar(Producto p) {
        if (p == null) throw new NotFoundException("Producto inválido");
        return repo.save(p);
    }
    public List<Producto> listar() { return repo.findAll(); }
    public Producto buscar(long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado")); }
    public Producto actualizar(long id, Producto cambios) {
        Producto existente = buscar(id);
        existente.setNombre(cambios.getNombre());
        existente.setPrecio(cambios.getPrecio());
        existente.setStock(cambios.getStock());
        return repo.save(existente);
    }
    public void eliminar(long id) { repo.deleteById(id); }
}
