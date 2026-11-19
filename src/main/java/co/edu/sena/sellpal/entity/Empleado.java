package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "documento_unico")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Empleado extends Persona {

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal sueldo;

    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false)
    private Cargo cargo;
}
