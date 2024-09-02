package eafc.uccle.be.dto;

import eafc.uccle.be.entity.Address;
import eafc.uccle.be.entity.Order;
import eafc.uccle.be.entity.OrderItem;
import eafc.uccle.be.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private User user;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
