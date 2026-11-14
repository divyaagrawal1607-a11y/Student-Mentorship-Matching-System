package com.mentorbridge.dao;

import com.mentorbridge.database.ConnectionManager;
import com.mentorbridge.models.Mentor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Mentor records.
 */
public class MentorDAO {

    public int create(Mentor m) throws SQLException {
        String sql = "INSERT INTO mentors (user_id, full_name, email, expertise_skills, experience_years, availability, biography) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (m.getUserId() != null && m.getUserId() > 0) {
                ps.setInt(1, m.getUserId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, m.getFullName());
            ps.setString(3, m.getEmail().trim().toLowerCase());
            ps.setString(4, m.getExpertiseSkills());
            ps.setInt(5, m.getExperienceYears());
            ps.setString(6, m.getAvailability());
            ps.setString(7, m.getBiography());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    m.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public boolean update(Mentor m) throws SQLException {
        String sql = "UPDATE mentors SET full_name = ?, email = ?, expertise_skills = ?, experience_years = ?, " +
                     "availability = ?, biography = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getFullName());
            ps.setString(2, m.getEmail().trim().toLowerCase());
            ps.setString(3, m.getExpertiseSkills());
            ps.setInt(4, m.getExperienceYears());
            ps.setString(5, m.getAvailability());
            ps.setString(6, m.getBiography());
            ps.setInt(7, m.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int mentorId) throws SQLException {
        String sql = "DELETE FROM mentors WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mentorId);
            return ps.executeUpdate() > 0;
        }
    }

    public Mentor findById(int id) {
        String sql = "SELECT id, user_id, full_name, email, expertise_skills, experience_years, availability, biography, created_at " +
                     "FROM mentors WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMentor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Mentor findByUserId(int userId) {
        String sql = "SELECT id, user_id, full_name, email, expertise_skills, experience_years, availability, biography, created_at " +
                     "FROM mentors WHERE user_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMentor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Mentor findByEmail(String email) {
        String sql = "SELECT id, user_id, full_name, email, expertise_skills, experience_years, availability, biography, created_at " +
                     "FROM mentors WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMentor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Mentor> findAll() {
        List<Mentor> list = new ArrayList<>();
        String sql = "SELECT id, user_id, full_name, email, expertise_skills, experience_years, availability, biography, created_at " +
                     "FROM mentors ORDER BY id ASC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapMentor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Mentor> search(String query) {
        List<Mentor> list = new ArrayList<>();
        String pattern = "%" + (query == null ? "" : query.trim().toLowerCase()) + "%";
        String sql = "SELECT id, user_id, full_name, email, expertise_skills, experience_years, availability, biography, created_at " +
                     "FROM mentors WHERE LOWER(full_name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(expertise_skills) LIKE ? OR LOWER(biography) LIKE ? " +
                     "ORDER BY id ASC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMentor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM mentors";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Mentor mapMentor(ResultSet rs) throws SQLException {
        Mentor m = new Mentor();
        m.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id");
        if (!rs.wasNull()) {
            m.setUserId(uid);
        }
        m.setFullName(rs.getString("full_name"));
        m.setEmail(rs.getString("email"));
        m.setExpertiseSkills(rs.getString("expertise_skills"));
        m.setExperienceYears(rs.getInt("experience_years"));
        m.setAvailability(rs.getString("availability"));
        m.setBiography(rs.getString("biography"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setCreatedAt(ts.toLocalDateTime());
        return m;
    }
}
