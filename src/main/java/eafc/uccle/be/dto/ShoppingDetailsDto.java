package eafc.uccle.be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingDetailsDto {

    private int idProduct;
    private String name;
    private int quantity;
    private String subTotalPrice;
    private String unitPrice;

    public ShoppingDetailsDto(int idProduct, String unitPrice, String name, int quantity, String subTotalPrice) {
        this.idProduct = idProduct;
        this.unitPrice = unitPrice;
        this.name = name;
        this.quantity = quantity;
        this.subTotalPrice = subTotalPrice;
    }
}
