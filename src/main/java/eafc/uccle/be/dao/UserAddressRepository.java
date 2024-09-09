package eafc.uccle.be.dao;

import eafc.uccle.be.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Set;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Query(value = "SELECT * FROM user_address WHERE id_user_address IN (SELECT id_user_address FROM lives WHERE id_user = :userId)", nativeQuery = true)
    Set<UserAddress> findUserAddressesByUserId(@Param("userId") Long userId);

}
