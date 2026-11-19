package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Entrega;
import co.edu.sena.sellpal.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    @Autowired
    private EntregaRepository entregaRepository;

    @GetMapping
    public List<Entrega> obtenerTodos() {
        return entregaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> obtenerPorId(@PathVariable Integer id) {
        return entregaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Entrega> crear(@RequestBody Entrega entrega) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entregaRepository.save(entrega));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrega> actualizar(@PathVariable Integer id, @RequestBody Entrega entrega) {
        if (!entregaRepository.existsById(id)) return ResponseEntity.notFound().build();
        entrega.setId_entrega(id);
        return ResponseEntity.ok(entregaRepository.save(entrega));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!entregaRepository.existsById(id)) return ResponseEntity.notFound().build();
        entregaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
