package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String tipo;

    /** Código amigable de la interfaz (ej. CAT-GAL) */
    @Column(length = 40)
    private String codigo;
}
