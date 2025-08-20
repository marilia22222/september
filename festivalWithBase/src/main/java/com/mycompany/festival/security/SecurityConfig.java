package com.mycompany.festival.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
             .requestMatchers(HttpMethod.GET, "/api/festivals", "/api/festivals/**").permitAll()       
                //.anyRequest().permitAll() // Επιτρέπει ΟΛΑ τα requests χωρίς authentication
                    .anyRequest().authenticated()
            )
            .httpBasic(); 
        return http.build();
    }

  
}
