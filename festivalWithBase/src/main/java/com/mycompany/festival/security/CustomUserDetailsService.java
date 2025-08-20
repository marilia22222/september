package com.mycompany.festival.security;

import com.mycompany.festival.model.User;
import com.mycompany.festival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        // Assign a general 'USER' role to all authenticated users for basic access
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 

        // Context-specific roles (ORGANIZER, ARTIST, STAFF for specific festivals)
        // are NOT loaded here as SimpleGrantedAuthority. They are checked
        // within service layer methods or using custom Spring Security expressions.

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}