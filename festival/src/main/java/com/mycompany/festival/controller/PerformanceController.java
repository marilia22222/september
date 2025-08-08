// src/main/java/com/mycompany/festival/controller/PerformanceController.java
package com.mycompany.festival.controller;

import com.mycompany.festival.objects.PerformanceO;
import com.mycompany.festival.model.Performance;
import com.mycompany.festival.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Βεβαιώσου ότι έχεις αυτή την import αν χρησιμοποιείς validation

import java.util.List;

@RestController
@RequestMapping("/api/festivals/{festivalId}/performances") // Το {festivalId} στο path είναι κρίσιμο
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    /**
     * Δημιουργεί μια νέα παράσταση για ένα συγκεκριμένο φεστιβάλ.
     * Ο creatorUsername λαμβάνεται αυτόματα από τον αυθεντικοποιημένο χρήστη.
     *
     * @param festivalId   Το ID του φεστιβάλ στο οποίο ανήκει η παράσταση.
     * @param performanceO Τα δεδομένα της παράστασης.
     * @return ResponseEntity με την αποθηκευμένη παράσταση και HTTP status 201 Created.
     */
    @PostMapping
    public ResponseEntity<Performance> createPerformance(
            @PathVariable Long festivalId, // Αυτό παίρνει το ID του φεστιβάλ από το URL
            @Valid @RequestBody PerformanceO performanceO) {
        
        // Λάβε το username του αυθεντικοποιημένου χρήστη
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication.getName(); 

        Performance createdPerformance = performanceService.createPerformance(performanceO, festivalId, creatorUsername);
        return new ResponseEntity<>(createdPerformance, HttpStatus.CREATED);
    }

    /**
     * Ενημερώνει μια υπάρχουσα παράσταση.
     * Ο currentUsername λαμβάνεται αυτόματα από τον αυθεντικοποιημένο χρήστη για έλεγχο εξουσιοδότησης.
     *
     * @param festivalId    (Προαιρετικό στο path, αλλά καλό για context)
     * @param performanceId Το ID της παράστασης προς ενημέρωση.
     * @param performanceO  Τα νέα δεδομένα της παράστασης.
     * @return ResponseEntity με την ενημερωμένη παράσταση και HTTP status 200 OK.
     */
    @PutMapping("/{performanceId}")
    public ResponseEntity<Performance> updatePerformance(
            @PathVariable Long festivalId, // Μπορεί να μην χρησιμοποιείται άμεσα, αλλά καλό για συνέπεια στο URL
            @PathVariable Long performanceId,
            @Valid @RequestBody PerformanceO performanceO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Η υπηρεσία θα κάνει τους ελέγχους εξουσιοδότησης
        Performance updatedPerformance = performanceService.updatePerformance(performanceId, performanceO, currentUsername);
        return new ResponseEntity<>(updatedPerformance, HttpStatus.OK);
    }

    /**
     * Ανακτά μια συγκεκριμένη παράσταση βάσει ID.
     * @param performanceId Το ID της παράστασης.
     * @return ResponseEntity με την παράσταση και HTTP status 200 OK.
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<Performance> getPerformanceById(@PathVariable Long performanceId) {
        Performance performance = performanceService.getPerformanceById(performanceId);
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }

    /**
     * Ανακτά όλες τις παραστάσεις.
     * @return ResponseEntity με μια λίστα παραστάσεων και HTTP status 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Performance>> getAllPerformances() {
        List<Performance> performances = performanceService.getAllPerformances();
        return new ResponseEntity<>(performances, HttpStatus.OK);
    }

    /**
     * Διαγράφει μια παράσταση.
     * Ο currentUsername λαμβάνεται αυτόματα από τον αυθεντικοποιημένο χρήστη για έλεγχο εξουσιοδότησης.
     *
     * @param performanceId Το ID της παράστασης προς διαγραφή.
     * @return ResponseEntity με HTTP status 204 No Content.
     */
    @DeleteMapping("/{performanceId}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long performanceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        performanceService.deletePerformance(performanceId, currentUsername);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}