
package com.mycompany.festival.service;

import com.mycompany.festival.enums.FestivalState;
import com.mycompany.festival.enums.PerformanceState;
import com.mycompany.festival.enums.RoleName;
import com.mycompany.festival.exception.InvalidOperationException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    private void checkAuthorization(String username, Performance performance) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

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
    
    // Αρχείο: PerformanceService.java

// Μέσα στο PerformanceService.java

@Transactional
public void assignStaffToPerformance(Long performanceId, Long staffUserId) {
    Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + performanceId));

    User staffUser = userRepository.findById(staffUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Staff user not found with ID: " + staffUserId));

    Festival festival = performance.getFestival();

    // --- ΕΛΕΓΧΟΣ ΕΞΟΥΣΙΟΔΟΤΗΣΗΣ ---
    // Παίρνουμε το όνομα του συνδεδεμένου χρήστη
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    
    // Και καλούμε τη μέθοδο που μόλις προσθέσαμε!
    checkIsOrganizerForFestival(currentUsername, festival);
    // --------------------------------

    // Έλεγχος Κατάστασης (ΚΡΙΣΙΜΟ!)
    if (festival.getState() != FestivalState.ASSIGNMENT) {
        throw new InvalidOperationException("Staff can only be assigned when the festival is in the ASSIGNMENT state.");
    }

    // Έλεγχος Ρόλου: Βεβαιώσου ότι ο χρήστης που πάμε να αναθέσουμε είναι όντως Staff του φεστιβάλ.
    boolean isStaffOfFestival = userFestivalRoleRepository
            .existsByUserAndFestivalAndRoleType(staffUser, festival, RoleName.STAFF);

    if (!isStaffOfFestival) {
        throw new InvalidOperationException("Cannot assign user to performance. User is not a registered staff member for this festival.");
    }

    // Αν όλα είναι ΟΚ, κάνε την ανάθεση.
    performance.setAssignedStaff(staffUser);
    performanceRepository.save(performance);
}
    
 private void checkIsOrganizerForFestival(String username, Festival festival) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

    boolean isOrganizer = userFestivalRoleRepository
            .existsByUserAndFestivalAndRoleType(user, festival, RoleName.ORGANIZER);
    
    if (!isOrganizer) {
        throw new UnauthorizedException("User '" + username + "' is not an organizer for festival '" + festival.getName() + "'.");
    }
}   
 
 @Transactional
// Πρόσθεσε αυτή τη μέθοδο στο PerformanceService.java

public void submitPerformance(Long performanceId) {
    Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Performance not found with ID: " + performanceId));
    
    // 1. Έλεγχος Εξουσιοδότησης: Είσαι ο creator της παράστασης;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    checkIsCreatorOfPerformance(currentUsername, performance);

    // 2. Έλεγχος Κατάστασης (του Φεστιβάλ!)
    if (performance.getFestival().getState() != FestivalState.SUBMISSION) {
        throw new InvalidOperationException("Performances can only be submitted when the festival is in SUBMISSION state.");
    }

    // 3. Έλεγχος Κατάστασης (της ίδιας της Παράστασης!)
    if (performance.getState() != PerformanceState.CREATED) {
        throw new InvalidOperationException("Performance must be in CREATED state to be submitted.");
    }
    
    // 4. Αλλαγή Κατάστασης & Αποθήκευση
    performance.setState(PerformanceState.SUBMITTED);
    performanceRepository.save(performance);
}


// Μέσα στο PerformanceService.java, μαζί με τις άλλες private μεθόδους

/**
 * Ελέγχει αν ο χρήστης με το δεδομένο username είναι ο ΔΗΜΙΟΥΡΓΟΣ της παράστασης.
 * Αν δεν είναι, πετάει UnauthorizedException.
 */
private void checkIsCreatorOfPerformance(String username, Performance performance) {
    // Δεν χρειάζεται να ψάξουμε τον user στη βάση εδώ.
    // Απλά συγκρίνουμε το username του creator της παράστασης με το username που μας έδωσαν.
    if (!performance.getCreator().getUsername().equals(username)) {
        throw new UnauthorizedException("User '" + username + "' is not the creator of this performance.");
    }
}
 
}