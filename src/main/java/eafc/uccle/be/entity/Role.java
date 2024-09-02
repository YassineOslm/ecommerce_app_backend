package eafc.uccle.be.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "role_description", nullable = false)
    private String roleDescription;
}

