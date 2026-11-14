package com.mentorbridge.dao;

import com.mentorbridge.database.ConnectionManager;
import com.mentorbridge.models.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Feedback entries.
 */
public class FeedbackDAO {

    public int create(Feedback f) throws SQLException {
        if (f.getRating() < 1 || f.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        String sql = "INSERT INTO feedback (session_id, student_id, mentor_id, rating, comments) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, f.getSessionId());
            ps.setInt(2, f.getStudentId());
            ps.setInt(3, f.getMentorId());
            ps.setInt(4, f.getRating());
            ps.setString(5, f.getComments());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    f.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public boolean hasFeedback(int sessionId) {
        String sql = "SELECT COUNT(*) FROM feedback WHERE session_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Feedback> findAll() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.id, f.session_id, f.student_id, f.mentor_id, f.rating, f.comments, f.created_at, " +
                     "s.full_name AS student_name, m.full_name AS mentor_name " +
                     "FROM feedback f " +
                     "JOIN students s ON f.student_id = s.id " +
                     "JOIN mentors m ON f.mentor_id = m.id " +
                     "ORDER BY f.id DESC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapFeedback(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Feedback> findByMentorId(int mentorId) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.id, f.session_id, f.student_id, f.mentor_id, f.rating, f.comments, f.created_at, " +
                     "s.full_name AS student_name, m.full_name AS mentor_name " +
                     "FROM feedback f " +
                     "JOIN students s ON f.student_id = s.id " +
                     "JOIN mentors m ON f.mentor_id = m.id " +
                     "WHERE f.mentor_id = ? ORDER BY f.id DESC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mentorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapFeedback(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getAverageRatingForMentor(int mentorId) {
        String sql = "SELECT AVG(rating) FROM feedback WHERE mentor_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mentorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Feedback mapFeedback(ResultSet rs) throws SQLException {
        Feedback f = new Feedback();
        f.setId(rs.getInt("id"));
        f.setSessionId(rs.getInt("session_id"));
        f.setStudentId(rs.getInt("student_id"));
        f.setMentorId(rs.getInt("mentor_id"));
        f.setStudentName(rs.getString("student_name"));
        f.setMentorName(rs.getString("mentor_name"));
        f.setRating(rs.getInt("rating"));
        f.setComments(rs.getString("comments"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) f.setCreatedAt(ts.toLocalDateTime());
        return f;
    }
}
