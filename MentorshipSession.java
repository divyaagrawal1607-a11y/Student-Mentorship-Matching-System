package com.mentorbridge.models;

import java.time.LocalDateTime;

/**
 * Model representing a Scheduled Mentorship Meeting Session.
 */
public class MentorshipSession {
    private int id;
    private int requestId;
    private String studentName;
    private String mentorName;
    private int studentId;
    private int mentorId;
    private LocalDateTime sessionDateTime;
    private String meetingMode; // "ONLINE", "IN_PERSON", "HYBRID"
    private String meetingLinkLocation;
    private String sessionStatus; // "SCHEDULED", "COMPLETED", "CANCELLED"
    private String notes;
    private LocalDateTime createdAt;
    private boolean feedbackSubmitted;

    public MentorshipSession() {}

    public MentorshipSession(int id, int requestId, LocalDateTime sessionDateTime, 
                             String meetingMode, String meetingLinkLocation, 
                             String sessionStatus, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.requestId = requestId;
        this.sessionDateTime = sessionDateTime;
        this.meetingMode = meetingMode;
        this.meetingLinkLocation = meetingLinkLocation;
        this.sessionStatus = sessionStatus;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getMentorName() { return mentorName; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getMentorId() { return mentorId; }
    public void setMentorId(int mentorId) { this.mentorId = mentorId; }

    public LocalDateTime getSessionDateTime() { return sessionDateTime; }
    public void setSessionDateTime(LocalDateTime sessionDateTime) { this.sessionDateTime = sessionDateTime; }

    public String getMeetingMode() { return meetingMode; }
    public void setMeetingMode(String meetingMode) { this.meetingMode = meetingMode; }

    public String getMeetingLinkLocation() { return meetingLinkLocation; }
    public void setMeetingLinkLocation(String meetingLinkLocation) { this.meetingLinkLocation = meetingLinkLocation; }

    public String getSessionStatus() { return sessionStatus; }
    public void setSessionStatus(String sessionStatus) { this.sessionStatus = sessionStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isFeedbackSubmitted() { return feedbackSubmitted; }
    public void setFeedbackSubmitted(boolean feedbackSubmitted) { this.feedbackSubmitted = feedbackSubmitted; }
}
