package com.mycompany.festival.service;

import com.mycompany.festival.model.Festival;
import com.mycompany.festival.objects.FestivalO;
import org.springframework.stereotype.Service;

@Service
public class FestivalService {

    public Festival convertFestivalOToEntity(FestivalO festivalO) {
        Festival festival = new Festival();
        festival.setName(festivalO.getName());
        festival.setDescription(festivalO.getDescription());
        festival.setStartDate(festivalO.getStartDate());
        festival.setEndDate(festivalO.getEndDate());
        festival.setState(festivalO.getState());
        return festival;
    }

    public FestivalO createFestival(FestivalO festivalO) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void startSubmission(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
