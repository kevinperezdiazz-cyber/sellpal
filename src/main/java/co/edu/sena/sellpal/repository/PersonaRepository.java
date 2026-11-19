package co.edu.sena.sellpal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.sena.sellpal.entity.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
