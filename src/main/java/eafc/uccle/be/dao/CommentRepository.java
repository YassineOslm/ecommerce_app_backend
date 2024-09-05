package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByProductId(Long id, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.product.id = :productId")
    Integer countByProductId(Long productId);

    @Query("SELECT AVG(c.grade) FROM Comment c WHERE c.product.id = :productId")
    Double findAverageGradeByProductId(Long productId);

    @Query("SELECT c.product.id, AVG(c.grade) FROM Comment c GROUP BY c.product.id")
    List<Object[]> findAverageRatingForProducts();

}
