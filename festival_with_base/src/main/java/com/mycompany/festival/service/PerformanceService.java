//package com.mycompany.festival.service;
//
//import com.mycompany.festival.objects.PerformanceO;
//import com.mycompany.festival.enums.RoleName; // Βεβαιώσου ότι έχεις το RoleName enum
//import com.mycompany.festival.exception.ResourceNotFoundException;
//import com.mycompany.festival.exception.UnauthorizedException;
////import com.mycompany.festival.exceptions.ResourceNotFoundException;
////import com.mycompany.festival.exceptions.UnauthorizedException;
//import com.mycompany.festival.model.Festival;
//import com.mycompany.festival.model.Performance;
//import com.mycompany.festival.model.User;
//import com.mycompany.festival.model.UserFestivalRole;
//import com.mycompany.festival.repository.FestivalRepository;
//import com.mycompany.festival.repository.PerformanceRepository;
//import com.mycompany.festival.repository.UserFestivalRoleRepository;
//import com.mycompany.festival.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional; // Για findById
//import java.time.LocalDateTime; // Για PerformanceTime, αν είναι LocalDateTime
//
//@Service
//public class PerformanceService {
//
//    @Autowired
//    private PerformanceRepository performanceRepository;
//    @Autowired
//    private FestivalRepository festivalRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserFestivalRoleRepository userFestivalRoleRepository;
//
//    /**
//     * Δημιουργεί μια νέα παράσταση για ένα συγκεκριμένο φεστιβάλ και αναθέτει το ρόλο ARTIST στον δημιουργό της.
//     *
//     * @param performanceO Τα δεδομένα της παράστασης.
//     * @param festivalId   Το ID του φεστιβάλ στο οποίο ανήκει η παράσταση.
//     * @param creatorUsername Το username του χρήστη που δημιουργεί την παράσταση.
//     * @return Την αποθηκευμένη οντότητα Performance.
//     * @throws ResourceNotFoundException Αν το φεστιβάλ ή ο δημιουργός δεν βρεθούν.
//     */
//    @Transactional
//    public Performance createPerformance(PerformanceO performanceO, Long festivalId, String creatorUsername) {
//        // 1. Βρες το Festival στο οποίο ανήκει η παράσταση
//        Festival festival = festivalRepository.findById(festivalId)
//                .orElseThrow(() -> new ResourceNotFoundException("Festival not found with ID: " + festivalId));
//
//        // 2. Βρες τον χρήστη (creator) που δημιουργεί την παράσταση
//        User creator = userRepository.findByUsername(creatorUsername)
//                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found."));
//
//        // 3. Δημιούργησε τη νέα Performance entity και αντιστοίχισε τα πεδία από το DTO
//        Performance performance = new Performance();
//        performance.setName(performanceO.getName()); // Βεβαιώσου ότι το Performance model έχει πεδίο 'name'
//        performance.setDescription(performanceO.getDescription());
//        performance.setGenre(performanceO.getGenre());
//        //performance.setDurationMinutes(performanceO.getDurationMinutes()); // Βεβαιώσου ότι το Performance model έχει πεδίο 'durationMinutes'
//        //performance.setPerformanceTime(performanceO.getPerformanceTime()); // Βεβαιώσου ότι το Performance model έχει πεδίο 'performanceTime' (LocalDateTime)
//        //performance.setArtistName(performanceO.getArtistName()); // Βεβαιώσου ότι το Performance model έχει πεδίο 'artistName' (το username του καλλιτέχνη)
//        // Πρόσθεσε εδώ οποιαδήποτε άλλα νέα πεδία (π.χ., band members, technicalRequirements)
//        // performance.setTechnicalRequirements(performanceO.getTechnicalRequirements());
//        // performance.setBandMembers(performanceO.getBandMembers());
//        // performance.setMerchandiseOptions(performanceO.getMerchandiseOptions());
//        // performance.setSetlist(performanceO.getSetlist());
//        // performance.setPreferredTimes(performanceO.getPreferredTimes());
//
//
//        performance.setFestival(festival); // Σύνδεσε την παράσταση με το φεστιβάλ
//
//        // 4. Αποθήκευσε την Performance για να πάρει ID
//        performance = performanceRepository.save(performance);
//
//        // 5. Ανάθεση ρόλου ARTIST στον δημιουργό για αυτό το φεστιβάλ
//        // Ελέγχει αν ο χρήστης έχει ήδη το ρόλο ARTIST για αυτό το φεστιβάλ.
//        // Αν όχι, δημιουργεί και αποθηκεύει την εγγραφή UserFestivalRole.
//        if (!userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(creator, festival, RoleName.ARTIST)) {
//            UserFestivalRole artistRole = new UserFestivalRole(creator, festival, RoleName.ARTIST);
//            userFestivalRoleRepository.save(artistRole);
//        }
//
//        return performance;
//    }
//
//    /**
//     * Ενημερώνει μια υπάρχουσα παράσταση. Μόνο ο συσχετισμένος καλλιτέχνης ή ένας organizer του φεστιβάλ
//     * μπορεί να ενημερώσει την παράσταση.
//     *
//     * @param performanceId Το ID της παράστασης προς ενημέρωση.
//     * @param performanceO  Τα νέα δεδομένα της παράστασης.
//     * @param currentUsername Το username του χρήστη που κάνει την ενημέρωση.
//     * @return Την ενημερωμένη οντότητα Performance.
//     * @throws ResourceNotFoundException Αν η παράσταση ή ο χρήστης δεν βρεθούν.
//     * @throws UnauthorizedException Αν ο χρήστης δεν έχει δικαίωμα ενημέρωσης.
//     */
//    @Transactional
//    public Performance updatePerformance(Long performanceId, PerformanceO performanceO, String currentUsername) {
//        Performance performance = performanceRepository.findById(performanceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + performanceId));
//        User currentUser = userRepository.findByUsername(currentUsername)
//                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));
//
//        // Έλεγχος εξουσιοδότησης: Ο χρήστης πρέπει να είναι είτε ORGANIZER του φεστιβάλ, είτε ο ARTIST που δημιούργησε την παράσταση.
//        boolean isOrganizerOfFestival = userFestivalRoleRepository
//                .existsByUserAndFestivalAndRoleType(currentUser, performance.getFestival(), RoleName.ORGANIZER);
//        boolean isArtistOfPerformance = performance.getArtistName().equals(currentUsername) &&
//                                        userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(currentUser, performance.getFestival(), RoleName.ARTIST);
//
//        if (!isOrganizerOfFestival && !isArtistOfPerformance) {
//            throw new UnauthorizedException("You are not authorized to update this performance. Only the associated artist or an organizer can.");
//        }
//
//        // Ενημέρωσε τα πεδία της Performance από το DTO
//        performance.setName(performanceO.getName());
//        performance.setDescription(performanceO.getDescription());
//        performance.setGenre(performanceO.getGenre());
//        //performance.setDurationMinutes(performanceO.getDurationMinutes());
//        //performance.setPerformanceTime(performanceO.getPerformanceTime());
//        //performance.setArtistName(performanceO.getArtistName());
//        // performance.setTechnicalRequirements(performanceO.getTechnicalRequirements());
//        // performance.setBandMembers(performanceO.getBandMembers());
//        // performance.setMerchandiseOptions(performanceO.getMerchandiseOptions());
//        // performance.setSetlist(performanceO.getSetlist());
//        // performance.setPreferredTimes(performanceO.getPreferredTimes());
//
//        return performanceRepository.save(performance);
//    }
//
//    /**
//     * Ανακτά όλες τις παραστάσεις.
//     * @return Μια λίστα με όλες τις παραστάσεις.
//     */
//    public List<Performance> getAllPerformances() {
//        return performanceRepository.findAll();
//    }
//
//    /**
//     * Ανακτά μια παράσταση βάσει ID.
//     * @param id Το ID της παράστασης.
//     * @return Την οντότητα Performance.
//     * @throws ResourceNotFoundException Αν η παράσταση δεν βρεθεί.
//     */
//    public Performance getPerformanceById(Long id) {
//        return performanceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + id));
//    }
//
//    /**
//     * Διαγράφει μια παράσταση. Μόνο ο συσχετισμένος καλλιτέχνης (αν είναι ο δημιουργός) ή ένας organizer του φεστιβάλ
//     * μπορεί να διαγράψει την παράσταση.
//     *
//     * @param performanceId Το ID της παράστασης προς διαγραφή.
//     * @param currentUsername Το username του χρήστη που κάνει τη διαγραφή.
//     * @throws ResourceNotFoundException Αν η παράσταση ή ο χρήστης δεν βρεθούν.
//     * @throws UnauthorizedException Αν ο χρήστης δεν έχει δικαίωμα διαγραφής.
//     */
//    @Transactional
//    public void deletePerformance(Long performanceId, String currentUsername) {
//        Performance performance = performanceRepository.findById(performanceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + performanceId));
//        User currentUser = userRepository.findByUsername(currentUsername)
//                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));
//
//        // Έλεγχος εξουσιοδότησης: Μόνο ο ORGANIZER του φεστιβάλ ή ο ARTIST της παράστασης (αν είναι ο ίδιος ο δημιουργός)
//        // θα πρέπει να μπορεί να διαγράψει.
//        boolean isOrganizerOfFestival = userFestivalRoleRepository
//                .existsByUserAndFestivalAndRoleType(currentUser, performance.getFestival(), RoleName.ORGANIZER);
//        boolean isArtistOfPerformance = performance.getArtistName().equals(currentUsername) &&
//                                        userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(currentUser, performance.getFestival(), RoleName.ARTIST);
//
//        if (!isOrganizerOfFestival && !isArtistOfPerformance) {
//            throw new UnauthorizedException("You are not authorized to delete this performance.");
//        }
//
//        performanceRepository.delete(performance);
//    }
//}
package com.mycompany.festival.service;

import com.mycompany.festival.enums.RoleName;
import com.mycompany.festival.exception.ResourceNotFoundException;
import com.mycompany.festival.exception.UnauthorizedException;
import com.mycompany.festival.model.Festival;
import com.mycompany.festival.model.Performance;
import com.mycompany.festival.model.User;
import com.mycompany.festival.model.UserFestivalRole;
import com.mycompany.festival.objects.PerformanceO;
import com.mycompany.festival.repository.FestivalRepository;
import com.mycompany.festival.repository.PerformanceRepository;
import com.mycompany.festival.repository.UserFestivalRoleRepository;
import com.mycompany.festival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFestivalRoleRepository userFestivalRoleRepository;

    /**
     * Δημιουργεί μια νέα παράσταση για ένα φεστιβάλ και δίνει στον δημιουργό τον ρόλο του ARTIST.
     */
    @Transactional
    public PerformanceO createPerformance(PerformanceO performanceO, Long festivalId, String creatorUsername) {
        // 1. Βρίσκουμε το Festival entity από τη βάση.
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create performance: Festival not found with ID: " + festivalId));

        // 2. Βρίσκουμε τον User entity (τον δημιουργό) από τη βάση.
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create performance: Creator user not found."));

        // 3. Μετατρέπουμε το DTO σε Entity και θέτουμε τις σχέσεις.
        Performance performance = convertOtoEntity(performanceO);
        performance.setFestival(festival); // Συνδέουμε την παράσταση με το φεστιβάλ.
        performance.setCreator(creator);   // Συνδέουμε την παράσταση με τον δημιουργό της.

        // 4. Αποθηκεύουμε την Performance για να πάρει ID.
        Performance savedPerformance = performanceRepository.save(performance);

        // 5. Δίνουμε στον χρήστη τον ρόλο του ARTIST για αυτό το φεστιβάλ (αν δεν τον έχει ήδη).
        if (!userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(creator, festival, RoleName.ARTIST)) {
            UserFestivalRole artistRole = new UserFestivalRole(creator, festival, RoleName.ARTIST);
            userFestivalRoleRepository.save(artistRole);
        }

        // 6. Επιστρέφουμε το DTO της παράστασης που δημιουργήθηκε.
        return convertEntityToO(savedPerformance);
    }

    /**
     * Ενημερώνει μια υπάρχουσα παράσταση.
     */
    @Transactional
    public PerformanceO updatePerformance(Long performanceId, PerformanceO performanceO, String currentUsername) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + performanceId));
        
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));

        // Έλεγχος Εξουσιοδότησης: Επιτρέπεται μόνο στον δημιουργό ή σε έναν organizer.
        checkAuthorization(currentUser, performance);

        // Ενημερώνουμε τα πεδία
        performance.setName(performanceO.getName());
        performance.setDescription(performanceO.getDescription());
        performance.setGenre(performanceO.getGenre());
        performance.setDuration(performanceO.getDuration());
        performance.setTechnicalRequirements(performanceO.getTechnicalRequirements());

        Performance updatedPerformance = performanceRepository.save(performance);
        return convertEntityToO(updatedPerformance);
    }

    /**
     * Ανακτά όλες τις παραστάσεις.
     */
    public List<PerformanceO> getAllPerformances() {
        return performanceRepository.findAll()
                .stream()
                .map(this::convertEntityToO)
                .collect(Collectors.toList());
    }

    /**
     * Ανακτά μια παράσταση βάσει ID.
     */
    public PerformanceO getPerformanceById(Long id) {
        return performanceRepository.findById(id)
                .map(this::convertEntityToO)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + id));
    }

    /**
     * Διαγράφει μια παράσταση.
     */
    @Transactional
    public void deletePerformance(Long performanceId, String currentUsername) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + performanceId));
        
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));

        // Έλεγχος Εξουσιοδότησης: Επιτρέπεται μόνο στον δημιουργό ή σε έναν organizer.
        checkAuthorization(currentUser, performance);

        performanceRepository.delete(performance);
    }


    // --- ΒΟΗΘΗΤΙΚΕΣ (PRIVATE) ΜΕΘΟДОΙ ---

    /**
     * Βοηθητική μέθοδος για έλεγχο εξουσιοδότησης. Πετάει UnauthorizedException αν ο χρήστης δεν έχει δικαίωμα.
     */
    private void checkAuthorization(User user, Performance performance) {
        boolean isOrganizer = userFestivalRoleRepository
                .existsByUserAndFestivalAndRoleType(user, performance.getFestival(), RoleName.ORGANIZER);
        
        boolean isCreator = performance.getCreator().equals(user);

        if (!isOrganizer && !isCreator) {
            throw new UnauthorizedException("You are not authorized to modify this performance. Only its creator or a festival organizer can.");
        }
    }
    
    /**
     * Μετατρέπει ένα Performance DTO σε Performance Entity.
     */
    private Performance convertOtoEntity(PerformanceO o) {
        Performance p = new Performance();
        p.setName(o.getName());
        p.setDescription(o.getDescription());
        p.setDuration(o.getDuration());
        p.setGenre(o.getGenre());
        p.setTechnicalRequirements(o.getTechnicalRequirements());
        return p;
    }
    
    /**
     * Μετατρέπει ένα Performance Entity σε Performance DTO.
     */
    private PerformanceO convertEntityToO(Performance p) {
        PerformanceO o = new PerformanceO();
        o.setId(p.getId());
        o.setName(p.getName());
        o.setDescription(p.getDescription());
        o.setDuration(p.getDuration());
        o.setGenre(p.getGenre());
        o.setTechnicalRequirements(p.getTechnicalRequirements());
        o.setCreatedDate(p.getCreatedDate());
        return o;
    }
}