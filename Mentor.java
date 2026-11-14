package com.mentorbridge.models;

import java.time.LocalDateTime;

/**
 * Model representing a Mentor available for guidance.
 */
public class Mentor {
    private int id;
    private Integer userId;
    private String fullName;
    private String email;
    private String expertiseSkills;
    private int experienceYears;
    private String availability;
    private String biography;
    private LocalDateTime createdAt;

    public Mentor() {}

    public Mentor(int id, Integer userId, String fullName, String email, String expertiseSkills, 
                  int experienceYears, String availability, String biography, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.expertiseSkills = expertiseSkills;
        this.experienceYears = experienceYears;
        this.availability = availability;
        this.biography = biography;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExpertiseSkills() { return expertiseSkills; }
    public void setExpertiseSkills(String expertiseSkills) { this.expertiseSkills = expertiseSkills; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
