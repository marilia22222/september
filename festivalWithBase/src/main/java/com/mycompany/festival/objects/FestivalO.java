package com.mycompany.festival.objects;

import com.mycompany.festival.enums.FestivalState;
import com.mycompany.festival.enums.FestivalState;


import java.time.LocalDate;

public class FestivalO {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private FestivalState state;

     
    private String location; 

    
    private Integer maxCapacity; 

    private Double ticketPrice; 
    
    
    
    
    
    // Getters and Setters
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
    public Long getId() {
        return id;
    }

    public void setState(FestivalState state) {
        this.state = state;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

//    public void setId(Long id) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
    

    //  ΠΡΟΣΘΗΚΗ: Getters και Setters για τα νέα πεδία 
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    public Double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(Double ticketPrice) { this.ticketPrice = ticketPrice; }
}


