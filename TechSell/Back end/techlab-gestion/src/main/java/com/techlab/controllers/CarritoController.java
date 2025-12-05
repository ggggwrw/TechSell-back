package com.techlab.controllers;

import com.techlab.excepciones.NotFoundException;
import com.techlab.productos.Producto;
import com.techlab.repos.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin
public class CarritoController {
    private final ProductoRepository productoRepo;
    // carrito en memoria: productoId -> cantidad
    private final Map<Long, Integer> carrito = new HashMap<>();

    public CarritoController(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    @GetMapping
    public Map<String, Object> ver() {
        Map<String, Object> resp = new HashMap<>();
        double total = 0.0;
        for (Map.Entry<Long, Integer> e : carrito.entrySet()) {
            Long key = e.getKey();
            if (key == null) continue;
            Producto p = productoRepo.findById(key).orElse(null);
            if (p != null) total += p.getPrecio() * e.getValue();
        }
        resp.put("items", carrito);
        resp.put("total", total);
        return resp;
    }

    @PostMapping("/agregar/{productoId}")
    public ResponseEntity<Map<String, Object>> agregar(@PathVariable long productoId, @RequestParam(defaultValue = "1") int cantidad) {
        Producto p = productoRepo.findById(productoId).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        Long key = p.getId();
        int inc = Math.max(1, cantidad);
        int current = carrito.getOrDefault(key, 0);
        carrito.put(key, current + inc);
        return ResponseEntity.ok(ver());
    }

    @PostMapping("/quitar/{productoId}")
    public ResponseEntity<Map<String, Object>> quitar(@PathVariable long productoId, @RequestParam(defaultValue = "1") int cantidad) {
        Integer actual = carrito.getOrDefault(productoId, 0);
        int nuevo = Math.max(0, actual - Math.max(1, cantidad));
        if (nuevo == 0) carrito.remove(productoId); else carrito.put(productoId, nuevo);
        return ResponseEntity.ok(ver());
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciar() {
        carrito.clear();
        return ResponseEntity.noContent().build();
    }
}