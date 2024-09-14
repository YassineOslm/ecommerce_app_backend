package eafc.uccle.be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {

    private String name;
    private String productDescription;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private int categoryId; // Corrected typo from 'categoruId' to 'categoryId'
    private String categoryName;
    private List<ImageDto> images;
}
