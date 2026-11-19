package co.edu.sena.sellpal.service;

import co.edu.sena.sellpal.dto.AuthDtos;
import co.edu.sena.sellpal.entity.Cliente;
import co.edu.sena.sellpal.entity.Usuario;
import co.edu.sena.sellpal.repository.ClienteRepository;
import co.edu.sena.sellpal.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;
    private final ClienteRepository clientes;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, ClienteRepository clientes, PasswordEncoder encoder) {
        this.repo = repo;
        this.clientes = clientes;
        this.encoder = encoder;
    }

    /**
     * Registro de tienda/cliente: queda PENDIENTE (activo=false) hasta que un admin apruebe.
     */
    @Transactional
    public Usuario registrar(AuthDtos.RegistroRequest r) {
        if (!r.password().equals(r.passwordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        if (!r.password().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,72}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener 8 caracteres, mayúscula, minúscula, número y símbolo");
        }
        if (repo.existsByEmailIgnoreCase(r.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (repo.existsByDocumento(r.documento())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El documento ya está registrado");
        }
        String documento = r.documento().trim();
        long documentoNumerico;
        try {
            documentoNumerico = Long.parseLong(documento.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El documento debe contener números para crear el cliente");
        }
        if (clientes.existsById(documentoNumerico)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El documento ya existe como cliente");
        }

        // activo=false → pendiente de aprobación del administrador
        Usuario u = new Usuario(
                null,
                r.nombre().trim(),
                r.email().trim().toLowerCase(),
                encoder.encode(r.password()),
                "CLIENTE",
                r.telefono(),
                r.direccion(),
                r.ciudad(),
                documento,
                false
        );
        Usuario guardado = repo.save(u);

        Cliente cliente = new Cliente();
        cliente.setDocumento_unico(documentoNumerico);
        cliente.setNombre(r.nombre().trim());
        cliente.setEmail(r.email().trim().toLowerCase());
        cliente.setTelefono(r.telefono());
        cliente.setDireccion(r.direccion());
        cliente.setTipo_cliente("CLIENTE");
        cliente.setEstado_cuenta("PENDIENTE");
        clientes.save(cliente);

        return guardado;
    }

    public Usuario porEmail(String email) {
        return repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public List<Usuario> listarTodos() {
        return repo.findAllByOrderByIdDesc();
    }

    public List<Usuario> listarPendientes() {
        return repo.findByActivoFalseOrderByIdDesc();
    }

    @Transactional
    public Usuario aprobar(Long id) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if ("ADMIN".equalsIgnoreCase(u.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cambiar el estado de un administrador desde aquí");
        }
        u.setActivo(true);
        repo.save(u);
        // Activar cuenta del cliente en tabla cliente
        try {
            long doc = Long.parseLong(u.getDocumento().replaceAll("[^0-9]", ""));
            clientes.findById(doc).ifPresent(c -> {
                c.setEstado_cuenta("ACTIVO");
                clientes.save(c);
            });
        } catch (Exception ignored) {}
        return u;
    }

    @Transactional
    public Usuario rechazar(Long id) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if ("ADMIN".equalsIgnoreCase(u.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede rechazar un administrador");
        }
        u.setActivo(false);
        repo.save(u);
        try {
            long doc = Long.parseLong(u.getDocumento().replaceAll("[^0-9]", ""));
            clientes.findById(doc).ifPresent(c -> {
                c.setEstado_cuenta("RECHAZADO");
                clientes.save(c);
            });
        } catch (Exception ignored) {}
        return u;
    }

    @Transactional
    public Usuario actualizarPerfil(String email, AuthDtos.PerfilRequest r) {
        Usuario u = porEmail(email);
        if (!u.getEmail().equalsIgnoreCase(r.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El correo de acceso no se puede cambiar desde este formulario");
        }
        u.setNombre(r.nombre().trim());
        u.setTelefono(r.telefono());
        u.setDireccion(r.direccion());
        return repo.save(u);
    }

    @Transactional
    public void cambiarPassword(String email, AuthDtos.PasswordRequest r) {
        if (!r.nueva().equals(r.confirmacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        if (!r.nueva().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,72}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener 8 caracteres, mayúscula, minúscula, número y símbolo");
        }
        Usuario u = porEmail(email);
        if (!encoder.matches(r.actual(), u.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta");
        }
        u.setPassword(encoder.encode(r.nueva()));
        repo.save(u);
    }

    public AuthDtos.UsuarioResponse respuesta(Usuario u) {
        return new AuthDtos.UsuarioResponse(
                u.getId(), u.getNombre(), u.getEmail(), u.getRol(),
                u.getDocumento(), u.getTelefono(), u.getDireccion(), u.getCiudad(),
                u.isActivo());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario u = porEmail(username);
        return User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRol())
                .disabled(!u.isActivo())
                .build();
    }
}
