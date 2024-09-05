package eafc.uccle.be.dto;

public class ProductRatingDto {

    private Long productId;
    private Double averageRating;

    public ProductRatingDto(Long productId, Double averageRating) {
        this.productId = productId;
        this.averageRating = averageRating;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
