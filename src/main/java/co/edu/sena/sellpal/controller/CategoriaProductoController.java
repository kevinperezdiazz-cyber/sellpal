package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.CategoriaProducto;
import co.edu.sena.sellpal.repository.CategoriaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProductoController {

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @GetMapping
    public List<CategoriaProducto> obtenerTodos() {
        return categoriaProductoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProducto> obtenerPorId(@PathVariable Integer id) {
        return categoriaProductoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaProducto> crear(@RequestBody CategoriaProducto categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaProductoRepository.save(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProducto> actualizar(@PathVariable Integer id, @RequestBody CategoriaProducto categoria) {
        if (!categoriaProductoRepository.existsById(id)) return ResponseEntity.notFound().build();
        categoria.setId_categoria(id);
        return ResponseEntity.ok(categoriaProductoRepository.save(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!categoriaProductoRepository.existsById(id)) return ResponseEntity.notFound().build();
        categoriaProductoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
