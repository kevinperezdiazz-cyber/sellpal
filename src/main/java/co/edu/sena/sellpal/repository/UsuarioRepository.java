package co.edu.sena.sellpal.repository;

import co.edu.sena.sellpal.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByDocumento(String documento);
    List<Usuario> findByActivoFalseOrderByIdDesc();
    List<Usuario> findAllByOrderByIdDesc();
    List<Usuario> findByRolIgnoreCaseOrderByIdDesc(String rol);
}
