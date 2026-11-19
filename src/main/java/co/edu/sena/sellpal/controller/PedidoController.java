package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Pedido;
import co.edu.sena.sellpal.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crea el pedido junto con sus renglones (detalles), calcula el total
    // y descuenta el stock de cada producto vendido, todo en una sola transacción.
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pedido pedido) {
        try {
            Pedido creado = pedidoService.crearPedidoConDetalles(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (pedidoService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza solo el estado del pedido.
     * Body JSON: { "estado": "Entregado" }  o  { "nombre_estado": "Entregado" }
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody java.util.Map<String, String> body) {
        try {
            String nombre = body.get("estado");
            if (nombre == null || nombre.isBlank()) nombre = body.get("nombre_estado");
            if (nombre == null || nombre.isBlank()) {
                return ResponseEntity.badRequest().body("Debe enviar el campo 'estado' con el nombre del estado.");
            }
            Pedido actualizado = pedidoService.actualizarEstado(id, nombre);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
