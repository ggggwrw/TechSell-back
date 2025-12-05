package com.techlab.services;

import com.techlab.excepciones.NotFoundException;
import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.productos.Producto;
import com.techlab.repos.LineaPedidoRepository;
import com.techlab.repos.PedidoRepository;
import com.techlab.repos.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;
    private final LineaPedidoRepository lineaRepo;

    public PedidoService(PedidoRepository pedidoRepo, ProductoRepository productoRepo, LineaPedidoRepository lineaRepo) {
        this.pedidoRepo = pedidoRepo; this.productoRepo = productoRepo; this.lineaRepo = lineaRepo;
    }

    public Pedido crearPedido() { return pedidoRepo.save(new Pedido()); }
    public List<Pedido> listar() { return pedidoRepo.findAll(); }
    public Pedido buscar(long id) { return pedidoRepo.findById(id).orElseThrow(() -> new NotFoundException("Pedido no encontrado")); }

    public Pedido agregarProducto(long pedidoId, long productoId, int cantidad) {
        Pedido pedido = buscar(pedidoId);
        Producto producto = productoRepo.findById(productoId).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente para producto " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepo.save(producto);

        LineaPedido linea = new LineaPedido();
        Long productoKey = producto.getId();
        if (productoKey == null) throw new NotFoundException("Producto sin identificador");
        linea.setProductoId(productoKey);
        linea.setCantidad(cantidad);
        linea.setPrecioUnitario(producto.getPrecio());
        linea.setSubtotal(producto.getPrecio() * cantidad);
        lineaRepo.save(linea);

        pedido.getLineas().add(linea);
        pedido.recalcularTotal();
        return pedidoRepo.save(pedido);
    }

    public Pedido confirmar(Long pedidoId) {
        Pedido p = buscar(pedidoId);
        p.setEstado("CONFIRMADO");
        return pedidoRepo.save(p);
    }

    public Pedido cancelar(Long pedidoId) {
        Pedido p = buscar(pedidoId);
        p.setEstado("CANCELADO");
        return pedidoRepo.save(p);
    }
}
