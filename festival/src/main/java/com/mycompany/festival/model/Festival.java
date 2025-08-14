//package com.mycompany.festival.model;
//import com.mycompany.festival.enums.FestivalState;
//import jakarta.persistence.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class Festival {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String name;
//
//    @Column(nullable = false)
//    private String description;
//
//    @Column(nullable = false)
//    private LocalDate startDate;
//
//    @Column(nullable = false)
//    private LocalDate endDate;
//
//    @Enumerated(EnumType.STRING)
//    private FestivalState state; // Enum για την κατάσταση του festival (π.χ. CREATED, SUBMISSION, REVIEW)
//
//    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL)
//    private List<Performance> performances = new ArrayList<>();
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }
//
//    public FestivalState getState() {
//        return state;
//    }
//
//    public void setState(FestivalState state) {
//        this.state = state;
//    }
//
//    public List<Performance> getPerformances() {
//        return performances;
//    }
//
//    public void setPerformances(List<Performance> performances) {
//        this.performances = performances;
//    }
//}


package com.mycompany.festival.model;

import com.mycompany.festival.enums.FestivalState;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
//    @NotBlank(message = "Location is mandatory") // <-- ΠΡΟΣΘΗΚΗ
    private String location; // <-- ΠΡΟΣΘΗΚΗ


  //  @Positive(message = "Max capacity must be positive")
    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "ticket_price")
    private Double ticketPrice; 

    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    private FestivalState state;
    
    // *** ΠΡΟΣΘΗΚΗ: Reference στον User που είναι organizer ***
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
    
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL)
    private List<Performance> performances = new ArrayList<>();
    
    // Getters and Setters
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
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public FestivalState getState() {
        return state;
    }
    
    public void setState(FestivalState state) {
        this.state = state;
    }
    
    // *** ΠΡΟΣΘΗΚΗ: Getter και Setter για organizer ***
    public User getOrganizer() {
        return organizer;
    }
    
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
    
    public List<Performance> getPerformances() {
        return performances;
    }
    
    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    
    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    
    
}