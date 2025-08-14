//package com.mycompany.festival.controller;
//
//import com.mycompany.festival.objects.FestivalO;
//import com.mycompany.festival.service.FestivalService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/api/festivals")
//public class FestivalController {
//
//    @Autowired
//    private FestivalService festivalService;
//
//    @PostMapping
//    @PreAuthorize("hasRole('ORGANIZER')")
//    public ResponseEntity<FestivalO> createFestival(@RequestBody @Valid FestivalO festivalO) {
//        FestivalO createdFestival = festivalService.createFestival(festivalO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdFestival);
//    }
//
//    @PostMapping("/{id}/start-submission")
//    @PreAuthorize("hasRole('ORGANIZER')")
//    public ResponseEntity<Void> startSubmission(@PathVariable Long id) {
//        festivalService.startSubmission(id);
//        return ResponseEntity.ok().build();
//    }
//}
//
//


package com.mycompany.festival.controller;

import com.mycompany.festival.objects.FestivalO;
import com.mycompany.festival.service.FestivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid; // Χρησιμοποιούμε javax.validation
import java.util.ArrayList; // Προσθήκη import για ArrayList
import java.util.List; // Προσθήκη import για List

@RestController
@RequestMapping("/api/festivals")
public class FestivalController {    
    @Autowired    
    private FestivalService festivalService;    
    
    // GET endpoint για όλα τα festivals
    // Αυτό επιστρέφει απλά μια κενή λίστα FestivalO, χωρίς να καλεί το service
    @GetMapping
    // @PreAuthorize("hasAnyRole('ORGANIZER', 'VISITOR', 'ARTIST', 'STAFF')") // Μπορείς να το προσθέσεις αν θες authorization
    public ResponseEntity<List<FestivalO>> getAllFestivals() {
          return ResponseEntity.ok(festivalService.getAllFestivals());

    }

    // ΑΦΑΙΡΕΘΗΚΕ: Το GET endpoint για ένα συγκεκριμένο festival (getFestivalById)
    // επειδή η μέθοδος στο service δεν υπήρχε ή δεν ήταν σωστή.

//    @PostMapping    
//    @PreAuthorize("hasRole('ORGANIZER')")    
//    public ResponseEntity<FestivalO> createFestival(@RequestBody @Valid FestivalO festivalO) {        
//        FestivalO createdFestival = festivalService.createFestival(festivalO);        
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdFestival);    
//    }    
    
    
     @GetMapping("/{id}")
    public ResponseEntity<FestivalO> getFestivalById(@PathVariable Long id) {
        FestivalO festival = festivalService.getFestivalById(id);
        return (festival != null) ? ResponseEntity.ok(festival) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FestivalO> createFestival(@RequestBody FestivalO festivalO) {
        FestivalO createdFestival = festivalService.createFestival(festivalO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFestival);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FestivalO> updateFestival(@PathVariable Long id, @RequestBody FestivalO festivalO) {
        FestivalO updatedFestival = festivalService.updateFestival(id, festivalO);
        return (updatedFestival != null) ? ResponseEntity.ok(updatedFestival) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFestival(@PathVariable Long id) {
        boolean removed = festivalService.deleteFestival(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/start-submission")
    public ResponseEntity<Void> startSubmission(@PathVariable Long id) {
        festivalService.startSubmission(id);
        return ResponseEntity.ok().build();
    }



}
