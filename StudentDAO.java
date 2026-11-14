package com.mentorbridge.dao;

import com.mentorbridge.database.ConnectionManager;
import com.mentorbridge.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student records.
 */
public class StudentDAO {

    public int create(Student s) throws SQLException {
        String sql = "INSERT INTO students (user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (s.getUserId() != null && s.getUserId() > 0) {
                ps.setInt(1, s.getUserId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, s.getFullName());
            ps.setString(3, s.getEmail().trim().toLowerCase());
            ps.setString(4, s.getDepartment());
            ps.setInt(5, s.getStudyYear());
            ps.setString(6, s.getSkillsInterests());
            ps.setString(7, s.getCareerGoal());
            ps.setString(8, s.getContactNumber());
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

    public boolean update(Student s) throws SQLException {
        String sql = "UPDATE students SET full_name = ?, email = ?, department = ?, study_year = ?, " +
                     "skills_interests = ?, career_goal = ?, contact_number = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getEmail().trim().toLowerCase());
            ps.setString(3, s.getDepartment());
            ps.setInt(4, s.getStudyYear());
            ps.setString(5, s.getSkillsInterests());
            ps.setString(6, s.getCareerGoal());
            ps.setString(7, s.getContactNumber());
            ps.setInt(8, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        }
    }

    public Student findById(int id) {
        String sql = "SELECT id, user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number, created_at " +
                     "FROM students WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student findByUserId(int userId) {
        String sql = "SELECT id, user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number, created_at " +
                     "FROM students WHERE user_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student findByEmail(String email) {
        String sql = "SELECT id, user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number, created_at " +
                     "FROM students WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number, created_at " +
                     "FROM students ORDER BY id ASC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> search(String query) {
        List<Student> list = new ArrayList<>();
        String pattern = "%" + (query == null ? "" : query.trim().toLowerCase()) + "%";
        String sql = "SELECT id, user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number, created_at " +
                     "FROM students WHERE LOWER(full_name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(department) LIKE ? OR LOWER(skills_interests) LIKE ? " +
                     "ORDER BY id ASC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id");
        if (!rs.wasNull()) {
            s.setUserId(uid);
        }
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setDepartment(rs.getString("department"));
        s.setStudyYear(rs.getInt("study_year"));
        s.setSkillsInterests(rs.getString("skills_interests"));
        s.setCareerGoal(rs.getString("career_goal"));
        s.setContactNumber(rs.getString("contact_number"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setCreatedAt(ts.toLocalDateTime());
        return s;
    }
}
