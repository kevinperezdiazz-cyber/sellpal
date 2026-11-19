package co.edu.sena.sellpal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "documento_unico")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Cliente extends Persona {

    @Column(length = 50)
    private String tipo_cliente;

    @Column(length = 50)
    private String estado_cuenta;
}
