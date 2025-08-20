// src/main/java/com/mycompany/festival/repository/UserFestivalRoleRepository.java
package com.mycompany.festival.repository;

import com.mycompany.festival.model.UserFestivalRole;
import com.mycompany.festival.model.User;
import com.mycompany.festival.model.Festival;
import com.mycompany.festival.enums.RoleName; // Χρησιμοποιούμε το δικό σου RoleName enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFestivalRoleRepository extends JpaRepository<UserFestivalRole, Long> {
    
     
/*Finds a UserFestivalRole by user, festival, and role type.
Useful for checking if a specific user has a specific role for a specific festival.
 */
Optional<UserFestivalRole> findByUserAndFestivalAndRoleType(User user, Festival festival, RoleName roleType);


     
/*Checks if a UserFestivalRole exists for a given user, festival, and role type.*/
  boolean existsByUserAndFestivalAndRoleType(User user, Festival festival, RoleName roleType);


     
/* Finds all UserFestivalRoles for a given user.
Useful for getting all roles a user has across all festivals.*/
List<UserFestivalRole> findByUser(User user);


     
/*Finds all UserFestivalRoles for a given festival.
Useful for getting all users and their roles for a specific festival.*/
List<UserFestivalRole> findByFestival(Festival festival);
}