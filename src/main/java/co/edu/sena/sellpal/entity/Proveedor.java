package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_proveedor;

    @Column(nullable = false, length = 150)
    private String nombre_empresa;

    @Column(length = 150)
    private String contacto;
}
