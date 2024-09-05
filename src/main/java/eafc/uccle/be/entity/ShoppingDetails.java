package eafc.uccle.be.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shopping_details")
@Data
public class ShoppingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shopping_details")
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "purchased_amount")
    private double purchasedAmount;

    @ManyToOne
    @JoinColumn(name = "id_shopping_order", nullable = false)
    private ShoppingOrder shoppingOrder;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;

}

