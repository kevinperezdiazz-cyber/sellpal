package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.dto.AuthDtos;
import co.edu.sena.sellpal.entity.Usuario;
import co.edu.sena.sellpal.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestión de usuarios web (aprobación de tiendas/clientes nuevos).
 * Acceso desde el panel admin.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService users;

    public UsuarioController(UsuarioService users) {
        this.users = users;
    }

    @GetMapping
    public List<AuthDtos.UsuarioResponse> listar() {
        return users.listarTodos().stream().map(users::respuesta).toList();
    }

    @GetMapping("/pendientes")
    public List<AuthDtos.UsuarioResponse> pendientes() {
        return users.listarPendientes().stream().map(users::respuesta).toList();
    }

    @PutMapping("/{id}/aprobar")
    public AuthDtos.UsuarioResponse aprobar(@PathVariable Long id) {
        return users.respuesta(users.aprobar(id));
    }

    @PutMapping("/{id}/rechazar")
    public AuthDtos.UsuarioResponse rechazar(@PathVariable Long id) {
        return users.respuesta(users.rechazar(id));
    }
}
