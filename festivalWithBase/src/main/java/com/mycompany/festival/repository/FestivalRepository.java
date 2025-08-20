package com.mycompany.festival.repository;

import com.mycompany.festival.model.Festival;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long> {

    //public Object findByName(String name);
    // Custom queries can be added here if necessary
    Optional<Festival> findByName(String name);
    List<Festival> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
}
