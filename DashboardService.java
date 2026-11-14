package com.mentorbridge.services;

import com.mentorbridge.dao.MentorshipRequestDAO;
import com.mentorbridge.dao.MentorshipSessionDAO;
import com.mentorbridge.dao.MentorDAO;
import com.mentorbridge.dao.StudentDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to aggregate live statistics for the Dashboard.
 */
public class DashboardService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final MentorDAO mentorDAO = new MentorDAO();
    private final MentorshipRequestDAO requestDAO = new MentorshipRequestDAO();
    private final MentorshipSessionDAO sessionDAO = new MentorshipSessionDAO();

    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalStudents", studentDAO.countTotal());
        stats.put("totalMentors", mentorDAO.countTotal());
        stats.put("pendingRequests", requestDAO.countByStatus("PENDING"));
        stats.put("acceptedRequests", requestDAO.countByStatus("ACCEPTED"));
        stats.put("completedSessions", sessionDAO.countByStatus("COMPLETED"));
        return stats;
    }
}
