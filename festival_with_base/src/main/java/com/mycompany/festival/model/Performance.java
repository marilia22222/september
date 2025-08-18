//package com.mycompany.festival.model;
//
//import com.mycompany.festival.enums.PerformanceState;
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class Performance {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//    private String description;
//    
//    @Column(nullable = false)
//    private Integer duration; // in minutes
//    private String genre;
//    private String technicalRequirements;
//    
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdDate;
//    
//
//    @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//    private PerformanceState state; // CREATED, SUBMITTED, etc.
//
//    @ManyToOne
//    @JoinColumn(name = "festival_id", nullable = false)
//    private Festival festival;
//
//    @ManyToMany
//    @JoinTable(name = "performance_artists", 
//               joinColumns = @JoinColumn(name = "performance_id"),
//               inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<User> artists = new ArrayList<>();
//
//    
//
//    // Getters and Setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    // Getter and Setter for 'name'
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    // Getter and Setter for 'description'
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    // Getter and Setter for 'duration'
//    public Integer getDuration() {
//        return duration;
//    }
//
//    public void setDuration(Integer duration) {
//        this.duration = duration;
//    }
//
//    // Getter and Setter for 'genre'
//    public String getGenre() {
//        return genre;
//    }
//
//    public void setGenre(String genre) {
//        this.genre = genre;
//    }
//
//    // Getter and Setter for 'technicalRequirements'
//    public String getTechnicalRequirements() {
//        return technicalRequirements;
//    }
//
//    public void setTechnicalRequirements(String technicalRequirements) {
//        this.technicalRequirements = technicalRequirements;
//    }
//
//    // Getter and Setter for 'createdDate'
//    public LocalDateTime getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(LocalDateTime createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public void setFestival(Festival festival) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public Festival getFestival() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public Object getArtistName() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//}
package com.mycompany.festival.model;

import com.mycompany.festival.enums.PerformanceState;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    
    @Column(nullable = false)
    private Integer duration; // Διάρκεια σε λεπτά

    private String genre;
    private String technicalRequirements;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerformanceState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", nullable = false)
    private User creator;

    @ManyToMany
    @JoinTable(name = "performance_artists", 
               joinColumns = @JoinColumn(name = "performance_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> artists = new ArrayList<>();

    // --- Constructor ---
    public Performance() {
        this.createdDate = LocalDateTime.now();
        this.state = PerformanceState.CREATED;
    }
    
    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTechnicalRequirements() {
        return technicalRequirements;
    }

    public void setTechnicalRequirements(String technicalRequirements) {
        this.technicalRequirements = technicalRequirements;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public PerformanceState getState() {
        return state;
    }

    public void setState(PerformanceState state) {
        this.state = state;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getArtists() {
        return artists;
    }

    public void setArtists(List<User> artists) {
        this.artists = artists;
    }
}