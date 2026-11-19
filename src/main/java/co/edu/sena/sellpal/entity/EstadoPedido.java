package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_estado;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre_estado;
}
