//package com.mycompany.festival.security;
//
//import com.mycompany.festival.security.JwtAuthenticationFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeHttpRequests()
//                .requestMatchers("/api/festivals/**").hasAnyRole("ORGANIZER", "VISITOR", "ARTIST", "STAFF")
//                .requestMatchers("/api/performances/**").hasAnyRole("ARTIST", "VISITOR", "STAFF", "ORGANIZER")
//                .anyRequest().authenticated()
//                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll()  // <-- Δεν χρειάζεται login
//)
//            .and()
//            .httpBasic();
//        return http.build();
//    }
//}



package com.mycompany.festival.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Προσθήκη αυτής της γραμμής
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import com.mycompany.festival.security.JwtAuthenticationFilter; // Κρατάμε το import αν θα το χρησιμοποιήσουμε αργότερα

@Configuration
@EnableWebSecurity // Προσθήκη αυτής της annotation
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Απενεργοποίηση CSRF
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll() // Επιτρέπει ΟΛΑ τα requests χωρίς authentication
            )
            .httpBasic(); // Μπορείς να το αφαιρέσεις αν δεν θες καθόλου basic auth
            
        // Αν θες να προσθέσεις JWT αργότερα, θα έμοιαζε κάπως έτσι:
        // .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    // Αν χρησιμοποιείς AuthenticationManagerBuilder, θα χρειαστείς και αυτό:
    // @Bean
    // public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    //     return http.getSharedObject(AuthenticationManagerBuilder.class)
    //         .build();
    // }
}
