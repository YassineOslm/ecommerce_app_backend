package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Lives;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivesRepository extends JpaRepository<Lives, Long> {
}
