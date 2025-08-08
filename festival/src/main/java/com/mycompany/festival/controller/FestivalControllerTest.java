/*
Τεστάραμε το endpoint POST /api/festivals, που δημιουργεί νέο φεστιβάλ. 
Το service ήταν mock-αρισμένο ώστε να επιστρέφει το αναμενόμενο αντικείμενο FestivalO.
 */
package com.mycompany.festival.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.festival.enums.FestivalState;
import com.mycompany.festival.objects.FestivalO;
import com.mycompany.festival.service.FestivalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FestivalController.class)
public class FestivalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FestivalService festivalService;

    @Autowired
    private ObjectMapper objectMapper;

    //@WithMockUser(username = "organizer", roles = {"ORGANIZER"})
    @Test
    public void testCreateFestival() throws Exception {
        // Input δεδομένα
        FestivalO inputFestival = new FestivalO();
        inputFestival.setName("Jazz Nights");
        inputFestival.setDescription("Ετήσιο φεστιβάλ jazz");
        inputFestival.setLocation("Αθήνα");
        inputFestival.setStartDate(LocalDate.of(2025, 7, 10));
        inputFestival.setEndDate(LocalDate.of(2025, 7, 12));
        inputFestival.setMaxCapacity(5000);
        inputFestival.setTicketPrice(25.0);

        // Αναμενόμενο αποτέλεσμα από το service (mocked)
        FestivalO createdFestival = new FestivalO();
        createdFestival.setId(1L);
        createdFestival.setName(inputFestival.getName());
        createdFestival.setDescription(inputFestival.getDescription());
        createdFestival.setLocation(inputFestival.getLocation());
        createdFestival.setStartDate(inputFestival.getStartDate());
        createdFestival.setEndDate(inputFestival.getEndDate());
        createdFestival.setMaxCapacity(inputFestival.getMaxCapacity());
        createdFestival.setTicketPrice(inputFestival.getTicketPrice());
        createdFestival.setState(FestivalState.CREATED);
        
        



        // Mock συμπεριφορά του service
        Mockito.when(festivalService.createFestival(Mockito.any(FestivalO.class)))
               .thenReturn(createdFestival);

        // Εκτέλεση και έλεγχοι
        mockMvc.perform(post("/api/festivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputFestival)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Jazz Nights"))
        .andExpect(jsonPath("$.location").value("Αθήνα"))
        .andExpect(jsonPath("$.ticketPrice").value(25.0));
        
        
        //Εκτυπωση
                String response = mockMvc.perform(post("/api/festivals")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(inputFestival)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

System.out.println("RESPONSE BODY: " + response);

    }
}


//package com.mycompany.festival.controller;
//
//import org.junit.jupiter.api.Test;
//
//public class FestivalControllerTest {
//
//    @Test
//    public void basicTest() {
//        System.out.println("Test works");
//    }
//}
