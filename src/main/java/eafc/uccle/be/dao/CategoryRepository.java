package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository <Category, Long> {

    @Modifying
    @Query(value = "DELETE FROM category WHERE id_category = :categoryId", nativeQuery = true)
    void deleteCategoryById(Long categoryId);

}
