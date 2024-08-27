package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Set<Image> findByProductId(Long productId);
}

