package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cargo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cargo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre_cargo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /** Estado visual usado por la interfaz (Activo / Inactivo) */
    @Column(length = 20)
    private String estado = "Activo";
}
