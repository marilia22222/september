
package com.mycompany.festival.service;

import com.mycompany.festival.enums.FestivalState;
import com.mycompany.festival.enums.RoleName;
import com.mycompany.festival.exception.ConflictException;
import com.mycompany.festival.exception.InvalidOperationException;
import com.mycompany.festival.exception.ResourceNotFoundException;
import com.mycompany.festival.exception.UnauthorizedException;
import com.mycompany.festival.model.Festival;
import com.mycompany.festival.model.User;
import com.mycompany.festival.model.UserFestivalRole;
import com.mycompany.festival.objects.FestivalO;
import com.mycompany.festival.repository.FestivalRepository;
import com.mycompany.festival.repository.UserFestivalRoleRepository;
import com.mycompany.festival.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class FestivalService {

    // Αυτά τα @Autowired "φέρνουν" τα Repositories ώστε το Service να μπορεί να μιλήσει με τη βάση δεδομένων.
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFestivalRoleRepository userFestivalRoleRepository;

    private void checkIsOrganizerForFestival(String username, Festival festival) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

    boolean isOrganizer = userFestivalRoleRepository
            .existsByUserAndFestivalAndRoleType(user, festival, RoleName.ORGANIZER);
    
    if (!isOrganizer) {
        throw new UnauthorizedException("User '" + username + "' is not an organizer for festival '" + festival.getName() + "'.");
    }
}
    
    
    
    
    /**
     * Δημιουργεί ένα νέο φεστιβάλ, το αποθηκεύει στη βάση δεδομένων,
     * και δίνει στον χρήστη που το δημιούργησε τον ρόλο του ORGANIZER.
     * @param festivalO Το αντικείμενο με τα δεδομένα του φεστιβάλ από το request.
     * @param creatorUsername Το όνομα του συνδεδεμένου χρήστη που κάνει τη δημιουργία.
     * @return Ένα FestivalO DTO με τα δεδομένα του φεστιβάλ που δημιουργήθηκε (συμπεριλαμβανομένου του νέου του ID).
     */
    
    
    
    
    @Transactional
    public FestivalO createFestival(FestivalO festivalO, String creatorUsername) {
        // Έλεγχος αν υπάρχει ήδη φεστιβάλ με το ίδιο όνομα.
        if (festivalRepository.findByName(festivalO.getName()).isPresent()) {
            throw new ConflictException("Festival with name '" + festivalO.getName() + "' already exists.");
        }

        // Βρίσκουμε τον χρήστη (User entity) από τη βάση με βάση το username του.
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found: " + creatorUsername));

        // Μετατρέπουμε το DTO (FestivalO) σε Entity (Festival) για να μπορέσει να αποθηκευτεί.
        Festival festivalToSave = convertOtoEntity(festivalO);
        festivalToSave.setState(FestivalState.CREATED); // Η αρχική κατάσταση είναι πάντα CREATED.

        // Αποθηκεύουμε το νέο festival entity στη βάση. Η βάση του δίνει αυτόματα ένα ID.
        Festival savedFestival = festivalRepository.save(festivalToSave);

        // Δημιουργούμε τον ρόλο: συνδέουμε τον χρήστη (creator) με το φεστιβάλ (savedFestival) με τον ρόλο ORGANIZER.
        UserFestivalRole organizerRole = new UserFestivalRole(creator, savedFestival, RoleName.ORGANIZER);
        userFestivalRoleRepository.save(organizerRole);

        // Μετατρέπουμε το αποθηκευμένο entity (που τώρα έχει ID) πάλι σε DTO για να το επιστρέψουμε στον Controller.
        return convertEntityToO(savedFestival);
    }

    /**
     * Επιστρέφει μια λίστα με όλα τα φεστιβάλ από τη βάση δεδομένων.
     * @return Μια λίστα από FestivalO DTOs.
     */
    public List<FestivalO> getAllFestivals() {
        return festivalRepository.findAll() // Παίρνει όλα τα Festival entities από τη βάση
                .stream()                   // Τα μετατρέπει σε stream
                .map(this::convertEntityToO) // Για κάθε entity, το μετατρέπει σε DTO
                .collect(Collectors.toList()); // Τα μαζεύει πάλι σε μια λίστα
    }

    /**
     * Βρίσκει ένα συγκεκριμένο φεστιβάλ με βάση το ID του.
     * @param id Το ID του φεστιβάλ.
     * @return Το FestivalO DTO αν βρεθεί.
     * @throws ResourceNotFoundException αν δεν υπάρχει φεστιβάλ με αυτό το ID.
     */
    public FestivalO getFestivalById(Long id) {
        return festivalRepository.findById(id) // Ψάχνει το Festival entity στη βάση
                .map(this::convertEntityToO)   // Αν το βρει, το μετατρέπει σε DTO
                .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id)); // Αλλιώς, πετάει exception.
    }

    /**
     * Ενημερώνει τα στοιχεία ενός υπάρχοντος φεστιβάλ.
     * @param id Το ID του φεστιβάλ προς ενημέρωση.
     * @param festivalDetailsO Το DTO με τα νέα στοιχεία.
     * @return Το ενημερωμένο FestivalO DTO.
     */
    @Transactional
    public FestivalO updateFestival(Long id, FestivalO festivalDetailsO) {
        // Βρίσκουμε το υπάρχον festival entity στη βάση.
        Festival existingFestival = festivalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: ".concat(id.toString())));

        // --- ΕΛΕΓΧΟΣ ΕΞΟΥΣΙΟΔΟΤΗΣΗΣ ---
        // Παίρνουμε το όνομα του συνδεδεμένου χρήστη.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        // Καλούμε τη βοηθητική μέθοδο για να ελέγξουμε αν ο χρήστης είναι Organizer
        // για το 'existingFestival' που μόλις βρήκαμε.
        checkIsOrganizerForFestival(currentUsername, existingFestival);
        // --------------------------------

        // Ενημερώνουμε τα πεδία του entity με τις νέες τιμές από το DTO.
        existingFestival.setName(festivalDetailsO.getName());
        existingFestival.setDescription(festivalDetailsO.getDescription());
        existingFestival.setLocation(festivalDetailsO.getLocation());
        existingFestival.setStartDate(festivalDetailsO.getStartDate());
        existingFestival.setEndDate(festivalDetailsO.getEndDate());
        existingFestival.setMaxCapacity(festivalDetailsO.getMaxCapacity());
        existingFestival.setTicketPrice(festivalDetailsO.getTicketPrice());

        // Αποθηκεύουμε το ενημερωμένο entity.
        Festival updatedFestival = festivalRepository.save(existingFestival);

        // Επιστρέφουμε το ενημερωμένο DTO.
        return convertEntityToO(updatedFestival);
    }

    /**
     * Διαγράφει ένα φεστιβάλ, μόνο αν είναι σε κατάσταση CREATED.
     * @param id Το ID του φεστιβάλ προς διαγραφή.
     */
    @Transactional
    public void deleteFestival(Long id) {
        Festival festival = festivalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

        //  Πρόσθεσε εδώ έλεγχο ασφαλείας! (Ο χρήστης πρέπει να είναι ORGANIZER)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
String currentUsername = authentication.getName();

checkIsOrganizerForFestival(currentUsername, festival);
        // Έλεγχος επιχειρησιακής λογικής: σύμφωνα με την εκφώνηση, η διαγραφή επιτρέπεται μόνο στην αρχική κατάσταση.
        if (festival.getState() != FestivalState.CREATED) {
            throw new InvalidOperationException("Festival can only be deleted in CREATED state. Current state: " + festival.getState());
        }

        // Σημαντικό: Πρέπει να σβήσουμε πρώτα τους ρόλους που συνδέονται με αυτό το φεστιβάλ.
        List<UserFestivalRole> roles = userFestivalRoleRepository.findByFestival(festival);
        userFestivalRoleRepository.deleteAll(roles);

        // Τώρα μπορούμε να σβήσουμε με ασφάλεια το ίδιο το φεστιβάλ.
        festivalRepository.delete(festival);
    }
    
    
    

    /**
     * Αλλάζει την κατάσταση ενός φεστιβάλ από CREATED σε SUBMISSION.
     * @param id Το ID του φεστιβάλ.
     */
    @Transactional
    public void startSubmission(Long id) {
        Festival festival = festivalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

        // Πρόσθεσε εδώ έλεγχο ασφαλείας! (Ο χρήστης πρέπει να είναι ORGANIZER)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
String currentUsername = authentication.getName();

checkIsOrganizerForFestival(currentUsername, festival);
        // Έλεγχος επιχειρησιακής λογικής: η μετάβαση επιτρέπεται μόνο από την κατάσταση CREATED.
        if (festival.getState() != FestivalState.CREATED) {
            throw new InvalidOperationException("Cannot start submission. Festival is not in CREATED state.");
        }

        festival.setState(FestivalState.SUBMISSION);
        festivalRepository.save(festival);
    }


    // --- ΒΟΗΘΗΤΙΚΕΣ (PRIVATE) ΜΕΘΟΔΟΙ ΓΙΑ ΜΕΤΑΤΡΟΠΕΣ ---

    /**
     * Μετατρέπει ένα Festival DTO (FestivalO) σε ένα Festival Entity.
     */
    private Festival convertOtoEntity(FestivalO festivalO) {
        Festival festival = new Festival();
        festival.setName(festivalO.getName());
        festival.setDescription(festivalO.getDescription());
        festival.setLocation(festivalO.getLocation());
        festival.setStartDate(festivalO.getStartDate());
        festival.setEndDate(festivalO.getEndDate());
        festival.setMaxCapacity(festivalO.getMaxCapacity());
        festival.setTicketPrice(festivalO.getTicketPrice());
        // ΣΗΜΕΙΩΣΗ: Δεν βάζουμε το ID ή το State εδώ. Αυτά τα διαχειρίζεται η λογική του service.
        return festival;
    }

    /**
     * Μετατρέπει ένα Festival Entity σε ένα Festival DTO (FestivalO).
     */
    private FestivalO convertEntityToO(Festival festival) {
        FestivalO festivalO = new FestivalO();
        festivalO.setId(festival.getId());
        festivalO.setName(festival.getName());
        festivalO.setDescription(festival.getDescription());
        festivalO.setLocation(festival.getLocation());
        festivalO.setStartDate(festival.getStartDate());
        festivalO.setEndDate(festival.getEndDate());
        festivalO.setState(festival.getState());
        festivalO.setMaxCapacity(festival.getMaxCapacity());
        festivalO.setTicketPrice(festival.getTicketPrice());
        return festivalO;
    }
    
    
    
    @Transactional
public void addStaffToFestival(Long festivalId, Long userId) {
    Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new ResourceNotFoundException("Festival not found with ID: " + festivalId));
    
    User userToAdd = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User to add as staff not found with ID: " + userId));
    
    // Πρόσθεσε έλεγχο εξουσιοδότησης εδώ! Μόνο ένας ORGANIZER πρέπει να μπορεί να το κάνει αυτό.
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    checkIsOrganizerForFestival(currentUsername, festival);

// (Θα χρειαστείς το username του τρέχοντος χρήστη από το SecurityContextHolder)

    // Έλεγχος αν ο χρήστης είναι ήδη Staff
    if (userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(userToAdd, festival, RoleName.STAFF)) {
        throw new ConflictException("User is already staff for this festival.");
    }

    // Δημιουργούμε τον νέο ρόλο και τον αποθηκεύουμε
    UserFestivalRole staffRole = new UserFestivalRole(userToAdd, festival, RoleName.STAFF);
    userFestivalRoleRepository.save(staffRole);
}

@Transactional
public void changeStateToAssignment(Long id) {
    Festival festival = festivalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

    // Έλεγχos Εξουσιοδότησης
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    checkIsOrganizerForFestival(auth.getName(), festival);

    // Έλεγχος Επιχειρησιακής Λογικής (Το πιο σημαντικό!)
    if (festival.getState() != FestivalState.SUBMISSION) {
        throw new InvalidOperationException("Festival must be in SUBMISSION state to move to ASSIGNMENT.");
    }

    // Αλλαγή της Κατάστασης
    festival.setState(FestivalState.ASSIGNMENT);
    festivalRepository.save(festival);
}

@Transactional
public void changeStateToReview(Long id) {
    Festival festival = festivalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

    // Έλεγχος Εξουσιοδότησης
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    checkIsOrganizerForFestival(auth.getName(), festival);
    
    // Έλεγχος Επιχειρησιακής Λογικής
    if (festival.getState() != FestivalState.ASSIGNMENT) {
        throw new InvalidOperationException("Festival must be in ASSIGNMENT state to move to REVIEW.");
    }

    // Αλλαγή της Κατάστασης
    festival.setState(FestivalState.REVIEW);
    festivalRepository.save(festival);
}


// Add these methods inside your FestivalService class

@Transactional
public void changeStateToScheduling(Long id) {
    Festival festival = festivalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

    // Authorization Check
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    checkIsOrganizerForFestival(auth.getName(), festival);

    // Business Logic Check
    if (festival.getState() != FestivalState.REVIEW) {
        throw new InvalidOperationException("Festival must be in REVIEW state to move to SCHEDULING.");
    }

    // State Change
    festival.setState(FestivalState.SCHEDULING);
    festivalRepository.save(festival);
}

@Transactional
public void changeStateToFinalSubmission(Long id) {
    Festival festival = festivalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

    // Authorization Check
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    checkIsOrganizerForFestival(auth.getName(), festival);

    // Business Logic Check
    if (festival.getState() != FestivalState.SCHEDULING) {
        throw new InvalidOperationException("Festival must be in SCHEDULING state to move to FINAL_SUBMISSION.");
    }

    // State Change
    festival.setState(FestivalState.FINAL_SUBMISSION);
    festivalRepository.save(festival);
}

@Transactional
public void changeStateToDecision(Long id) {
    Festival festival = festivalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

    // Authorization Check
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    checkIsOrganizerForFestival(auth.getName(), festival);

    // Business Logic Check
    if (festival.getState() != FestivalState.FINAL_SUBMISSION) {
        throw new InvalidOperationException("Festival must be in FINAL_SUBMISSION state to move to DECISION.");
    }

    // State Change
    festival.setState(FestivalState.DECISION);
    festivalRepository.save(festival);
}

@Transactional
public void changeStateToAnnounced(Long id) {
    Festival festival = festivalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Festival not found with id: " + id));

    // Authorization Check
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    checkIsOrganizerForFestival(auth.getName(), festival);

    // Business Logic Check
    if (festival.getState() != FestivalState.DECISION) {
        throw new InvalidOperationException("Festival must be in DECISION state to be ANNOUNCED.");
    }

    // State Change
    festival.setState(FestivalState.ANNOUNCED);
    festivalRepository.save(festival);
}


// Αρχείο: FestivalService.java

public List<FestivalO> searchFestivals(String name, String location) {
    return festivalRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location)
            .stream()
            .map(this::convertEntityToO)
            .collect(Collectors.toList());
}

}