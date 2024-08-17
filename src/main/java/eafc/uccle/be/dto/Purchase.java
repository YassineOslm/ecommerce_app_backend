package eafc.uccle.be.dto;

import eafc.uccle.be.entity.Address;
import eafc.uccle.be.entity.Customer;
import eafc.uccle.be.entity.Order;
import eafc.uccle.be.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
