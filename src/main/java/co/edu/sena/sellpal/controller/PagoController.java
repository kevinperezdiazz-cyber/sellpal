package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Pago;
import co.edu.sena.sellpal.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping
    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Integer id) {
        return pagoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoRepository.save(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Integer id, @RequestBody Pago pago) {
        if (!pagoRepository.existsById(id)) return ResponseEntity.notFound().build();
        pago.setId_pago(id);
        return ResponseEntity.ok(pagoRepository.save(pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!pagoRepository.existsById(id)) return ResponseEntity.notFound().build();
        pagoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
