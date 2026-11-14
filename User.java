package com.mentorbridge.models;

import java.time.LocalDateTime;

/**
 * Model representing a system user with role-based authentication.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String role; // "ADMIN", "STUDENT", "MENTOR"
    private String accountStatus; // "ACTIVE", "INACTIVE"
    private LocalDateTime createdAt;

    public User() {}

    public User(int id, String name, String email, String passwordHash, String role, String accountStatus, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
    public boolean isStudent() { return "STUDENT".equalsIgnoreCase(role); }
    public boolean isMentor() { return "MENTOR".equalsIgnoreCase(role); }
}
