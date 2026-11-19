package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="usuarios", uniqueConstraints={@UniqueConstraint(name="uk_usuario_email", columnNames="email")})
@Data @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=150) private String nombre;
    @Column(nullable=false, unique=true, length=150) private String email;
    @Column(nullable=false, length=100) private String password;
    @Column(nullable=false, length=30) private String rol = "CLIENTE";
    private String telefono;
    private String direccion;
    private String ciudad;
    @Column(name="documento", nullable=false, unique=true, length=40) private String documento;
    private boolean activo = true;
}
