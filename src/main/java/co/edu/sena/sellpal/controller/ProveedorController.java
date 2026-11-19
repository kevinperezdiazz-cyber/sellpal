package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Proveedor;
import co.edu.sena.sellpal.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public List<Proveedor> obtenerTodos() {
        return proveedorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Integer id) {
        return proveedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorRepository.save(proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Integer id, @RequestBody Proveedor proveedor) {
        if (!proveedorRepository.existsById(id)) return ResponseEntity.notFound().build();
        proveedor.setId_proveedor(id);
        return ResponseEntity.ok(proveedorRepository.save(proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!proveedorRepository.existsById(id)) return ResponseEntity.notFound().build();
        proveedorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
