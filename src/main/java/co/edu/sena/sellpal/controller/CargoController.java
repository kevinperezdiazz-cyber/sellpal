package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Cargo;
import co.edu.sena.sellpal.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@CrossOrigin(origins = "*")
public class CargoController {

    @Autowired
    private CargoRepository cargoRepository;

    @GetMapping
    public List<Cargo> obtenerTodos() {
        return cargoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> obtenerPorId(@PathVariable Integer id) {
        return cargoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cargo> crear(@RequestBody Cargo cargo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoRepository.save(cargo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> actualizar(@PathVariable Integer id, @RequestBody Cargo cargo) {
        if (!cargoRepository.existsById(id)) return ResponseEntity.notFound().build();
        cargo.setId_cargo(id);
        return ResponseEntity.ok(cargoRepository.save(cargo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!cargoRepository.existsById(id)) return ResponseEntity.notFound().build();
        cargoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
