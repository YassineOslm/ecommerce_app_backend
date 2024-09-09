package eafc.uccle.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "shopping_order")
@Data
public class ShoppingOrder {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_shopping_order", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_price")
    private String totalPrice;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "delivery_method")
    private String deliveryMethod;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "shoppingOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ShoppingDetails> shoppingDetails;

}

