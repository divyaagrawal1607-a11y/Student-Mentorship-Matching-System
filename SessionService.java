package com.mentorbridge.services;

import com.mentorbridge.dao.MentorshipSessionDAO;
import com.mentorbridge.models.MentorshipSession;
import com.mentorbridge.utils.ValidationUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for scheduling and tracking mentorship sessions.
 */
public class SessionService {

    private final MentorshipSessionDAO sessionDAO = new MentorshipSessionDAO();

    public List<MentorshipSession> getAllSessions() {
        return sessionDAO.findAll();
    }

    public List<MentorshipSession> getSessionsForStudent(int studentId) {
        return sessionDAO.findByStudentId(studentId);
    }

    public MentorshipSession getSessionById(int id) {
        return sessionDAO.findById(id);
    }

    public boolean scheduleSession(int requestId, LocalDateTime dateTime, String mode,
                                   String linkOrLocation, String notes) throws Exception {
        if (requestId <= 0) throw new IllegalArgumentException("Valid request must be selected.");
        if (dateTime == null) throw new IllegalArgumentException("Session date and time cannot be empty.");
        if (dateTime.isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new IllegalArgumentException("Session cannot be scheduled in the past.");
        }
        if (!ValidationUtil.isNotEmpty(linkOrLocation)) {
            throw new IllegalArgumentException("Meeting link or physical location is required.");
        }

        MentorshipSession session = new MentorshipSession();
        session.setRequestId(requestId);
        session.setSessionDateTime(dateTime);
        session.setMeetingMode(mode != null ? mode : "ONLINE");
        session.setMeetingLinkLocation(linkOrLocation.trim());
        session.setSessionStatus("SCHEDULED");
        session.setNotes(notes);

        return sessionDAO.create(session) > 0;
    }

    public boolean completeSession(int sessionId) throws SQLException {
        return sessionDAO.updateStatus(sessionId, "COMPLETED");
    }

    public boolean cancelSession(int sessionId) throws SQLException {
        return sessionDAO.updateStatus(sessionId, "CANCELLED");
    }

    public int getCompletedCount() {
        return sessionDAO.countByStatus("COMPLETED");
    }
}
