package com.techlab.controllers;

import com.techlab.pedidos.Pedido;
import com.techlab.services.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Pedido> crear() { return ResponseEntity.ok(service.crearPedido()); }

    @GetMapping
    public List<Pedido> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping("/{pedidoId}/agregar/{productoId}")
    public ResponseEntity<Pedido> agregarProducto(@PathVariable Long pedidoId, @PathVariable Long productoId, @RequestParam int cantidad) {
        return ResponseEntity.ok(service.agregarProducto(pedidoId, productoId, cantidad));
    }

    @PostMapping("/{pedidoId}/confirmar")
    public ResponseEntity<Pedido> confirmar(@PathVariable Long pedidoId) { return ResponseEntity.ok(service.confirmar(pedidoId)); }

    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long pedidoId) { return ResponseEntity.ok(service.cancelar(pedidoId)); }
}
