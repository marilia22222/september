package com.mycompany.festival.objects;

import com.mycompany.festival.enums.FestivalState;
import com.mycompany.festival.enums.FestivalState;


import java.time.LocalDate;

public class FestivalO {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private FestivalState state;

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

    public void setState(FestivalState state) {
        this.state = state;
    }

    public void setId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
