package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.DetallePedido;
import co.edu.sena.sellpal.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CRUD de solo lectura/borrado: los detalles se crean junto con el pedido (ver PedidoController).
@RestController
@RequestMapping("/api/detalles-pedido")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @GetMapping
    public List<DetallePedido> obtenerTodos() {
        return detallePedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> obtenerPorId(@PathVariable Integer id) {
        return detallePedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!detallePedidoRepository.existsById(id)) return ResponseEntity.notFound().build();
        detallePedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
