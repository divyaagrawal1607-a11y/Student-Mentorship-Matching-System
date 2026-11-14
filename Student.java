package com.mentorbridge.models;

import java.time.LocalDateTime;

/**
 * Model representing a Student enrolled in mentorship matching.
 */
public class Student {
    private int id;
    private Integer userId;
    private String fullName;
    private String email;
    private String department;
    private int studyYear;
    private String skillsInterests;
    private String careerGoal;
    private String contactNumber;
    private LocalDateTime createdAt;

    public Student() {}

    public Student(int id, Integer userId, String fullName, String email, String department, 
                   int studyYear, String skillsInterests, String careerGoal, 
                   String contactNumber, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.studyYear = studyYear;
        this.skillsInterests = skillsInterests;
        this.careerGoal = careerGoal;
        this.contactNumber = contactNumber;
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

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }

    public String getSkillsInterests() { return skillsInterests; }
    public void setSkillsInterests(String skillsInterests) { this.skillsInterests = skillsInterests; }

    public String getCareerGoal() { return careerGoal; }
    public void setCareerGoal(String careerGoal) { this.careerGoal = careerGoal; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
