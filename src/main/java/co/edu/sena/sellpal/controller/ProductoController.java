package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.entity.Producto;
import co.edu.sena.sellpal.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite peticiones desde frontend (Figma, React, HTML/JS)
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // 1. Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    // 2. Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // 4. Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        return productoService.findById(id)
                .map(productoExistente -> {
                    producto.setId(id); // Asegura que se actualice el registro correcto
                    Producto actualizado = productoService.save(producto);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (productoService.findById(id).isPresent()) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}