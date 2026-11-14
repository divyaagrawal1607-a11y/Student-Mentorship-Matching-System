package com.mentorbridge.models;

import java.time.LocalDateTime;

/**
 * Model representing a Mentorship Request initiated by a Student to a Mentor.
 */
public class MentorshipRequest {
    private int id;
    private int studentId;
    private int mentorId;
    private String studentName;
    private String mentorName;
    private String studentEmail;
    private String mentorEmail;
    private String requestMessage;
    private String requestStatus; // "PENDING", "ACCEPTED", "REJECTED", "CANCELLED"
    private LocalDateTime requestTimestamp;

    public MentorshipRequest() {}

    public MentorshipRequest(int id, int studentId, int mentorId, String requestMessage, 
                             String requestStatus, LocalDateTime requestTimestamp) {
        this.id = id;
        this.studentId = studentId;
        this.mentorId = mentorId;
        this.requestMessage = requestMessage;
        this.requestStatus = requestStatus;
        this.requestTimestamp = requestTimestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getMentorId() { return mentorId; }
    public void setMentorId(int mentorId) { this.mentorId = mentorId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getMentorName() { return mentorName; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getMentorEmail() { return mentorEmail; }
    public void setMentorEmail(String mentorEmail) { this.mentorEmail = mentorEmail; }

    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }

    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }

    public LocalDateTime getRequestTimestamp() { return requestTimestamp; }
    public void setRequestTimestamp(LocalDateTime requestTimestamp) { this.requestTimestamp = requestTimestamp; }
}
