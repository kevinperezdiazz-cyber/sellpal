package co.edu.sena.sellpal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pago;

    @ManyToOne
    @JoinColumn(name = "id_entrega", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Entrega entrega;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodo_pago;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fecha_pago = LocalDateTime.now();

    @Column(name = "estado_pago", nullable = false, length = 50)
    private String estado_pago = "PENDIENTE";
}
