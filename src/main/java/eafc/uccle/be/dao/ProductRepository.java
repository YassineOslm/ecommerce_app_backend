package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Product;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(@Param("id_category") Long categoryId, Pageable pageable);

    Page<Product> findByNameContaining(@Param("name") String productName, Pageable pageable);

    @NonNull
    Optional<Product> findById(@NonNull @Param("id_product") Long idProduct);

    @Query("SELECT p FROM Product p LEFT JOIN Comment c ON p.id = c.product.id WHERE p.category.id = :categoryId GROUP BY p.id ORDER BY AVG(c.grade) DESC")
    Page<Product> findByCategoryIdOrderByAverageRating(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN Comment c ON p.id = c.product.id WHERE p.name LIKE %:name% GROUP BY p.id ORDER BY AVG(c.grade) DESC")
    Page<Product> findByNameContainingOrderByAverageRating(String name, Pageable pageable);

    @Query(value = "SELECT * FROM product WHERE id_category = :categoryId", nativeQuery = true)
    List<Product> findProductsByCategoryId(Long categoryId);

    @Modifying
    @Query(value = "DELETE FROM product WHERE id_category = :categoryId", nativeQuery = true)
    void deleteProductsByCategoryId(Long categoryId);

}
