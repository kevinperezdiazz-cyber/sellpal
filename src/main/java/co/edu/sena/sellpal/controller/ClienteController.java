package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Cliente;
import co.edu.sena.sellpal.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{documentoUnico}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable("documentoUnico") Long documentoUnico) {
        return clienteRepository.findById(documentoUnico)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // El documento_unico (cédula/NIT) lo define el cliente en el cuerpo del JSON, no se autogenera.
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepository.save(cliente));
    }

    @PutMapping("/{documentoUnico}")
    public ResponseEntity<Cliente> actualizar(@PathVariable("documentoUnico") Long documentoUnico, @RequestBody Cliente cliente) {
        if (!clienteRepository.existsById(documentoUnico)) return ResponseEntity.notFound().build();
        cliente.setDocumento_unico(documentoUnico);
        return ResponseEntity.ok(clienteRepository.save(cliente));
    }

    @DeleteMapping("/{documentoUnico}")
    public ResponseEntity<Void> eliminar(@PathVariable("documentoUnico") Long documentoUnico) {
        if (!clienteRepository.existsById(documentoUnico)) return ResponseEntity.notFound().build();
        clienteRepository.deleteById(documentoUnico);
        return ResponseEntity.noContent().build();
    }
}
