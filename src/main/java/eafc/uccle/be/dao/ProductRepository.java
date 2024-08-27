package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Product;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(@Param("id_category") Long categoryId, Pageable pageable);

    Page<Product> findByNameContaining(@Param("name") String productName, Pageable pageable);

    @NonNull
    Optional<Product> findById(@NonNull @Param("id_product") Long idProduct);

}
