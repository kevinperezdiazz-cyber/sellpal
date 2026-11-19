package co.edu.sena.sellpal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaProducto categoria;

    // ---- Campos alineados con la interfaz ----
    @Column(length = 40)
    private String codigo;

    @Column(length = 80)
    private String marca;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;

    @Column(name = "unidad_medida", length = 40)
    private String unidadMedida;

    @Column(length = 255)
    private String imagen;

    private Boolean destacado = false;
}
