package com.mycompany.festival.service;

import com.mycompany.festival.enums.FestivalState;
import com.mycompany.festival.enums.RoleName;
import com.mycompany.festival.exception.ConflictException;
import com.mycompany.festival.exception.ResourceNotFoundException;
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


import com.mycompany.festival.model.Festival;
import com.mycompany.festival.objects.FestivalO;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class FestivalService {
    
     @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFestivalRoleRepository userFestivalRoleRepository;
    
    private List<FestivalO> festivals = new ArrayList<>();
    private long nextId = 1;

    public Festival convertFestivalOToEntity(FestivalO festivalO) {
        Festival festival = new Festival();
        festival.setName(festivalO.getName());
        festival.setDescription(festivalO.getDescription());
        festival.setStartDate(festivalO.getStartDate());
        festival.setEndDate(festivalO.getEndDate());
        festival.setState(festivalO.getState());
        return festival;
    }
    
    @Transactional
    public Festival createFestival(FestivalO festivalO, String creatorUsername) {
//        if (festivalRepository.findByName(festivalO.getName()).isPresent()) {
//            throw new ConflictException("Festival with name " + festivalO.getName() + " already exists.");
//        }

        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found."));

        Festival festival = new Festival();
        festival.setName(festivalO.getName());
        festival.setDescription(festivalO.getDescription());
        festival.setLocation(festivalO.getLocation());
        festival.setStartDate(festivalO.getStartDate());
        festival.setEndDate(festivalO.getEndDate());
        festival.setMaxCapacity(festivalO.getMaxCapacity());
        festival.setTicketPrice(festivalO.getTicketPrice());
        // Do not set organizer directly, roles are handled by UserFestivalRole

        festival = festivalRepository.save(festival); // Save festival first to get its ID

        // Assign ORGANIZER role to the creator for this specific festival
        UserFestivalRole organizerRole = new UserFestivalRole(creator, festival, RoleName.ORGANIZER);
        userFestivalRoleRepository.save(organizerRole);

        return festival;
    }

    @Transactional
    public void addOrganizerToFestival(Long festivalId, Long userId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new ResourceNotFoundException("Festival not found with ID: " + festivalId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Check if user already has ORGANIZER role for this festival
        if (userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(user, festival, RoleName.ORGANIZER)) {
            throw new ConflictException("User is already an ORGANIZER for this festival.");
        }

        UserFestivalRole organizerRole = new UserFestivalRole(user, festival, RoleName.ORGANIZER);
        userFestivalRoleRepository.save(organizerRole);
    }

    @Transactional
    public void addStaffToFestival(Long festivalId, Long userId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new ResourceNotFoundException("Festival not found with ID: " + festivalId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Check if user already has STAFF role for this festival
        if (userFestivalRoleRepository.existsByUserAndFestivalAndRoleType(user, festival, RoleName.STAFF)) {
            throw new ConflictException("User is already STAFF for this festival.");
        }

        UserFestivalRole staffRole = new UserFestivalRole(user, festival, RoleName.STAFF);
        userFestivalRoleRepository.save(staffRole);
    }
    
     public List<FestivalO> getAllFestivals() {
        return festivals;
    }

     public FestivalO getFestivalById(Long id) {
        return festivals.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
     
     
     

    public FestivalO createFestival(FestivalO festivalO) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        festivalO.setId(nextId++);
        festivals.add(festivalO);
        return festivalO;
    
    }

    public FestivalO updateFestival(Long id, FestivalO updatedFestival) {
        FestivalO existing = getFestivalById(id);
        if (existing != null) {
            existing.setName(updatedFestival.getName());
            existing.setDescription(updatedFestival.getDescription());
            existing.setLocation(updatedFestival.getLocation());
            existing.setStartDate(updatedFestival.getStartDate());
            existing.setEndDate(updatedFestival.getEndDate());
            existing.setState(updatedFestival.getState());
            existing.setMaxCapacity(updatedFestival.getMaxCapacity());
            existing.setTicketPrice(updatedFestival.getTicketPrice());
        }
        return existing;
    }
    
    public boolean deleteFestival(Long id) {
        return festivals.removeIf(f -> f.getId().equals(id));
    } 
    
    public void startSubmission(Long id) {
    FestivalO festival = getFestivalById(id);
    if (festival != null) {
        festival.setState(FestivalState.SUBMISSION);
    }
}
    
    
    
}
