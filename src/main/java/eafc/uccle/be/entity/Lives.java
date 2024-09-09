package eafc.uccle.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lives")
@Data
public class Lives {

    @EmbeddedId
    private LivesId id = new LivesId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userAddressId")
    @JoinColumn(name = "id_user_address")
    private UserAddress userAddress;
}

