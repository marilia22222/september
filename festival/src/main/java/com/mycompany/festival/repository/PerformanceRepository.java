package com.mycompany.festival.repository;

import com.mycompany.festival.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByNameContainingIgnoreCase(String name);

    List<Performance> findByGenreContainingIgnoreCase(String genre);

    List<Performance> findByArtists_UsernameContainingIgnoreCase(String artistUsername);
}
