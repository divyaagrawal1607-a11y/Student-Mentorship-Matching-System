package com.mentorbridge.services;

import com.mentorbridge.dao.MentorshipRequestDAO;
import com.mentorbridge.models.MentorshipRequest;
import com.mentorbridge.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Service for managing mentorship requests.
 */
public class RequestService {

    private final MentorshipRequestDAO requestDAO = new MentorshipRequestDAO();

    public List<MentorshipRequest> getAllRequests() {
        return requestDAO.findAll();
    }

    public List<MentorshipRequest> getRequestsForStudent(int studentId) {
        return requestDAO.findByStudentId(studentId);
    }

    public List<MentorshipRequest> getRequestsForMentor(int mentorId) {
        return requestDAO.findByMentorId(mentorId);
    }

    public MentorshipRequest getRequestById(int id) {
        return requestDAO.findById(id);
    }

    public boolean createRequest(int studentId, int mentorId, String message) throws SQLException {
        if (!ValidationUtil.isNotEmpty(message)) {
            message = "I would appreciate your mentorship and guidance.";
        }
        MentorshipRequest req = new MentorshipRequest();
        req.setStudentId(studentId);
        req.setMentorId(mentorId);
        req.setRequestMessage(message);
        req.setRequestStatus("PENDING");
        return requestDAO.create(req) > 0;
    }

    public boolean acceptRequest(int requestId) throws SQLException {
        return requestDAO.updateStatus(requestId, "ACCEPTED");
    }

    public boolean rejectRequest(int requestId) throws SQLException {
        return requestDAO.updateStatus(requestId, "REJECTED");
    }

    public boolean cancelRequest(int requestId) throws SQLException {
        return requestDAO.updateStatus(requestId, "CANCELLED");
    }

    public int getPendingCount() {
        return requestDAO.countByStatus("PENDING");
    }

    public int getAcceptedCount() {
        return requestDAO.countByStatus("ACCEPTED");
    }
}
