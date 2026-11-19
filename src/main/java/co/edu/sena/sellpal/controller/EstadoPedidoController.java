package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.EstadoPedido;
import co.edu.sena.sellpal.repository.EstadoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-pedido")
@CrossOrigin(origins = "*")
public class EstadoPedidoController {

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    @GetMapping
    public List<EstadoPedido> obtenerTodos() {
        return estadoPedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoPedido> obtenerPorId(@PathVariable Integer id) {
        return estadoPedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstadoPedido> crear(@RequestBody EstadoPedido estado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoPedidoRepository.save(estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoPedido> actualizar(@PathVariable Integer id, @RequestBody EstadoPedido estado) {
        if (!estadoPedidoRepository.existsById(id)) return ResponseEntity.notFound().build();
        estado.setId_estado(id);
        return ResponseEntity.ok(estadoPedidoRepository.save(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!estadoPedidoRepository.existsById(id)) return ResponseEntity.notFound().build();
        estadoPedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
