package eafc.uccle.be.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "unit_price")
    private String unitPrice;

    @Column(name = "sub_total_price")
    private String subTotalPrice;

    @ManyToOne
    @JoinColumn(name = "id_shopping_order", nullable = false)
    @JsonIgnore
    private ShoppingOrder shoppingOrder;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    @JsonIgnore
    private Product product;

}

