package com.mycompany.festival.repository;

import com.mycompany.festival.enums.RoleName;
import com.mycompany.festival.model.Role;
//import com.mycompany.festival.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
