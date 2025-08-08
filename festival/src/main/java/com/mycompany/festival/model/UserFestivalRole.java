package com.mycompany.festival.model;

import com.mycompany.festival.enums.RoleName; // Import το υπάρχον σου enum
import jakarta.persistence.*;

@Entity
@Table(name = "user_festival_roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "festival_id", "role_type"})
})
public class UserFestivalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    // Εδώ χρησιμοποιούμε το δικό σου RoleName
    private RoleName roleType; 

    public UserFestivalRole() {}

    public UserFestivalRole(User user, Festival festival, RoleName roleType) { // Αλλαγή εδώ
        this.user = user;
        this.festival = festival;
        this.roleType = roleType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Festival getFestival() { return festival; }
    public void setFestival(Festival festival) { this.festival = festival; }
    public RoleName getRoleType() { return roleType; } // Αλλαγή εδώ
    public void setRoleType(RoleName roleType) { this.roleType = roleType; } // Αλλαγή εδώ
}