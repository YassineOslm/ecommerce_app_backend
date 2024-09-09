package eafc.uccle.be.dao;

import eafc.uccle.be.entity.ShoppingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Long> {
}
