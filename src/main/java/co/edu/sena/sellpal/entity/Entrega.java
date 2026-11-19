package co.edu.sena.sellpal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_entrega;

    @ManyToOne
    @JoinColumn(name = "id_detalle", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DetallePedido detalle;

    @Column(name = "fecha_entrega")
    private LocalDateTime fecha_entrega;

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE";

    @Column(length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_empleado_repartidor", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Empleado empleado_repartidor;
}
