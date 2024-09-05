package eafc.uccle.be.dto;

import eafc.uccle.be.entity.UserAddress;
import eafc.uccle.be.entity.ShoppingOrder;
import eafc.uccle.be.entity.ShoppingDetails;
import eafc.uccle.be.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private User user;
    private UserAddress shippingAddress;
    private UserAddress billingAddress;
    private ShoppingOrder order;
    private Set<ShoppingDetails> orderItems;

}
