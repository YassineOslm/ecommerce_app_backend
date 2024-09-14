package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Set<Image> findByProductId(Long productId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}

