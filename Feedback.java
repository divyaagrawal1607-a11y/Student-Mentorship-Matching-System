package com.mentorbridge.models;

import java.time.LocalDateTime;

/**
 * Model representing Student Feedback and Rating for a completed session.
 */
public class Feedback {
    private int id;
    private int sessionId;
    private int studentId;
    private int mentorId;
    private String studentName;
    private String mentorName;
    private int rating; // 1 to 5
    private String comments;
    private LocalDateTime createdAt;

    public Feedback() {}

    public Feedback(int id, int sessionId, int studentId, int mentorId, int rating, String comments, LocalDateTime createdAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.mentorId = mentorId;
        this.rating = rating;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getMentorId() { return mentorId; }
    public void setMentorId(int mentorId) { this.mentorId = mentorId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getMentorName() { return mentorName; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
