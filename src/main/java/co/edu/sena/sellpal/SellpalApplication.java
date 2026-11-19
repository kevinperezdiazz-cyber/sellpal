package co.edu.sena.sellpal;

import co.edu.sena.sellpal.entity.Cliente;
import co.edu.sena.sellpal.entity.Usuario;
import co.edu.sena.sellpal.repository.ClienteRepository;
import co.edu.sena.sellpal.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class SellpalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellpalApplication.class, args);
    }

    /**
     * Credenciales de demostración (interfaz sellpal-fixed):
     *  Admin:  carlos.morales@invercajas.com / admin123
     *          admin@sellpal.co / admin123
     *  Cliente: compras@elahorro.co / 123456
     */
    @Bean
    CommandLineRunner seedDemoUsers(UsuarioRepository usuarios,
                                    ClienteRepository clientes,
                                    PasswordEncoder encoder) {
        return args -> {
            upsertUsuario(usuarios, encoder,
                    "Carlos Andrés Morales",
                    "carlos.morales@invercajas.com",
                    "admin123",
                    "ADMIN",
                    "3104567890",
                    "Bogotá",
                    "1023456789");

            upsertUsuario(usuarios, encoder,
                    "Administrador SELLPAL",
                    "admin@sellpal.co",
                    "admin123",
                    "ADMIN",
                    "3000000000",
                    "Bogotá",
                    "ADMIN-001");

            upsertUsuario(usuarios, encoder,
                    "Supertiendas El Ahorro S.A.S.",
                    "compras@elahorro.co",
                    "123456",
                    "CLIENTE",
                    "3114567890",
                    "Bogotá",
                    "9005432101");

            Long docCliente = 9005432101L;
            if (!clientes.existsById(docCliente)) {
                Cliente c = new Cliente();
                c.setDocumento_unico(docCliente);
                c.setNombre("Supertiendas El Ahorro S.A.S.");
                c.setEmail("compras@elahorro.co");
                c.setTelefono("3114567890");
                c.setDireccion("Cra 15 # 45 - 20, Bogotá");
                c.setFecha_nacimiento(LocalDate.of(1990, 1, 1));
                c.setTipo_cliente("Empresa");
                c.setEstado_cuenta("Activa");
                clientes.save(c);
                System.out.println("[SELLPAL] Cliente demo creado: documento " + docCliente);
            }

            System.out.println("[SELLPAL] Usuarios demo listos:");
            System.out.println("  Admin  -> carlos.morales@invercajas.com / admin123");
            System.out.println("  Admin  -> admin@sellpal.co / admin123");
            System.out.println("  Cliente-> compras@elahorro.co / 123456");
        };
    }

    private static void upsertUsuario(UsuarioRepository repo,
                                      PasswordEncoder encoder,
                                      String nombre,
                                      String email,
                                      String passwordPlano,
                                      String rol,
                                      String telefono,
                                      String ciudad,
                                      String documento) {
        var existing = repo.findByEmailIgnoreCase(email);
        if (existing.isPresent()) {
            Usuario u = existing.get();
            u.setPassword(encoder.encode(passwordPlano));
            u.setRol(rol);
            u.setNombre(nombre);
            u.setActivo(true);
            if (u.getDocumento() == null || u.getDocumento().isBlank()) {
                u.setDocumento(documento);
            }
            repo.save(u);
            return;
        }
        String doc = documento;
        if (repo.existsByDocumento(doc)) {
            doc = doc + "-" + System.currentTimeMillis() % 10000;
        }
        repo.save(new Usuario(
                null, nombre, email.toLowerCase(),
                encoder.encode(passwordPlano), rol,
                telefono, "Dirección comercial", ciudad, doc, true
        ));
    }
}
