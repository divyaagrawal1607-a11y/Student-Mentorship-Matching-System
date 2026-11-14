package com.mentorbridge.dao;

import com.mentorbridge.database.ConnectionManager;
import com.mentorbridge.models.MentorshipSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Mentorship Meeting Sessions.
 */
public class MentorshipSessionDAO {

    public int create(MentorshipSession s) throws SQLException {
        String sql = "INSERT INTO mentorship_sessions (request_id, session_datetime, meeting_mode, meeting_link_location, session_status, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getRequestId());
            ps.setTimestamp(2, Timestamp.valueOf(s.getSessionDateTime()));
            ps.setString(3, s.getMeetingMode() != null ? s.getMeetingMode() : "ONLINE");
            ps.setString(4, s.getMeetingLinkLocation());
            ps.setString(5, s.getSessionStatus() != null ? s.getSessionStatus() : "SCHEDULED");
            ps.setString(6, s.getNotes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    s.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public boolean updateStatus(int sessionId, String status) throws SQLException {
        String sql = "UPDATE mentorship_sessions SET session_status = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, sessionId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(MentorshipSession s) throws SQLException {
        String sql = "UPDATE mentorship_sessions SET session_datetime = ?, meeting_mode = ?, meeting_link_location = ?, " +
                     "session_status = ?, notes = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(s.getSessionDateTime()));
            ps.setString(2, s.getMeetingMode());
            ps.setString(3, s.getMeetingLinkLocation());
            ps.setString(4, s.getSessionStatus());
            ps.setString(5, s.getNotes());
            ps.setInt(6, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public MentorshipSession findById(int id) {
        String sql = "SELECT s.id, s.request_id, s.session_datetime, s.meeting_mode, s.meeting_link_location, " +
                     "s.session_status, s.notes, s.created_at, " +
                     "st.full_name AS student_name, st.id AS student_id, " +
                     "m.full_name AS mentor_name, m.id AS mentor_id, " +
                     "(SELECT COUNT(*) FROM feedback f WHERE f.session_id = s.id) > 0 AS feedback_submitted " +
                     "FROM mentorship_sessions s " +
                     "JOIN mentorship_requests r ON s.request_id = r.id " +
                     "JOIN students st ON r.student_id = st.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "WHERE s.id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapSession(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MentorshipSession> findAll() {
        List<MentorshipSession> list = new ArrayList<>();
        String sql = "SELECT s.id, s.request_id, s.session_datetime, s.meeting_mode, s.meeting_link_location, " +
                     "s.session_status, s.notes, s.created_at, " +
                     "st.full_name AS student_name, st.id AS student_id, " +
                     "m.full_name AS mentor_name, m.id AS mentor_id, " +
                     "(SELECT COUNT(*) FROM feedback f WHERE f.session_id = s.id) > 0 AS feedback_submitted " +
                     "FROM mentorship_sessions s " +
                     "JOIN mentorship_requests r ON s.request_id = r.id " +
                     "JOIN students st ON r.student_id = st.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "ORDER BY s.session_datetime DESC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapSession(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MentorshipSession> findByStudentId(int studentId) {
        List<MentorshipSession> list = new ArrayList<>();
        String sql = "SELECT s.id, s.request_id, s.session_datetime, s.meeting_mode, s.meeting_link_location, " +
                     "s.session_status, s.notes, s.created_at, " +
                     "st.full_name AS student_name, st.id AS student_id, " +
                     "m.full_name AS mentor_name, m.id AS mentor_id, " +
                     "(SELECT COUNT(*) FROM feedback f WHERE f.session_id = s.id) > 0 AS feedback_submitted " +
                     "FROM mentorship_sessions s " +
                     "JOIN mentorship_requests r ON s.request_id = r.id " +
                     "JOIN students st ON r.student_id = st.id " +
                     "JOIN mentors m ON r.mentor_id = m.id " +
                     "WHERE st.id = ? ORDER BY s.session_datetime DESC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSession(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM mentorship_sessions WHERE session_status = ?";
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

    private MentorshipSession mapSession(ResultSet rs) throws SQLException {
        MentorshipSession s = new MentorshipSession();
        s.setId(rs.getInt("id"));
        s.setRequestId(rs.getInt("request_id"));
        s.setStudentName(rs.getString("student_name"));
        s.setMentorName(rs.getString("mentor_name"));
        s.setStudentId(rs.getInt("student_id"));
        s.setMentorId(rs.getInt("mentor_id"));
        Timestamp dt = rs.getTimestamp("session_datetime");
        if (dt != null) s.setSessionDateTime(dt.toLocalDateTime());
        s.setMeetingMode(rs.getString("meeting_mode"));
        s.setMeetingLinkLocation(rs.getString("meeting_link_location"));
        s.setSessionStatus(rs.getString("session_status"));
        s.setNotes(rs.getString("notes"));
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) s.setCreatedAt(ca.toLocalDateTime());
        s.setFeedbackSubmitted(rs.getBoolean("feedback_submitted"));
        return s;
    }
}
