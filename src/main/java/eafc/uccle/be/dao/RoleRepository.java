package eafc.uccle.be.dao;

import eafc.uccle.be.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM role WHERE id_role = :id", nativeQuery = true)
    Role findRoleById(@Param("id") Long id);
}
