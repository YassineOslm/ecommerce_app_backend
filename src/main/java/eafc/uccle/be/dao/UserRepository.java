package eafc.uccle.be.dao;

import eafc.uccle.be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    User findUserByEmail(@Param("email") String email);

}
