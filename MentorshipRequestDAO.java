package com.mentorbridge.dao;

import com.mentorbridge.database.ConnectionManager;
import com.mentorbridge.models.MentorshipRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Mentorship Requests.
 */
public class MentorshipRequestDAO {

    public int create(MentorshipRequest r) throws SQLException {
        String sql = "INSERT INTO mentorship_requests (student_id, mentor_id, request_message, request_status) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getStudentId());
            ps.setInt(2, r.getMentorId());
            ps.setString(3, r.getRequestMessage());
            ps.setString(4, r.getRequestStatus() != null ? r.getRequestStatus() : "PENDING");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    r.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public boolean updateStatus(int requestId, String newStatus) throws SQLException {
        // Enforce transition rules
        MentorshipRequest req = findById(requestId);
        if (req == null) return false;

        String currentStatus = req.getRequestStatus();
        if ("CANCELLED".equals(currentStatus) || "REJECTED".equals(currentStatus)) {
            throw new IllegalStateException("Cannot change status of a request that is already " + currentStatus);
        }

        String sql = "UPDATE mentorship_requests SET request_status = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        }
    }

    public MentorshipRequest findById(int id) {
        String sql = "SELECT r.id, r.student_id, r.mentor_id, r.request_message, r.request_status, r.request_timestamp, " +
                     "s.full_name AS student_name, s.email AS student_email, " +
                     "m.full_name AS mentor_name, m.email AS mentor_email " +
                     "FROM mentorship_requests r " +
                     "JOIN students s ON r.student_id = s.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "WHERE r.id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRequest(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MentorshipRequest> findAll() {
        List<MentorshipRequest> list = new ArrayList<>();
        String sql = "SELECT r.id, r.student_id, r.mentor_id, r.request_message, r.request_status, r.request_timestamp, " +
                     "s.full_name AS student_name, s.email AS student_email, " +
                     "m.full_name AS mentor_name, m.email AS mentor_email " +
                     "FROM mentorship_requests r " +
                     "JOIN students s ON r.student_id = s.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "ORDER BY r.id DESC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MentorshipRequest> findByStudentId(int studentId) {
        List<MentorshipRequest> list = new ArrayList<>();
        String sql = "SELECT r.id, r.student_id, r.mentor_id, r.request_message, r.request_status, r.request_timestamp, " +
                     "s.full_name AS student_name, s.email AS student_email, " +
                     "m.full_name AS mentor_name, m.email AS mentor_email " +
                     "FROM mentorship_requests r " +
                     "JOIN students s ON r.student_id = s.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "WHERE r.student_id = ? ORDER BY r.id DESC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MentorshipRequest> findByMentorId(int mentorId) {
        List<MentorshipRequest> list = new ArrayList<>();
        String sql = "SELECT r.id, r.student_id, r.mentor_id, r.request_message, r.request_status, r.request_timestamp, " +
                     "s.full_name AS student_name, s.email AS student_email, " +
                     "m.full_name AS mentor_name, m.email AS mentor_email " +
                     "FROM mentorship_requests r " +
                     "JOIN students s ON r.student_id = s.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "WHERE r.mentor_id = ? ORDER BY r.id DESC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mentorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM mentorship_requests WHERE request_status = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private MentorshipRequest mapRequest(ResultSet rs) throws SQLException {
        MentorshipRequest r = new MentorshipRequest();
        r.setId(rs.getInt("id"));
        r.setStudentId(rs.getInt("student_id"));
        r.setMentorId(rs.getInt("mentor_id"));
        r.setStudentName(rs.getString("student_name"));
        r.setStudentEmail(rs.getString("student_email"));
        r.setMentorName(rs.getString("mentor_name"));
        r.setMentorEmail(rs.getString("mentor_email"));
        r.setRequestMessage(rs.getString("request_message"));
        r.setRequestStatus(rs.getString("request_status"));
        Timestamp ts = rs.getTimestamp("request_timestamp");
        if (ts != null) r.setRequestTimestamp(ts.toLocalDateTime());
        return r;
    }
}
