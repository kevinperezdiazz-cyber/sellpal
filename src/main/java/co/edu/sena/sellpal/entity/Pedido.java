package co.edu.sena.sellpal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_empleado_registro")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empleado empleado_registro;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"pedido", "hibernateLazyInitializer", "handler"})
    private List<DetallePedido> detalles;
}
