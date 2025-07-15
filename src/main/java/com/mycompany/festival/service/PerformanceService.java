package com.mycompany.festival.service;

import com.mycompany.festival.model.Performance;
import com.mycompany.festival.objects.PerformanceO;
import com.mycompany.festival.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;
public PerformanceO createPerformance(PerformanceO performanceO) {
    // Map fields from PerformanceO to Performance entity
    Performance performance = new Performance();
    
    if (performanceO.getName() != null) {
        performance.setName(performanceO.getName());
    }
    if (performanceO.getDescription() != null) {
        performance.setDescription(performanceO.getDescription());
    }
    if (performanceO.getDuration() != null) {
        performance.setDuration(performanceO.getDuration());
    }
    if (performanceO.getGenre() != null) {
        performance.setGenre(performanceO.getGenre());
    }
    if (performanceO.getTechnicalRequirements() != null) {
        performance.setTechnicalRequirements(performanceO.getTechnicalRequirements());
    }
    if (performanceO.getCreatedDate() != null) {
        performance.setCreatedDate(performanceO.getCreatedDate());
    }

    // Save the performance entity
    performance = performanceRepository.save(performance);

    // Map back to PerformanceO for the response
    PerformanceO savedPerformanceO = new PerformanceO();
    savedPerformanceO.setId(performance.getId());
    savedPerformanceO.setName(performance.getName());
    savedPerformanceO.setDescription(performance.getDescription());
    savedPerformanceO.setDuration(performance.getDuration());
    savedPerformanceO.setGenre(performance.getGenre());
    savedPerformanceO.setTechnicalRequirements(performance.getTechnicalRequirements());
    savedPerformanceO.setCreatedDate(performance.getCreatedDate());

    return savedPerformanceO;
}

}
