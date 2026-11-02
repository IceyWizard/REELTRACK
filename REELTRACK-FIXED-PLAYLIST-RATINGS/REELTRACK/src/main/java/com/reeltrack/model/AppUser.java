package com.reeltrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String passwordHash;

    public AppUser() {}
    public AppUser(String username, String email, String passwordHash) {
        this.username = username; this.email = email; this.passwordHash = passwordHash;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
