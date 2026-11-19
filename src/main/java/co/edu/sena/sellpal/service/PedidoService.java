package co.edu.sena.sellpal.service;

import co.edu.sena.sellpal.entity.DetallePedido;
import co.edu.sena.sellpal.entity.EstadoPedido;
import co.edu.sena.sellpal.entity.Pedido;
import co.edu.sena.sellpal.entity.Producto;
import co.edu.sena.sellpal.repository.EstadoPedidoRepository;
import co.edu.sena.sellpal.repository.PedidoRepository;
import co.edu.sena.sellpal.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    public void deleteById(Integer id) {
        pedidoRepository.deleteById(id);
    }

    /**
     * Crea un pedido junto con todos sus renglones (detalles) en una sola transacción,
     * calcula el total del pedido y descuenta el stock de cada producto vendido.
     * Si algo falla a mitad de camino, @Transactional revierte todo (no queda un pedido a medias).
     */
    @Transactional
    public Pedido crearPedidoConDetalles(Pedido pedido) {
        BigDecimal totalPedido = BigDecimal.ZERO;

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido); // relación bidireccional obligatoria para el FK

                Producto producto = productoRepository.findById(detalle.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Producto no encontrado: " + detalle.getProducto().getId()));

                if (producto.getStock() < detalle.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
                }

                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoRepository.save(producto);

                totalPedido = totalPedido.add(
                        detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            }
        }

        pedido.setTotal(totalPedido);
        return pedidoRepository.save(pedido);
    }

    /**
     * Cambia el estado del pedido buscando por nombre (Pendiente, Procesando, Enviado, Entregado, Cancelado).
     * Persiste en PostgreSQL para que el cliente vea el cambio en "Mis pedidos".
     */
    @Transactional
    public Pedido actualizarEstado(Integer idPedido, String nombreEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + idPedido));

        String buscado = nombreEstado == null ? "" : nombreEstado.trim();
        EstadoPedido estado = estadoPedidoRepository.findAll().stream()
                .filter(e -> e.getNombre_estado() != null
                        && e.getNombre_estado().equalsIgnoreCase(buscado))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Estado de pedido no existe en BD: " + buscado
                        + ". Verifique tabla estado_pedido."));

        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}

