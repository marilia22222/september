
package com.mycompany.festival;

//import com.mycompany.festival.repository.UserRepository;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.mycompany.festival.model.User;
import com.mycompany.festival.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class Festival {
    public static void main(String[] args) {
        SpringApplication.run(Festival.class, args);
    }

 @Bean
    CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Έλεγχος αν ο χρήστης υπάρχει ήδη για να μην τον ξαναδημιουργεί κάθε φορά
            if (userRepository.findByUsername("organizerUser").isEmpty()) {
                User user = new User();
                user.setUsername("organizerUser");
                user.setEmail("organizer@test.com");
                
                // ΠΟΤΕ μην αποθηκεύεις τον κωδικό ως απλό κείμενο! Πάντα κρυπτογραφημένο.
                user.setPassword(passwordEncoder.encode("password"));

                userRepository.save(user);
                System.out.println(">>> Created default user 'organizerUser' with password 'password'");
            }
            
            if (userRepository.findByUsername("artistUser").isEmpty()) {
                User artist = new User();
                artist.setUsername("artistUser");
                artist.setEmail("artist@test.com");
                artist.setPassword(passwordEncoder.encode("password"));
                userRepository.save(artist);
                System.out.println(">>> Created default user 'artistUser'");
            }
            
            // Δημιουργία Χρήστη 3: Ένας άλλος χρήστης που δεν είναι τίποτα ακόμα
            if (userRepository.findByUsername("newUser").isEmpty()) {
                User newUser = new User();
                newUser.setUsername("newUser");
                newUser.setEmail("new@test.com");
                newUser.setPassword(passwordEncoder.encode("password"));
                userRepository.save(newUser);
                System.out.println(">>> Created default user 'newUser'");
            }
        };
    }
    
    
}