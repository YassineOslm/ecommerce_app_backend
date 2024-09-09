package eafc.uccle.be.dto;

import eafc.uccle.be.entity.ShoppingOrder;
import eafc.uccle.be.entity.User;
import lombok.Data;
import java.util.Set;

@Data
public class Purchase {

    private User user;
    private ShoppingOrder order;
    private Set<ShoppingDetailsDto> orderItems;

}
