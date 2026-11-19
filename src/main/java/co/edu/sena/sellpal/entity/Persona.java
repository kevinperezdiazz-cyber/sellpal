package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    private Long documento_unico;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 200)
    private String direccion;

    @Column(length = 30)
    private String telefono;

    @Column(length = 150)
    private String email;

    private LocalDate fecha_nacimiento;
}
