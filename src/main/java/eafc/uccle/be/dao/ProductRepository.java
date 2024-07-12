package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
