package co.edu.sena.sellpal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.sena.sellpal.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
