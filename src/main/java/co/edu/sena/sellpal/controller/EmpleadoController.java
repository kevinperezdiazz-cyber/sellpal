package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Empleado;
import co.edu.sena.sellpal.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @GetMapping("/{documentoUnico}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable("documentoUnico") Long documentoUnico) {
        return empleadoRepository.findById(documentoUnico)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@RequestBody Empleado empleado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoRepository.save(empleado));
    }

    @PutMapping("/{documentoUnico}")
    public ResponseEntity<Empleado> actualizar(@PathVariable("documentoUnico") Long documentoUnico, @RequestBody Empleado empleado) {
        if (!empleadoRepository.existsById(documentoUnico)) return ResponseEntity.notFound().build();
        empleado.setDocumento_unico(documentoUnico);
        return ResponseEntity.ok(empleadoRepository.save(empleado));
    }

    @DeleteMapping("/{documentoUnico}")
    public ResponseEntity<Void> eliminar(@PathVariable("documentoUnico") Long documentoUnico) {
        if (!empleadoRepository.existsById(documentoUnico)) return ResponseEntity.notFound().build();
        empleadoRepository.deleteById(documentoUnico);
        return ResponseEntity.noContent().build();
    }
}
