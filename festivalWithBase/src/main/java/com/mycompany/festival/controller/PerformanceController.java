
package com.mycompany.festival.controller;

import com.mycompany.festival.objects.PerformanceO;
import com.mycompany.festival.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/festivals/{festivalId}/performances")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    // Δημιουργία νέας παράστασης
    @PostMapping
    public ResponseEntity<PerformanceO> createPerformance(
            @PathVariable Long festivalId,
            @Valid @RequestBody PerformanceO performanceO) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication.getName(); 

        // Το service επιστρέφει πλέον PerformanceO, οπότε το αλλάξαμε εδώ.
        PerformanceO createdPerformance = performanceService.createPerformance(performanceO, festivalId, creatorUsername);
        
        return new ResponseEntity<>(createdPerformance, HttpStatus.CREATED);
    }

    // Ενημέρωση παράστασης
    @PutMapping("/{performanceId}")
    public ResponseEntity<PerformanceO> updatePerformance(
            @PathVariable Long festivalId, // Το κρατάμε για συνέπεια, αν και δεν χρησιμοποιείται στο service.
            @PathVariable Long performanceId,
            @Valid @RequestBody PerformanceO performanceO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        PerformanceO updatedPerformance = performanceService.updatePerformance(performanceId, performanceO, currentUsername);
        return ResponseEntity.ok(updatedPerformance);
    }

    // Ανάκτηση παράστασης με ID
    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceO> getPerformanceById(@PathVariable Long performanceId) {
        // Ο controller δεν χρειάζεται το festivalId εδώ, άρα το αγνοούμε.
        PerformanceO performance = performanceService.getPerformanceById(performanceId);
        return ResponseEntity.ok(performance);
    }

    // Ανάκτηση όλων των παραστάσεων (για το συγκεκριμένο φεστιβάλ)
    // ΣΗΜΕΙΩΣΗ: Η τωρινή μέθοδος στο service φέρνει ΟΛΕΣ τις παραστάσεις από ΟΛΑ τα φεστιβάλ.
    // Θα χρειαζόταν μια νέα μέθοδος στο service για να φέρνει μόνο αυτές του festivalId.
    @GetMapping
    public ResponseEntity<List<PerformanceO>> getAllPerformances() {
        List<PerformanceO> performances = performanceService.getAllPerformances();
        return ResponseEntity.ok(performances);
    }

    // Διαγραφή παράστασης
    @DeleteMapping("/{performanceId}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long performanceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        performanceService.deletePerformance(performanceId, currentUsername);
        return ResponseEntity.noContent().build(); // Χρησιμοποιούμε 204 No Content για επιτυχή διαγραφή.
    }
    
    
    // Endpoint για την ανάθεση ενός Staff σε μια παράσταση
@PostMapping("/{performanceId}/assign-staff")
public ResponseEntity<Void> assignStaffToPerformance(
        @PathVariable Long festivalId,
        @PathVariable Long performanceId,
        @RequestBody Long staffUserId) {
    
    performanceService.assignStaffToPerformance(performanceId, staffUserId);
    return ResponseEntity.ok().build();
}
@PostMapping("/{performanceId}/submit")
public ResponseEntity<Void> submitPerformance(@PathVariable Long performanceId) {
    performanceService.submitPerformance(performanceId);
    return ResponseEntity.ok().build();
}

}